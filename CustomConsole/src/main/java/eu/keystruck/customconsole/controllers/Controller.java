package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Controller implements InputListener {
    private static final Pattern ARG_SPLITTER = Pattern.compile("[ ](?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    private final HashMap<String,Command> cmdMap = new HashMap();

    @Override
    public void pull(UserInterface source, String data) {
        if(source == null || data == null || data.trim().isEmpty() || data.trim().isBlank())
            return;
        data = data.trim();
        var cmdString = data.substring(0, data.contains(" ")? data.indexOf(" ") : data.length());
        var arguments = data.replace(cmdString, "").split(ARG_SPLITTER.pattern());
        if(this.cmdMap.containsKey(cmdString))
            this.cmdMap.get(cmdString).execute(source, arguments);
    }

    @Override
    public void registerCommand(Command cmd) {
        if(cmd == null || this.cmdMap.containsKey(cmd.getCommand()))
            return;
        this.cmdMap.put(cmd.getCommand(), cmd);
    }
    
    public void unregisterCommand(String cmd) {
        if(cmd == null || cmd.isEmpty() || cmd.isBlank())
            return;
        if(this.cmdMap.containsKey(cmd))
            this.cmdMap.remove(cmd);
    }
}
