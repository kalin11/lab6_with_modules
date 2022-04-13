package command.tasksCommands.with_arguments;

import command.parsing.CommandWithMovie;
import entity.Movie;
import visitor.Visitor;

import java.io.BufferedReader;

public class UpdateCommand implements CommandWithMovie {

    private Movie movie = null;

    private String[] args = null;

    public String toString(){
        return "update id {element} - обновить значение элемента коллекции, id которого равен заданному";
    }

    public Movie getMovie(){
        return movie;
    }

    public String[] getArgs(){
        return args;
    }

    @Override
    public Object accept(Visitor v, Movie movie) {
        return v.visit(this, movie);
    }

    @Override
    public boolean execute(String[] arguments, BufferedReader in) {
        args = arguments;
//        try {
//            long x = Long.parseLong(arguments[0]);
//        }catch (ArrayIndexOutOfBoundsException e){
//            System.out.print("");
//        } catch (NumberFormatException e) {
//            System.out.println("тип аргумента неправильный");
//        }

            return true;
        }
    public boolean valid(String[] args) {
        this.args = args;
        if (args.length==0){
            System.out.println("вы не ввели id фильма, который надо обновлять");
            return false;
        }
        else if (args.length > 1){
            System.out.println("вы ввели слишком много аругментов");
            return false;
        }
        else {
            try {
                long id = Long.parseLong(args[0]);
                return true;
            }catch (NumberFormatException e){
                System.out.println("тип аргумента неправильный");
                return false;
            }
        }

    }

}
