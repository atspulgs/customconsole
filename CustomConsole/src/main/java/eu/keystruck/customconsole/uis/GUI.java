package eu.keystruck.customconsole.uis;

import eu.keystruck.customconsole.commands.Command;
import eu.keystruck.customconsole.controllers.InputListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class GUI extends UserInterface implements ActionListener, KeyListener {
    public static final AttributeSet LINE_NUMBER = GUI.genAS(Color.GRAY, "Lucida Console");
    public static final AttributeSet TIMESTAMP = GUI.genAS(new Color(255,100,100), "Lucida Console");
    public static final AttributeSet REGULAR = GUI.genAS(Color.LIGHT_GRAY);
    
    private static final int MIN_FRAME_WIDTH = 800;
    private static final int MIN_FRAME_HEIGHT = 600;
    private static final Dimension MIN_FRAME_DIMENSION = new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT);
    
    private final JFrame frame;
    private final JPanel wrapper;
    private final JTextPane output;
    private final JScrollPane outputScroll;
    private final JTextField input;
    private final JButton submit;
    
    private int lineNumber = 0;
    private final ArrayList<String> history = new ArrayList();
    private int historyIndex = -1;
    
    private boolean waiting = false;
    
    public GUI(InputListener il) {
        super(il);
        this.listener.registerCommand(new Command("exit", "Exits the UI!") {
            public void execute(UserInterface io, String... args) {
                if(io == null)
                    throw new NullPointerException(
                        this.getClass().getName() 
                        + ": Null value as an argument. Following arguments must not be null:"
                        + " \nio = " + String.valueOf(io)
                    );
                if(io instanceof GUI) {
                    ((GUI)io).exit();
                    io.push("Exiting...");
                } else io.push("This command can only run on CLI Object");
            }
        });
        this.frame = new JFrame();
        this.wrapper = new JPanel();
        this.output = new JTextPane();
        this.outputScroll = new JScrollPane(this.output);
        this.input = new JTextField();
        this.submit = new JButton("Commit");
        uiSetup();
    }
    
    private void uiSetup() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setMinimumSize(MIN_FRAME_DIMENSION);
        this.wrapper.setLayout(new BorderLayout());
        EmptyBorder border = new EmptyBorder(new Insets(5,5,5,5));
        this.output.setBorder(border);
        this.output.setEditable(false);
        this.output.setBackground(Color.DARK_GRAY);
        this.input.addKeyListener(this);
        this.frame.getRootPane().setDefaultButton(this.submit);
        this.submit.addActionListener(this);
        this.outputScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.wrapper.add(this.outputScroll, BorderLayout.CENTER);
        JPanel bottomWrap = new JPanel();
        bottomWrap.setLayout(new BorderLayout());
        bottomWrap.add(this.input, BorderLayout.CENTER);
        bottomWrap.add(this.submit, BorderLayout.EAST);
        this.wrapper.add(bottomWrap, BorderLayout.SOUTH);
        this.frame.setContentPane(this.wrapper);
        this.frame.setLocationRelativeTo(null);
    }
    
    private static AttributeSet genAS(Color c) {
        return genAS(c, "Courier New");
    }
    
    private static AttributeSet genAS(Color c, String fontFamily) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet temp = SimpleAttributeSet.EMPTY;
        temp = sc.addAttribute(temp, StyleConstants.Foreground, c);
        temp = sc.addAttribute(temp, StyleConstants.FontFamily, fontFamily);
        temp = sc.addAttribute(temp, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
        return temp;
    }
    
    private void addTextToOut(String text, AttributeSet as) {
        this.output.setEditable(true);
        this.output.setCaretPosition(this.output.getDocument().getLength());
        this.output.setCharacterAttributes(as, true);
        this.output.replaceSelection(text);
        this.output.setEditable(false);
    }
    
    public void exit() {
        this.frame.setVisible(false);
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }
    
    @Override
    public synchronized String fetch(String data) {
        this.addTextToOut(data+" -> ", REGULAR);
        this.waiting = true;
        while(this.waiting) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted!!!!");
            }
            this.waiting = false;
        }
        var r = this.input.getText();
        this.input.setText("");
        this.addTextToOut(r+"\n", REGULAR);
        return r;
    }

    @Override
    public synchronized void push(String data) {
        this.addTextToOut(data+"\n", REGULAR);
    }

    @Override
    public void run() {
        this.frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.submit) {
            if(this.waiting) {
                //I dont think this needs a synchronized context.
                //At least not in this implementation, I may make it possible for threads to be more recognizable.
                this.notifyAll();
            } else {
                String text = this.input.getText().replace("\\n", "\n");
                String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS O"));
                String lineNumberString = String.format("%06d",(++lineNumber));
                this.addTextToOut(lineNumberString+" | ", LINE_NUMBER);
                this.addTextToOut(timestamp+" | ", TIMESTAMP);
                this.addTextToOut(text+"\n", REGULAR);
                this.history.add(this.input.getText());
                this.historyIndex = -1;
                this.input.setText("");
                if(text != null && !text.isEmpty()) {
                    this.listener.pull(this, text);
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        /* -
         * This is cycling through hisotry when pressing up or down arrows
         * The idea is that the console remembers all the comands provided by the user and can recall them.
         */
        if(key == 38) { //Up
            if(this.history == null || this.history.isEmpty())
                return;
            
            if(this.historyIndex > 0 && this.historyIndex <= this.history.size())
                this.historyIndex --;
            else if(this.historyIndex <= -1)
                this.historyIndex = this.history.size() -1;
            else return;
            
            if(this.historyIndex >= 0 && this.historyIndex < this.history.size())
                this.input.setText(this.history.get(this.historyIndex));
        } else if(key == 40) { //Down
            if(this.history == null || this.history.isEmpty())
                return;
            
            if(this.historyIndex >= 0 && this.historyIndex < this.history.size() - 1)
                this.historyIndex++;
            else if(this.historyIndex >= this.history.size() -1)
                this.historyIndex = -1;
            else return;
            
            if(this.historyIndex >= 0 && this.historyIndex < this.history.size())
                this.input.setText(this.history.get(this.historyIndex));
            else if(this.historyIndex <= -1)
                this.input.setText("");
        }
    }
}
