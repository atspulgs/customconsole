package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.CdCommand;
import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.commands.EchoCommand;
import eu.keystruck.customconsole.commands.ListCommand;
import eu.keystruck.customconsole.commands.LsCommand;
import eu.keystruck.customconsole.commands.ManCommand;
import eu.keystruck.customconsole.commands.PwdCommand;
import eu.keystruck.customconsole.commands.RCLCommand;
import eu.keystruck.customconsole.commands.VersionCommand;
import eu.keystruck.customconsole.uis.UserInterface;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public final class Controller implements InputListener, CommandListener {
    private static final Pattern ARG_SPLITTER = Pattern.compile("[ ](?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    private final HashMap<String,Command> cmdMap = new HashMap();
    private final HashMap<Long,Thread> threadList = new HashMap();
    private Path currentDirectory;
    private final Object cdLock = new Object();
    private final ArrayList<WorkingDirectoryListener> wdlisteners = new ArrayList();
    
    public Controller() {
        this.currentDirectory = new File(".").toPath();
        this.registerCommand(new ListCommand(this.cmdMap));
        this.registerCommand(new ManCommand(this.cmdMap));
        this.registerCommand(new RCLCommand(this.threadList));
        this.registerCommand(new VersionCommand());
        this.registerCommand(new PwdCommand(this));
        this.registerCommand(new EchoCommand());
        this.registerCommand(new LsCommand(this));
        this.registerCommand(new CdCommand(this));
    }
    
    public void cd(Path newcd) {
        synchronized(this.cdLock) {
            this.currentDirectory = newcd;
            System.setProperty("user.dir", newcd.toAbsolutePath().normalize().toString());
            this.wdlisteners.forEach((listener) -> listener.updateWorkingDirectory(this.currentDirectory));
        }
    }
    
    public Path getCd() {
        synchronized(this.cdLock) {
            return this.currentDirectory;
        }   
    }
    
    public void addWorkingDirectoryListener(WorkingDirectoryListener wdl) {
        if(wdl != null)
            this.wdlisteners.add(wdl);
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
        var cmdString = data.substring(0, data.contains(" ")? data.indexOf(" ") : data.length());
        var arguments = data.replaceFirst(cmdString, "").trim().split(ARG_SPLITTER.pattern());
        var c = this.cmdMap.get(cmdString);
        if(c != null) {
            Thread temp = new Thread(c.command) {
                @Override public void run() {
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
        if(cmd == null || this.cmdMap.containsKey(cmd.command))
            return;
        this.cmdMap.put(cmd.command, cmd);
    }
    
    @Override
    public synchronized void unregisterCommand(String cmd) {
        if(cmd == null || cmd.isEmpty() || cmd.isBlank())
            return;
        if(this.cmdMap.containsKey(cmd))
            this.cmdMap.remove(cmd);
    }
}
