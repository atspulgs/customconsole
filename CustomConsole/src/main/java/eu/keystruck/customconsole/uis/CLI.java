package eu.keystruck.customconsole.uis;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.controllers.InputListener;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI extends UserInterface {
    private final Scanner in;
    private final PrintStream out;
    
    private boolean running = true;
    
    public CLI(InputListener il, InputStream in, PrintStream out) {
        super(il);
        if(in == null || out == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nin = " + String.valueOf(in)
                + " | out = " + String.valueOf(out)
            );
        this.in = new Scanner(in);
        this.out = out;
        
        this.listener.registerCommand(new Command("exit", "Exit", "This is a command registered by the CLI object!\n"
                + "The purpose of this command is to stop the execution of the Thread and ultimetally kill the UI itself.\n"
                + "Depending on the implementation of the parent process, it could exit the program too.", "", "") {
            @Override public void execute(UserInterface io, String... args) {
                if(io == null)
                    throw new NullPointerException(
                        this.getClass().getName() 
                        + ": Null value as an argument. Following arguments must not be null:"
                        + " \nio = " + String.valueOf(io)
                    );
                if(io instanceof CLI) {
                    ((CLI)io).exit();
                    io.push("Exiting...");
                } else io.push("This command can only run on CLI Object");
            }
        });
    }
    
    @Override
    public synchronized String fetch(String data) {
        this.out.println(String.valueOf(data));
        this.out.print("- > ");
        return this.in.nextLine();
    }

    @Override
    public synchronized void push(String data) {
        this.out.println(String.valueOf(data));
    }

    @Override
    public void run() {
        if(this.listener == null) {
            this.out.println("No InputListener found, Exiting...");
            return;
        }
        
        do {
            this.out.print("- > ");
            this.listener.pull(this, this.in.nextLine());
        } while(this.running);
    }
    
    public void exit() {
        this.running = false;
    }
}
