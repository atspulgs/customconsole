package eu.keystruck.customconsole.interpriter;

import java.util.HashMap;

public class FormattedText {
    public final String text;
    private final RuleSet rules;
    public FormattedText(final String text, RuleSet rules) {
        this.text = text;
        this.rules = new RuleSet(rules);
    }
    
    public HashMap<RuleSet.Rule,String[]> getRuleList() {
        return this.rules.getRuleList();
    }
    
    public String[] getRule(RuleSet.Rule rule) {
        if(rule == null) return null;
        return this.rules.getRuleList().get(rule);           
    }
}
