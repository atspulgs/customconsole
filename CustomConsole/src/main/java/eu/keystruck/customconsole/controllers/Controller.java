package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;
import java.util.HashMap;
import java.util.regex.Pattern;

public final class Controller implements InputListener, CommandListener {
    private static final Pattern ARG_SPLITTER = Pattern.compile("[ ](?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    private final HashMap<String,Command> cmdMap = new HashMap();
    private final HashMap<Long,Thread> threadList = new HashMap();
    
    public Controller() {
        this.registerCommand(new ListCommand(this.cmdMap));
        this.registerCommand(new ManCommand(this.cmdMap));
        this.registerCommand(new RCLCommand(this.threadList));
    }
    
    @Override
    public synchronized void notifyHolder(final Thread child) {
        if(child == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument."
            );
        this.threadList.remove(child.getId());
    }
    
    @Override
    public void pull(UserInterface source, String data) {
        if(source == null || data == null || data.trim().isEmpty() || data.trim().isBlank())
            return;
        data = data.trim();
        //"man man" didnt work and I dont know why.
        var cmdString = data.substring(0, data.contains(" ")? data.indexOf(" ") : data.length());
        var arguments = data.replaceFirst(cmdString, "").trim().split(ARG_SPLITTER.pattern());
        var c = this.cmdMap.get(cmdString);
        if(c != null) {
            Thread temp = new Thread(c.getCommand()) {
                public void run() {
                    c.execute(source, arguments);
                    notifyHolder(this);
                }
            };
            this.threadList.put(temp.getId(), temp);
            temp.start();
        } else source.push("Command not recognized.");
    }

    @Override
    public synchronized void registerCommand(Command cmd) {
        if(cmd == null || this.cmdMap.containsKey(cmd.getCommand()))
            return;
        this.cmdMap.put(cmd.getCommand(), cmd);
    }
    
    @Override
    public synchronized void unregisterCommand(String cmd) {
        if(cmd == null || cmd.isEmpty() || cmd.isBlank())
            return;
        if(this.cmdMap.containsKey(cmd))
            this.cmdMap.remove(cmd);
    }
}
