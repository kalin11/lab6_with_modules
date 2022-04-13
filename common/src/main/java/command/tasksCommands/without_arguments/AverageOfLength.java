package command.tasksCommands.without_arguments;


import command.parsing.CommandWithoutMovie;
import visitor.Visitor;

import java.io.BufferedReader;

public class AverageOfLength implements CommandWithoutMovie {

    private String[] args;

    public String[] getArgs(){
        return args;
    }

    public String toString(){
        return "average_of_length : вывести среднее значение поля length для всех элементов коллекции";
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
            System.out.println("команда 'average_of_length' должна быть без аргументов");
            return false;
        }
        else {
            return true;
        }
    }
}
