package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.uis.UserInterface;

public abstract class Command {
    public final String command;
    public final String description;
    
    public Command(String command, String description) {
        if(command == null || description == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \ncommand = " + String.valueOf(command)
                + " | description = " + String.valueOf(description)
            );
        this.command = command;
        this.description = description;
    }
    
    public abstract void execute(final UserInterface io, final String... args);
    public String getCommand() { return this.command; }
    public String getDescription() { return this.description; }
}
