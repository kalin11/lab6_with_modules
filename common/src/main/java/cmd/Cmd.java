package cmd;

import entity.Movie;
import java.io.Serializable;

public class Cmd implements Serializable {
    private String command;
    private String arguments;
    private String file;
    private Movie movie;

    public Cmd(String cmd, String args){
        command = cmd;
        arguments = args;
    }

    public Cmd(String cmd, String args, String file){
        command = cmd;
        arguments = args;
        this.file = file;
    }

    public Cmd(String cmd){
        command = cmd;
    }

    public Cmd(String cmd, String args, Movie movie){
        command = cmd;
        arguments = args;
        this.movie = movie;
    }

    public Cmd(String cmd, Movie movie){
        command = cmd;
        this.movie = movie;
    }

    public String toString(){
        return command + " " + arguments + " " + movie + "\n" + file;
    }

    public String getName(){
        return command;
    }
    public String getArguments(){
        return arguments;
    }
    public Movie getMovie(){
        return movie;
    }

    public String getFile(){
        return file;
    }
}
