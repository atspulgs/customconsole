package eu.keystruck.customconsole.controllers;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.uis.UserInterface;

public interface InputListener {
    public void pull(UserInterface source, String data);
    public void registerCommand(Command cmd);
}
