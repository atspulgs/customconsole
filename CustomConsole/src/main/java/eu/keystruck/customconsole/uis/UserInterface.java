package eu.keystruck.customconsole.uis;

import eu.keystruck.customconsole.controllers.InputListener;

public abstract class UserInterface implements Runnable {
    protected final InputListener listener;
    
    protected UserInterface(InputListener il) {
        if(il == null)
            throw new NullPointerException(
                this.getClass().getName() 
                + ": Null value as an argument. Following arguments must not be null:"
                + " \nlistener = " + String.valueOf(il)
            );
        this.listener = il;
    }
    
    public abstract String fetch(String data);
    public abstract void push(String data);
}
