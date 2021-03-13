package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.uis.UserInterface;
import java.io.File;
import java.nio.file.Path;

public class CdCommand extends Command {
    private static final String NAME = "CD";
    private static final String COMMAND = "cd";
    private static final String DESCRIPTION = "Changes the current directory.";
    private static final String SYNTAX = "<<color:255,120,120>><<weight:2.0>>cd <path><</weight>><</color>>";
    private static final String NOTES = "";
    
    private final Controller co;

    public CdCommand(final Controller co) {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
        this.co = co;
    }

    @Override
    public void execute(UserInterface io, String... args) {
        if(args != null && args.length > 0 && !args[0].isEmpty() && !args[0].isBlank()) {
            if(args[0].startsWith("~"))
                args[0] = args[0].replaceFirst("~", System.getProperty("user.home").replaceAll("\\\\", "/"));
            Path newLoc = this.co.getCd().resolve(args[0]);
            File newLocFile = newLoc.toFile();
            if(newLocFile.exists() && newLocFile.isDirectory()) {
                this.co.cd(newLoc);
            }
        } else {
            io.push("Couldn't change the directory, Requires a target.");
        }
    }
}
