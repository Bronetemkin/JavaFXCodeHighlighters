package fx.codearea.highlight;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HighlightPattern {

    private String patternName;
    private Pattern pattern;
    private List<Pair<String, String>> paramsMap;

    private HighlightPattern() {
        paramsMap = new ArrayList<>();
    }

    public static HighlightPattern name(String patternName) {
        HighlightPattern pattern = new HighlightPattern();
        pattern.setPatternName(patternName);
        return pattern;
    }

    public static HighlightPattern name(String patternName, Pattern pattern) {
        HighlightPattern result = name(patternName);
        result.setPattern(pattern);
        return result;
    }

    public HighlightPattern putParam(String regexName, String cssName) {
        paramsMap.add(new Pair<>(regexName, cssName));
        return this;
    }

    public List<Pair<String, String>> getParams() {
        return paramsMap;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
