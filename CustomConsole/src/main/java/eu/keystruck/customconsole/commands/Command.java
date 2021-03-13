package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.interpriter.UChars;
import eu.keystruck.customconsole.uis.UserInterface;
import java.io.BufferedReader;

public abstract class Command {
    public final String command;
    public final String name;
    public final String description;
    public final String syntax;
    public final String notes;
    public final String help;
    
    public Command(final String command, final String name, final String description, final String syntax, final String notes) {
        if(command == null || description == null || syntax == null || notes == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \ncommand = " + String.valueOf(command)
                + " \ndescription = " + String.valueOf(description)
                + " \nsyntax = " + String.valueOf(syntax)
                + " \nnotes = " + String.valueOf(notes)
            );
        this.command = command;
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.notes = notes;
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%1$1s %2$s\n", UChars.S_VERTICAL, String.valueOf(this.name)));
        sb.append(String.format("%1$1s%2$103s\n", UChars.S_VRJUNCTION, String.valueOf(UChars.S_HORIZONTAL).repeat(103) ));
        sb.append(String.format("%1$1s%2$3s %3$11s %4$87s\n", UChars.S_VRJUNCTION, String.valueOf(UChars.S_HORIZONTAL).repeat(3),
                "Description", String.valueOf(UChars.S_HORIZONTAL).repeat(87)));
        for(String line : this.description.split("\n")) 
            sb.append(String.format("%1$1s %2$s\n", UChars.S_VERTICAL, line));
        sb.append(String.format("%1$1s%2$3s %3$6s %4$92s\n", UChars.S_VRJUNCTION, String.valueOf(UChars.S_HORIZONTAL).repeat(3),
                "Syntax", String.valueOf(UChars.S_HORIZONTAL).repeat(92)));
        for(String line : this.syntax.split("\n")) 
            sb.append(String.format("%1$1s %2$s\n", UChars.S_VERTICAL, line));
        sb.append(String.format("%1$1s%2$3s %3$5s %4$93s\n", UChars.S_VRJUNCTION, String.valueOf(UChars.S_HORIZONTAL).repeat(3),
                "Notes", String.valueOf(UChars.S_HORIZONTAL).repeat(93)));
        for(String line : this.notes.split("\n")) 
            sb.append(String.format("%1$1s %2$s\n", UChars.S_VERTICAL, line));
        this.help = sb.toString();
    }
    
    public abstract void execute(final UserInterface io, final String... args);
}
