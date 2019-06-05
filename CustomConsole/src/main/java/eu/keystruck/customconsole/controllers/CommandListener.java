package eu.keystruck.customconsole.controllers;

interface CommandListener {
    public void notifyHolder(final Thread child);
}
