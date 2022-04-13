package command.tasksCommands.without_arguments;

//import lab5.Collection.CollectionManager;
import command.parsing.CommandWithoutMovie;
import visitor.Visitor;

import java.io.BufferedReader;

public class HeadCommand implements CommandWithoutMovie {
    private String[] args;

    public String[] getArgs(){
        return args;
    }

    @Override
    public String toString() {
        return "head - вывести первый элемент коллекции";
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean execute(String[] arguments, BufferedReader in) {
        args = arguments;
        return true;
    }
    public boolean valid(String[] args) {
        this.args = args;
        if (args.length!=0){
            System.out.println("команда 'head' должна быть без аргументов");
            return false;
        }
        else {
            return true;
        }
    }


}

//public static class Parser implements CommandParser<HeadCommand, String> {
//    LinkedCollection linkedCollection = new LinkedCollection();
//
//    @Override
//    public String parseCommandLine(String line) {
//        return "head" == line ? line : null;
//    }
//
//    @Override
//    public HeadCommand parseCommand(String lineParsingResult, BufferedReader in) {
//
//        return new HeadCommand();
//    }
//}
