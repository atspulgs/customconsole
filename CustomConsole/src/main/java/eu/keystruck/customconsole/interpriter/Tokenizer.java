package eu.keystruck.customconsole.interpriter;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class Tokenizer {
    private static class TokenMold {
        public final Pattern pattern;
        public final int code;
        
        public TokenMold(final String regex, final int code) {
            this.pattern = Pattern.compile("^("+regex+")");
            this.code = code;
        }
    }
    
    public static class Token {
        public static final int TEXT = 1;
        public static final int TAG_OPEN = 2;
        public static final int TAG_CLOSE = 3;
        
        public final String token;
        public final int code;
        
        public Token(final String token, final int code) {
            this.token = token;
            this.code = code;
        }
        
        @Override
        public String toString() {
            return this.code+":"+this.token;
        }
    }
    
    private final LinkedList<TokenMold> tokenModlds = new LinkedList();
    private final LinkedList<Token> tokens = new LinkedList();
    
    public Tokenizer() {
        this.tokenModlds.add(new TokenMold("<<[a-zA-Z0-9:,_ -]+?>>", 2)); //Tag OPEN
        this.tokenModlds.add(new TokenMold("<<[/a-zA-Z_ -]+?>>", 3)); //Tag CLOSE
        this.tokenModlds.add(new TokenMold(".+?(?=(<<[a-zA-Z0-9:,_/ -]+?>>)|$)", 1)); //Text (quite important that tags are matched first.)
    }
    
    public void tokenize(final String data) {
        var string = new String(data);
        this.tokens.clear();
        loop:while(!string.isBlank()) {
            System.out.println(string);
            for(var tm : this.tokenModlds) {
                var matcher = tm.pattern.matcher(string);
                if(matcher.find()) {
                    var t = matcher.group();
                    System.out.println("Code: "+tm.code+" T: "+t);
                    this.tokens.add(new Token(t, tm.code));
                    string = matcher.replaceFirst("");
                    continue loop;
                }
            }
            break;
        }
    }
    
    public LinkedList<Token> getTokens() {
        return new LinkedList(this.tokens);
    }
}
