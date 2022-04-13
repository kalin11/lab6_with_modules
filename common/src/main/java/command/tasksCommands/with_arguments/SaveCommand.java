//package lab5.client.command.tasksCommands.with_arguments;
//
//import lab5.client.command.parsing.Command;
//import lab5.server.visitor.Visitor;
//
//import java.io.BufferedReader;
//
//public class SaveCommand implements Command {
//
//    private String[] args = null;
//
//    public String[] getArgs(){
//        return args;
//    }
//
//    @Override
//    public String toString(){
//        return "save: сохранить коллекцию в файл";
//    }
//
//    @Override
//    public void accept(Visitor v) {
//        v.visit(this);
//    }
//
//    @Override
//    public boolean execute(String[] arguments, BufferedReader in) {
//        args = arguments;
//        return true;
//    }
//    public boolean valid(String[] args) {
//        this.args = args;
//        if (args.length==0){
//            System.out.println("вы не ввели аргументы");
//            return false;
//        }
//        else if (args.length > 1){
//            System.out.println("вы ввели слишком много аругментов");
//            return false;
//        }
//        else return true;
//    }
//}
