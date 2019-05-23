package eu.keystruck.customconsole;

import java.util.Optional;

public final class Command {
    private final String command;
    private final String help;
    private final Callback func;
    
    public Command(String command, String help, Callback func) {
        this.command = command;
        this.help = help;
        this.func = func;
    }
    
    //Passthrough command, just calls the execute command on callback.
    //Mutations are left up to the callback itself which would be up to the plugin program.
    public final <T> Optional<T> execute(String... arguments) {
        return this.func.execute(arguments);
    }
    
    public final String getCommand() { return this.command; }
    public final String getHelp() { return this.help; }
    public final Callback getCallback() { return this.func; } //This is purposfully like this.
}
