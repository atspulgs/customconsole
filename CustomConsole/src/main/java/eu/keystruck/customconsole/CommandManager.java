package eu.keystruck.customconsole;

import java.util.HashMap;

public final class CommandManager {
    private final HashMap<String, Command> commands = new HashMap();
    
    //some form of constructor?
    
    public final void register(Command cmd) {
        if(!this.commands.containsKey(cmd.getCommand()))
            this.commands.put(cmd.getCommand(), cmd);
    }
    
    public final void unregister(String command) {
        if(this.commands.containsKey(command))
            this.commands.remove(command);
    }
    
    public final void unregister(Command cmd) {
        this.unregister(cmd.getCommand());
    }
    
    public final Command find(String command) {
        if( command != null && this.commands.containsKey(command))
            return this.commands.get(command);
        return null;
    }
}
