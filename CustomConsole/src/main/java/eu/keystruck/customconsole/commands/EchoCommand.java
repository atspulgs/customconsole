package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.uis.UserInterface;

public class EchoCommand extends Command{
    private static final String NAME = "Echo";
    private static final String COMMAND = "echo";
    private static final String DESCRIPTION = "Prints the string that was provided to the command. This will process the markup language and apply the rules before displaying it";
    private static final String SYNTAX = "<<color:255,120,120>><<weight:2.0>>echo [string...]<</weight>><</color>>";
    private static final String NOTES = "";
    
    public EchoCommand() {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
    }

    @Override
    public void execute(UserInterface io, String... args) {
        if(args == null || args.length < 1) {
            io.push("No arguments provided.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(String arg : args)
            sb.append(arg).append(" ");
        io.push(sb.toString());
    }
}
