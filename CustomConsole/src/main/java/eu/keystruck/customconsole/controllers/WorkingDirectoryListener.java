package eu.keystruck.customconsole.controllers;

import java.nio.file.Path;

public interface WorkingDirectoryListener {
    public void updateWorkingDirectory(Path wd);
}
