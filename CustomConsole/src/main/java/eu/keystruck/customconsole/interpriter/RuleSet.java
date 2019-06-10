package eu.keystruck.customconsole.interpriter;

import java.util.HashMap;

public class RuleSet {
    public static enum Rule {
        COLOR("color"),//ForeGround
        BG("bg"),//BackGround
        FONT("font"),//Font Famility
        SIZE("size"),//Font Size
        WEIGHT("weight"),//Font Weight
        ITALIC("italic");//Posture
        private final String name;
        
        private Rule(String name) {
            this.name = name;
        }
        public String getName() { return this.name; }
        public static Rule getRule(String name) {
            if(name == null)
                return null;
            for(Rule e : Rule.values()) {
                if(e.getName().equals(name))
                    return e;
            }
            return null;
        }
    }  
    private static final HashMap<Rule,String[]> defRules = new HashMap();
    static {
        defRules.put(Rule.COLOR, null);
        defRules.put(Rule.BG, null);
        defRules.put(Rule.FONT, new String[] {"Default"});
        defRules.put(Rule.SIZE, new String[] {"12"});
        defRules.put(Rule.WEIGHT, new String[] {"1.0"});
        defRules.put(Rule.ITALIC, new String[] {"0.0"});
    }
    public static HashMap<Rule,String[]> getDefaults() {
        return new HashMap(defRules);
    }
    
    private final HashMap<Rule,String[]> rules;
    public RuleSet() {
        this.rules = RuleSet.getDefaults();
    }
    
    public RuleSet(RuleSet rules) {
        if(rules == null)
            this.rules = RuleSet.getDefaults();
        else this.rules = rules.getRuleList();
    } 
    
    public void setRule(Rule rule, String... args) {
        if(rule == null) return;
        this.rules.put(rule, args);
    }
    
    public String[] getRule(Rule rule) {
        if(rule == null) return null;
        return this.rules.get(rule);           
    }
    
    public HashMap<Rule,String[]> getRuleList() {
        return new HashMap(this.rules);
    }
}
