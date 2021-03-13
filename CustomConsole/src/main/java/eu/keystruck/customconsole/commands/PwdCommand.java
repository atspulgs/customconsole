package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.uis.UserInterface;
import java.nio.file.Path;

public class PwdCommand extends Command {
    private static final String NAME = "PWD";
    private static final String COMMAND = "pwd";
    private static final String DESCRIPTION = "Prints the current working directory.";
    private static final String SYNTAX = "<<color:255,120,120>><<weight:2.0>>pwd<</weight>><</color>>";
    private static final String NOTES = "";
    
    private final Controller co;
    
    public PwdCommand(final Controller co) {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
        this.co = co;
    }

    @Override
    public void execute(UserInterface io, String... args) {
        String presentWorkingDirectory = this.co.getCd().toAbsolutePath().normalize().toString();
        io.push("<<color:100,255,100>>"+presentWorkingDirectory+"<</color>>");
    }
}
