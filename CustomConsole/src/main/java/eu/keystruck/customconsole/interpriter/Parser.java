package eu.keystruck.customconsole.interpriter;

import eu.keystruck.customconsole.interpriter.Tokenizer.Token;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Parser {
    public static class Node {
        public final boolean leaf;
        protected final LinkedList<Node> children;
        public final Node parent;
        private Node(final boolean leaf, Node parent) {
            this.leaf = leaf;
            this.children = this.leaf? null: new LinkedList();
            this.parent = parent;
        }
        
        public boolean isRoot() {
            return this.parent == null;
        }
        
        public void addChild(Node child) {
            if(this.children == null) {
                return;
            }
            this.children.add(child);
        }
        
        @Override
        public String toString() {
            var indent = "";
            var par = this.parent;
            while(par != null) {
                indent += ". ";
                par = par.parent;
            }
            var ret = indent+this.getClass().getSimpleName();
            ret += " - Children:";
            if(!this.leaf) for(var n : this.children)
                ret += "\n"+n.toString();
            return ret;
        }
    }
    
    public static class TagNode extends Node {
        public final String name;
        public final String[] args;
        public final RuleSet rules;
        public TagNode(Node parent, String name, RuleSet rules, String... args) {
            super(false, parent);
            this.name = name;
            this.rules = rules;
            this.args = args;
        }
        
        @Override
        public String toString() {
            var indent = "";
            var par = this.parent;
            while(par != null) {
                indent += ". ";
                par = par.parent;
            }
            var ret = indent+this.getClass().getSimpleName();
            ret += " - Attributes: name = " +this.name+ " | args = " + Arrays.toString(this.args);
            ret += " - Children:";
            if(!this.leaf) for(var n : this.children)
                ret += "\n"+n.toString();
            return ret;
        }
    }
    
    public static class TextNode extends Node {
        public final String text;
        public TextNode(Node parent, String text) {
            super(true,parent);
            this.text = text;
        }
        
        @Override
        public String toString() {
            var indent = "";
            var par = this.parent;
            while(par != null) {
                indent += ". ";
                par = par.parent;
            }
            var ret = indent+this.getClass().getSimpleName();
            ret += " - Attributes: text = " +this.text;
            ret += " - Children:";
            if(!this.leaf) for(var n : this.children)
                ret += "\n"+n.toString();
            return ret;
        }
    }
    
    private static final Pattern TAG_OPEN_PATTERN = Pattern.compile("<<(?<name>[a-zA-Z0-9_-]+?)(:(?<args>[a-zA-Z0-9,_.-]*))?>>");
    private static final Pattern TAG_CLOSE_PATTERN = Pattern.compile("<</(?<name>[a-zA-Z0-9_-]+?)>>");
    
    private LinkedList<Token> stack = null;
    private Token pointer = null;
    private final Node root = new Node(false, null);
    private Node current = root;
    
    private void nextToken() {
        this.stack.pop();
        if(this.stack.isEmpty())
            this.pointer = null;
        else this.pointer = this.stack.getFirst();
    }
    
    public void parse(final LinkedList<Token> tokens) {
        this.stack = new LinkedList(tokens);
        this.pointer = this.stack.getFirst();
        
        this.evaluate();
        
    }

    private void evaluate() {
        System.out.println("Pointer: "+this.pointer);
        if(this.pointer == null)
            return;
        switch (this.pointer.code) {
            case Token.TEXT:
                this.current.addChild(new TextNode(this.current, this.pointer.token));
                break;
            case Token.TAG_OPEN:
                var om = TAG_OPEN_PATTERN.matcher(this.pointer.token);
                if(om.matches()) {
                    System.out.println("\tName: "+om.group("name") +"\n\tArgs: "+String.valueOf(om.group("args")));
                    var r = RuleSet.Rule.getRule(om.group("name"));
                    var ruleSet = new RuleSet();
                    if(this.current instanceof TagNode) {
                        ruleSet = ((TagNode)this.current).rules;
                    }
                    if(om.group("name") != null && r != null ) {
                        ruleSet.setRule(r, om.group("args") == null?null: om.group("args").split(","));
                    }
                    var on = new TagNode(this.current, om.group("name"), ruleSet, om.group("args") == null?null: om.group("args").split(","));
                    this.current.addChild(on);
                    this.current = on;
                }
                break;
            case Token.TAG_CLOSE:
                var cm = TAG_CLOSE_PATTERN.matcher(this.pointer.token);
                if(cm.matches()) {
                    System.out.println("\tName: "+cm.group("name"));
                    var safekeep = this.current;
                    var found = false;
                    
                    while(!found && !this.current.isRoot()) {
                        var temp = this.current;
                        this.current = this.current.parent;
                        if(temp instanceof TagNode && ((TagNode)temp).name.equals(cm.group("name"))) {
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        this.current = safekeep;                    
                }
                break;
        }
        this.nextToken();
        this.evaluate();
    }
    
    public Node getRoot() {
        return this.root;
    }
    
    private final LinkedList<FormattedText> list = new LinkedList();
    public LinkedList<FormattedText> construct() {
        this.list.clear();
        trav(this.root);
        return new LinkedList<>(this.list);
    }
    
    private void trav(Node n) {
        if(n == null || n.children == null) return;
        LinkedList<Node> childCopy = n.children;
        childCopy.forEach((node) -> {
            if(node.leaf) {
                TextNode tn = (TextNode) node;
                RuleSet rs = tn.parent.isRoot()? new RuleSet(): ((TagNode) tn.parent).rules;
                list.add(new FormattedText(tn.text, rs));
            } else {
                trav(node);
            }
        });
    }
}
