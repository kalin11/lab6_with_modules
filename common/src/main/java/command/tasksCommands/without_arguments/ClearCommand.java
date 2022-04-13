package command.tasksCommands.without_arguments;

import command.parsing.CommandWithoutMovie;
import visitor.Visitor;

import java.io.BufferedReader;

public class ClearCommand implements CommandWithoutMovie {
    private String[] args;

    public String[] getArgs(){
        return args;
    }

    @Override
    public String toString(){
        return "clear : очистить коллекцию";
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
            System.out.println("команда 'clear' должна быть без аргументов");
            return false;
        }
        else {
            return true;
        }
    }
}
