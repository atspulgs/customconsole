package eu.keystruck.customconsole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Interpreter {
    private static final Pattern ARG_SPLITTER = Pattern.compile(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    
    public static class CommandCall {
        private final String call;
        private final ArrayList<String> args = new ArrayList();
        public CommandCall(String call, String... args) {
            if(call == null || call.isBlank())
                throw new NullPointerException("NO!");
            this.call = call;
            if(args.length > 0)
                this.args.addAll(Arrays.asList(args));
        }
        public String getCall() { return this.call; }
        public String[] getArgs() { return this.args.toArray(new String[1]); }
    }
    
    public static final CommandCall interpret(String command) {
        if(command == null || command.isBlank())
            throw new NullPointerException("NO!");
        command = command.trim();
        var c = command.substring(0, command.contains(" ")? command.indexOf(" "): command.length());
        var a = command.length() > c.length() ? command.replace(c, ""): "";
        if(a.isBlank())
            return new CommandCall(c);
        var as = a.split(ARG_SPLITTER.pattern());
        return new CommandCall(c, as);
    }
}
