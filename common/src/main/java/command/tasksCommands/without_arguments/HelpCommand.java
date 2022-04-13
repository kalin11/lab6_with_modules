package command.tasksCommands.without_arguments;

import command.parsing.CommandWithoutMovie;
import visitor.Visitor;

import java.io.BufferedReader;
import java.io.Serializable;

public class HelpCommand implements CommandWithoutMovie, Serializable {
    private String[] args;

    public String[] getArgs(){
        return args;
    }


    @Override
    public String toString() {
        return "help - вывести инфо о командах";
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
            System.out.println("команда 'help' должна быть без аргументов");
            return false;
        }
        else {
            return true;
        }
    }
}
