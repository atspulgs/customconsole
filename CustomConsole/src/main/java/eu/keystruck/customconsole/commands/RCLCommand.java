package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.interpriter.UChars;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;

public class RCLCommand extends Command {
    private static final String NAME = "Running Command List";
    private static final String COMMAND = "rcls";
    private static final String DESCRIPTION = "Shows a list of currently running commands.\n"
            +"May also target a specific thread ID to get details on that thread alone.";
    private static final String SYNTAX = "<<color:255,120,120>><<weight:2.0>>rcls [<target>]<</weight>><</color>>";
    private static final String NOTES = "";
    private final HashMap<Long, Thread> threads;
    
    public RCLCommand(final HashMap<Long, Thread> threads) {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
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
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("<<font:Consolas>><<weight:2.0>>%1$10s %2$1s %3$25s %2$1s %4$s<</weight>><</font>>\n",
                    "Thread ID", UChars.S_VERTICAL, "Thread Name", "Thread Class Name"));
            this.threads.values().forEach((t) -> {
                sb.append(String.format("<<font:Consolas>><<color:100,100,255>>%2$10s<</color>> %1$1s <<color:100,255,100>>%3$-25s<</color>> %1$1s %4$s<</font>>\n",
                        UChars.S_VERTICAL, t.getId(), t.getName(), t.getClass().getName()));
            });
            io.push(sb.toString());
        } else {
            try {
                var thread = this.threads.get(Long.parseLong(args[0]));
                if(thread != null) {
                    io.push("<<font:Courier>><<color:100,100,255>>"+String.format("%9s", thread.getId()) + " <</color>>|<<color:100,255,100>> " + String.format("%-20s", thread.getName()) + " <</color>>| " + thread.getClass().getName()+"<</font>>");
                } else {
                    io.push("Could not find the specified target Thread!");
                }
            } catch(NumberFormatException ex) {
                io.push("Could not find the specified target Thread!");
            }
        }
    }
    
}
