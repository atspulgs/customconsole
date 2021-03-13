package eu.keystruck.customconsole.commands;

import eu.keystruck.customconsole.controllers.Controller;
import eu.keystruck.customconsole.interpriter.UChars;
import eu.keystruck.customconsole.uis.UserInterface;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//Make this more..... variable where ls that exists now is full ls -liah and the normal one is simpler.
//Format it more for the console too.

public class LsCommand extends Command {
    private static final String NAME = "Ls";
    private static final String COMMAND = "ls";
    private static final String DESCRIPTION = "Lists the contents of the directory.";
    private static final String SYNTAX = "<<color:255,120,120>><<weight:2.0>>ls [path]<</weight>><</color>>";
    private static final String NOTES = "";
    
    private final Controller co;
    
    public LsCommand(final Controller co) {
        super(COMMAND,NAME,DESCRIPTION,SYNTAX,NOTES);
        this.co = co;
    }

    @Override
    public void execute(UserInterface io, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("H T RWX  %1$-30s %2$-10s %3$-19s %4$-19s %5$-19s %6$s\n", "Owner", "Size", "Created", "Last Modified", "Last Accessed", "Name"));
        sb.append(String.valueOf(UChars.S_HORIZONTAL).repeat(120)).append("\n");
        if(args != null && args.length > 0 && !args[0].isEmpty() && !args[0].isBlank()) {
            io.push("Not Yet implemented!");
        } else {
            try {
                File[] children = this.co.getCd().toFile().listFiles();
                for(File child : children) {
                    BasicFileAttributes bfa = Files.readAttributes(child.toPath(), BasicFileAttributes.class);
                    /*try {
                        Set<PosixFilePermission> perms = Files.getPosixFilePermissions(child.toPath());
                        perms.forEach(p -> System.out.println(p));
                    } catch (UnsupportedOperationException uoe) {
                        
                    }*/
                    /*
                    N - Null/ Unknown/ Somehting gone wrong
                    O - Other
                    D - Directory
                    R - Regular File
                    S - Symlink
                    */
                    char type = 'N';
                    if(bfa.isDirectory()) type = 'D';
                    else if(bfa.isRegularFile()) type = 'R';
                    else if(bfa.isSymbolicLink()) type = 'S';
                    else if(bfa.isOther()) type = 'O';
                    
                    //Size stuff
                    /*
                     0 = Bytes
                     1 = KBytes
                     2 = MBytes
                     3 = GBytes
                     4 = TBytes
                    */
                    long size = bfa.size();
                    boolean go = true;
                    int granularity = 0;
                    String gran;
                    while(go) {
                        if(size / 1024 > 0) {
                            size /= 1024;
                            granularity++;
                        } else go = false;
                    }
                    switch(granularity) {
                        case 0: gran = " B"; break;
                        case 1: gran = " KB"; break;
                        case 2: gran = " MB"; break;
                        case 3: gran = " GB"; break;
                        case 4: gran = " TB"; break;
                        default: gran = " B";
                    }
                    
                    
                    sb.append(String.format("%1$1s %2$1s %3$1s%4$1s%5$1s %6$-30s %7$-10s %8$19s %9$19s %10$19s <<color:255,100,100>>%11$s<</color>>\n",
                        child.isHidden()?UChars.CHECK_MARK:UChars.BALLOT_X,
                        type, child.canRead()?UChars.CHECK_MARK:UChars.BALLOT_X,
                        child.canWrite()?UChars.CHECK_MARK:UChars.BALLOT_X,
                        child.canExecute()?UChars.CHECK_MARK:UChars.BALLOT_X,
                        Files.getOwner(child.toPath()).getName(),
                        size+gran, //still needs to be changed
                        ZonedDateTime.ofInstant(bfa.creationTime().toInstant(), ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        ZonedDateTime.ofInstant(bfa.lastModifiedTime().toInstant(), ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        ZonedDateTime.ofInstant(bfa.lastAccessTime().toInstant(), ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        child.getName()));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        io.push("<<font:Consolas>>"+sb.toString()+"<</font>>");
    }
}
