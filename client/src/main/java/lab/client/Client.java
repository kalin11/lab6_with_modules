package lab.client;

import cmd.Cmd;
import command.CommandName;
import command.parsing.ClientLineParser;
import command.parsing.Command;
import command.parsing.ObjectParser;
import command.tasksCommands.with_arguments.*;
import command.tasksCommands.without_arguments.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;


public class Client {
    private static final String EXECUTE_SCRIPT_STRING = "execute_script";
    public static void main(String[] args) {
        try {
            boolean okay = false;
            Selector selector = Selector.open();
            SocketChannel clientSocket = SocketChannel.open();
            clientSocket.configureBlocking(false);
            while (!okay)
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("введите хост сервера");
                String host = scanner.next();
                System.out.println("введите порт сервера");
                int port = scanner.nextInt();
                clientSocket.connect(new InetSocketAddress(host, port));
                okay = true;
            }catch (UnresolvedAddressException e){
                System.out.println("вы ввели что-то не то");
            }
            clientSocket.register(selector, SelectionKey.OP_CONNECT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    SocketChannel client = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        if (client.isConnectionPending()) {
                            try {
                                client.finishConnect();
                                System.out.println("Добро пожаловать, новый пользователь!\nДля получения списка всех команд введте 'help'");
                            } catch (IOException e) {
                                System.out.println("IO");
                            }
                        }
                        client.register(selector, SelectionKey.OP_WRITE);
                        continue;
                    }
                    if (key.isWritable()) {
                        //вот ввели какую-то команду здесь
                        //дальше мы должны проверить ее валидность, если она не валидная, сказать что дебик клиент
                        //далее если она норм делаем объект комманды и отправляем уже на сервак
                        System.out.print(">>> ");

                        ClientLineParser clientLineParser = new ClientLineParser(new BufferedReader(new InputStreamReader(System.in)), initCommands());
                        String str = (String) clientLineParser.readLine();
                        String[] cmdWithArgs = str.replaceAll("\\s+", " ").split(" ");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);

                        if (cmdWithArgs[0].equals("exit")) {
                            Cmd cmd = new Cmd(cmdWithArgs[0]);
                            outputStream.writeObject(cmd);
                            outputStream.writeObject(cmd);
                            try {
                                client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                            } catch (IOException e) {
                                System.out.println("сервер не отвечает");
                                continue;
                            }
                            System.exit(0);
                        } else if (cmdWithArgs[0].equals("")) {

                        } else if (cmdWithArgs.length == 1) {
                            if (cmdWithArgs[0].equals("add") || cmdWithArgs[0].equals("remove_lower") || cmdWithArgs[0].equals("remove_greater")) {
                                Cmd cmd = new Cmd(cmdWithArgs[0], new ObjectParser().parseObject(new BufferedReader(new InputStreamReader(System.in))));
                                outputStream.writeObject(cmd);
                                outputStream.flush();
                                try {
                                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                } catch (IOException e) {
                                    System.out.println("сервер не отвечает");
                                    continue;
                                }
                            } else {
                                Cmd command = new Cmd(cmdWithArgs[0]);
                                outputStream.writeObject(command);
                                outputStream.flush();
                                try {
                                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                } catch (IOException e) {
                                    System.out.println("сервер не отвечает");
                                    continue;
                                }
                            }
                            clientSocket.register(selector, SelectionKey.OP_READ);
                        } else if (cmdWithArgs.length == 2) {
                            if (cmdWithArgs[0].equals("update")) {
                                Cmd cmd = new Cmd(cmdWithArgs[0], cmdWithArgs[1], new ObjectParser().parseObject(new BufferedReader(new InputStreamReader(System.in))));
                                cmd.getMovie().setId(Long.parseLong(cmdWithArgs[1]));
                                outputStream.writeObject(cmd);
                                outputStream.flush();
                                try {
                                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                } catch (IOException e) {
                                    System.out.println("сервер не отвечает");
                                    continue;
                                }
                                clientSocket.register(selector, SelectionKey.OP_READ);
                            } else if (cmdWithArgs[0].equals("execute_script")) {
                                try {
                                    String commands = "";
                                    String rec = "";
                                    HashSet<String> pathes = new HashSet<>();
                                    pathes.add(cmdWithArgs[1]);
                                    List<String> f = Files.readAllLines(Paths.get(cmdWithArgs[1]));
                                    for (int i = 0; i < f.size(); i++) {
                                        String line = f.get(i);
                                        if (line.toLowerCase(Locale.ROOT).startsWith(EXECUTE_SCRIPT_STRING)) {
                                            String filePath = line.substring(EXECUTE_SCRIPT_STRING.length()).trim();
                                            if (pathes.contains(filePath)) {
                                                f.remove(i);
                                                rec = "цикл";
                                                System.out.println("вы пытаетесь зациклить скрипт");
                                                continue;
                                            } else {
                                                pathes.add(filePath);
                                                List<String> ftmp = Files.readAllLines(Paths.get(filePath));
                                                f.addAll(i + 1, ftmp);
                                                //noinspection SuspiciousListRemoveInLoop
                                                f.remove(i);
                                            }
                                        }
                                    }
                                    try {
                                        f.removeIf(s -> s.equals(""));
                                        for (String s : f) {
                                            commands += s + "!";
                                        }
                                    } catch (StringIndexOutOfBoundsException e) {
                                        System.out.print("");
                                    }
                                    if (rec.equals("цикл")) {
                                        continue;
                                    } else {
                                        Cmd cmd = new Cmd(cmdWithArgs[0], cmdWithArgs[1], commands.substring(commands.length() - 1));
                                        outputStream.writeObject(cmd);
                                        outputStream.flush();
                                    }
                                } catch (NoSuchFileException e) {
                                    System.out.print("");
                                    continue;
                                }
                                try {
                                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                } catch (IOException e) {
                                    System.out.println("сервер не отвечает");
                                    continue;
                                }
                                clientSocket.register(selector, SelectionKey.OP_READ);
                            } else {
                                Cmd command = new Cmd(cmdWithArgs[0], cmdWithArgs[1]);
                                outputStream.writeObject(command);
                                outputStream.flush();
                                try {
                                    client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                                } catch (IOException e) {
                                    System.out.println("сервер не отвечает");
                                    continue;
                                }
                                clientSocket.register(selector, SelectionKey.OP_READ);
                            }
                        }
                    }
                    //то есть у меня сейчас на руках сама команда и введенные аргументы
                    //пора бы сделать объект команды,в параметры передать ее название и аргс
                    //а потом можно и передать объект на сервер
                    if (key.isReadable()) {
                        client = (SocketChannel) key.channel();
                        ByteBuffer data = ByteBuffer.allocate(16384);
                        try {
                            client.read(data);
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.array());
                            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                            Object o = objectInputStream.readObject();
                            System.out.println("server's answer - \n" + o);
                            clientSocket.register(selector, SelectionKey.OP_WRITE);
                        } catch (IOException e) {
                            System.out.println("сервак отлетел");
                            key.cancel();
                            continue;
                        }

                    }
                }
            }
        }catch (IOException e){
            System.out.println("IO");
        }catch (ClassNotFoundException e){
            System.out.println("ClassNot");
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
}
