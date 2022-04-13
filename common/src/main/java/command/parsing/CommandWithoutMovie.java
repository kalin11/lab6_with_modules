package command.parsing;


import visitor.Visitor;

public interface CommandWithoutMovie extends Command {
    Object accept(Visitor v);
}
