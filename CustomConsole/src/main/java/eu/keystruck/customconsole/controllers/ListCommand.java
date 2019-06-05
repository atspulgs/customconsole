package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;

public class ListCommand extends Command {
    private static final String COMMAND = "commands";
    private static final String DESCRIPTION = "Lists all currently registered commands.";
    private HashMap<String,Command> traversable = null;
    
    public ListCommand(HashMap<String,Command> traversable) {
        super(COMMAND,DESCRIPTION);
        if(traversable != null)
            this.traversable = traversable;
    }
    
    @Override
    public void execute(UserInterface io, String... args) {
        if(io == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nio = " + String.valueOf(io)
            );
        if(this.traversable == null || this.traversable.isEmpty()) {
            io.push("Could not retrieve the command list!");
            return;
        }
        this.traversable.values().forEach((item) -> {
            //Potentially other formats here.
            
            //default output
            io.push(item.getCommand() + " - " + item.getDescription());
        });
    }
    
}
