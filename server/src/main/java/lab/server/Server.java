package lab.server;

import cmd.Cmd;
import collection.CollectionReader;
import collection.LinkedCollection;
import command.parsing.Command;
import command.parsing.CommandLineParser;
import command.tasksCommands.with_arguments.*;
import command.tasksCommands.without_arguments.*;
import visitor.*;
import entity.*;
import command.*;



import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Сервер запущен!");
        LinkedCollection collection = new LinkedCollection();
        HashMap<CommandName, Command> commands = initCommands();
        boolean correctPath = true;
        if (args.length == 0){
            System.out.println("не введен путь к файлу");
        }
        else {
            while (correctPath) {
                String filePath = args[0];
                File file = new File(filePath);
                if (!file.exists()) {
                    System.out.println("указанный файл не существует");
                    return;
                } else if (!file.canWrite() || !file.canRead()) {
                    System.out.println("файл не доступен для чтения/записи");
                    return;
                } else if (file.isDirectory()) {
                    System.out.println("введен не файл, а директория");
                    return;
                } else {
                    if (file.length() == 0) {
                        collection = new LinkedCollection();
                        correctPath = false;
                    } else {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("введите хост сервера");
                        String host = scanner.next();
                        System.out.println("введите порт сервера");
                        int port = scanner.nextInt();
                        CollectionReader collectionReader = new CollectionReader(collection);
                        collection = collectionReader.read(filePath);
                        System.out.println("Загружена коллекция \n" + collection);
                        VisitorImpl visitor = new VisitorImpl(collection, initCommands());
                        Selector selector = Selector.open();
                        ServerSocketChannel server = ServerSocketChannel.open();
                        server.configureBlocking(false);
                        server.socket().bind(new InetSocketAddress(host, port));
                        server.register(selector, SelectionKey.OP_ACCEPT);
                        while (true) {
                            SocketChannel client;
                            selector.select();
                            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                            while (iterator.hasNext()) {
                                SocketChannel channel = null;
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                if (key.isAcceptable()) {
                                    client = server.accept();
                                    System.out.println("подключился пользователь " + client.toString().substring(42));
                                    client.configureBlocking(false);
                                    client.register(selector, SelectionKey.OP_READ);
                                }
                                if (key.isReadable()) {
                                    channel = (SocketChannel) key.channel();
                                    ByteBuffer data = ByteBuffer.allocate(16384);
                                    try {
                                        channel.read(data);
                                    } catch (IOException e) {
                                        System.out.println("пользователь отключился");
                                        key.cancel();
                                        continue;
                                    }
                                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
                                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                                    Cmd cmd = (Cmd) objectInputStream.readObject();
                                    if (cmd.getArguments() == null) {
                                        if (cmd.getMovie() != null){
                                            System.out.println("client's message - " + cmd.getName() + " " + cmd.getMovie());
                                        }
                                        else System.out.println("client's message - " + cmd.getName());
                                    } else {
                                        System.out.println("client's message - " + cmd);
                                    }

                                    if (channel != null) {
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);

                                        //отправить мы должны ответ команды, это может быть как список, объект Movie, еще чот
                                        //здесь опять идет проверка на валидность
                                        //далее мы запускаем какое-то исполнение команды и выхватываем результат
                                        if (cmd.getArguments() == null) {
                                            if (cmd.getName().equals("exit")) {
                                                collection.save("laba.txt");
                                                System.out.println("коллекция была сохранена в файл 'laba.txt'");
                                            } else if (cmd.getName().equals("add") || cmd.getName().equals("remove_lower") || cmd.getName().equals("remove_greater")) {
                                                Movie movie = cmd.getMovie();
                                                String[] temp = cmd.toString().split(" ");
                                                if (temp[2] != null) {
                                                    CommandLineParser parser = new CommandLineParser(temp[0] + " " + movie.allInfo(), initCommands());
                                                    Object o = parser.exe(visitor);
                                                    outputStream.writeObject(o);
                                                    outputStream.flush();
                                                    channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

                                                }
                                            } else {
                                                CommandLineParser parser = new CommandLineParser(cmd.getName(), initCommands());
                                                Object o = parser.exe(visitor);
                                                outputStream.writeObject(o);
                                                outputStream.flush();
                                                channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                                System.out.println("sending answer to the client");
                                            }
                                        } else {
                                            if (cmd.getMovie() != null) {
                                                CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getMovie().allInfo(), initCommands());
                                                Object o = parser.exe(visitor);
                                                outputStream.writeObject(o);
                                                outputStream.flush();
                                                channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                                System.out.println("sending answer to the client");
//                                CommandLineParser parser = new CommandLineParser()
                                            } else {
                                                CommandLineParser parser = new CommandLineParser(cmd.getName() + " " + cmd.getArguments() + " " + cmd.getFile(), initCommands());
                                                Object o = parser.exe(visitor);
                                                outputStream.writeObject(o);
                                                outputStream.flush();
                                                channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                                System.out.println("sending answer to the client");
                                            }
                                        }
                                        outputStream.close();
                                    }
                                }

                                //получили команду, в зависимости от нее
                                //отправляем дальше на исполнение
                                //получаем какой-то результат
                                //получаем его
                                //а дальше отправялем клиенту
                            }
                            System.out.println("сервер ожидает дальнейших действий.");
                        }
                    }
                }
            }
        }

    }
    public static HashMap<CommandName, Command> initCommands() {
        HashMap<CommandName, Command> commands = new HashMap<>();
        commands.put(CommandName.HELP, new HelpCommand());
        commands.put(CommandName.EXIT, new ExitCommand());
        commands.put(CommandName.ADD, new AddCommand());
        commands.put(CommandName.SHOW, new ShowCommand());
        commands.put(CommandName.HEAD, new HeadCommand());
        commands.put(CommandName.INFO, new InfoCommand());
        commands.put(CommandName.AVERAGE_OF_LENGTH, new AverageOfLength());
        commands.put(CommandName.PRINT_UNIQUE_OSCARS_COUNT, new PrintUniqueOscarsCount());
        commands.put(CommandName.UPDATE_ID, new UpdateCommand());
        commands.put(CommandName.REMOVE_BY_ID, new RemoveCommand());
        commands.put(CommandName.COUNT_BY_GENRE, new CountByGenreCommand());
        commands.put(CommandName.REMOVE_GREATER, new RemoveGreaterCommand());
        commands.put(CommandName.CLEAR, new ClearCommand());
        commands.put(CommandName.REMOVE_LOWER, new RemoveLowerCommand());
        commands.put(CommandName.EXECUTE_SCRIPT, new ExecuteScriptCommand());
        return commands;
    }

    // getName у команд
}
