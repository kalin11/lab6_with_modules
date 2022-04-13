package command.parsing;


import entity.Movie;
import visitor.Visitor;

public interface CommandWithMovie extends Command{
    Object accept(Visitor v, Movie movie);
}
