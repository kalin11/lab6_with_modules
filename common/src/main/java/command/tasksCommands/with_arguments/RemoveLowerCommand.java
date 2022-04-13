package command.tasksCommands.with_arguments;

import command.parsing.CommandWithMovie;
import entity.Movie;
import visitor.Visitor;

import java.io.BufferedReader;

public class RemoveLowerCommand implements CommandWithMovie {

    private Movie movie = null;

    private String[] args = null;
    public String[] getArgs(){
        return args;
    }

    public String toString(){
        return "remove_lower {element} - удалить из коллекции все элементы, меньшие,чем заданный";
    }

    @Override
    public Object accept(Visitor v, Movie movie) {
        return v.visit(this, movie);
    }

    public Movie getMovie(){
        return movie;
    }

    @Override
    public boolean execute(String[] arguments, BufferedReader in) {
        args = arguments;
        return true;
    }
    public boolean valid(String[] args) {
        this.args = args;
        if (args.length == 0){
            return true;
        }
        else{
            System.out.println("вы должны вводить значения полей в консоли");
            return false;
        }
    }
}
