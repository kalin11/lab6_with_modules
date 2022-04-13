package visitor;

import command.tasksCommands.with_arguments.*;
import command.tasksCommands.without_arguments.*;
import entity.Movie;

public interface Visitor {

    Object visit(AddCommand c, Movie movie);
    Object visit(HelpCommand c);
    Object visit(ShowCommand c);
    Object visit(HeadCommand c);
    Object visit(ClearCommand c);
    Object visit(InfoCommand c);
    Object visit(AverageOfLength c);
    Object visit(PrintUniqueOscarsCount c);
    Object visit(UpdateCommand c, Movie movie);
    Object visit(RemoveCommand c);
    Object visit(CountByGenreCommand c);
    Object visit(RemoveGreaterCommand c, Movie movie);
    Object visit(RemoveLowerCommand c, Movie movie);
    Object visit(ExecuteScriptCommand c);
    Object visit(ExecuteScriptCommand c, String str);
}


