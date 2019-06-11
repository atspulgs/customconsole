package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;

public class RCLCommand extends Command{
    private static final String COMMAND = "rcls";
    private static final String DESCRIPTION = "Shows a list of currently running commands."
            + "\n\tMay also target a specific thread ID to get details on that thread alone."
            + "\n\tSyntax: <<color:255,120,120>><<weight:2.0>>rcls [<target>]<</weight>><</color>>";
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
            io.push("<<font:Courier>><<weight:2.0>>Thread ID | Thread Name          | Thread Class Name<</weight>><</font>>");
            this.threads.values().forEach((t) -> {
                io.push("<<font:Courier>><<color:100,100,255>>"+String.format("%9s", t.getId()) + " <</color>>|<<color:100,255,100>> " + String.format("%-20s", t.getName()) + " <</color>>| " + t.getClass().getName()+"<</font>>");
            });
            io.push("-------------------------------------------");
        } else {
            var thread = this.threads.get(Long.parseLong(args[0]));
            if(thread != null) {
                io.push("<<font:Courier>><<color:100,100,255>>"+String.format("%9s", thread.getId()) + " <</color>>|<<color:100,255,100>> " + String.format("%-20s", thread.getName()) + " <</color>>| " + thread.getClass().getName()+"<</font>>");

            } else {
                io.push("Could not find the specified target Thread!");
            }
        }
    }
    
}
