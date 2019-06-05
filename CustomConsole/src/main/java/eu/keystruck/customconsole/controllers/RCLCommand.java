package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;

public class RCLCommand extends Command{
    private static final String COMMAND = "rcls";
    private static final String DESCRIPTION = "Shows a list of currently running commands."
            + "\n\tMay also target a specific thread ID to get details on that thread alone."
            + "\n\tSyntax: rcls [<target>]";
    private final HashMap<Long, Thread> threads;
    
    public RCLCommand(final HashMap<Long, Thread> threads) {
        super(COMMAND,DESCRIPTION);
        if(threads == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nio = " + String.valueOf(threads)
            );
        this.threads = threads;
    }
    
    @Override
    public void execute(final UserInterface io, final String... args) {
        if(io == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nio = " + String.valueOf(io)
            );
        if(args == null || args.length <= 0 || args[0].isEmpty() || args[0].isBlank()) {
            io.push("Thread ID | Thread Name | Thread Class Name");
            this.threads.values().forEach((t) -> {
                io.push(t.getId() + " | " + t.getName() + " | " + t.getClass().getName());
            });
            io.push("-------------------------------------------");
        } else {
            var thread = this.threads.get(Long.parseLong(args[0]));
            if(thread != null) {
                io.push(thread.getId() + " | " + thread.getName() + " | " + thread.getClass().getName());
            } else {
                io.push("Could not find the specified target Thread!");
            }
        }
    }
    
}
