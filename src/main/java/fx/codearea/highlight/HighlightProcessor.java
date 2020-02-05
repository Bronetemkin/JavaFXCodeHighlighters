package fx.codearea.highlight;

import javafx.util.Pair;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HighlightProcessor implements CodeHighlightProcessor, Initable {

    public static final String PATTERN_DOUBLE_QUOTED_STRING = "\"[^\"]*\"";
    public static final String PATTERN_QUOTED_STRING = "'[^']*'";

    private Map<String, HighlightPattern> patternMap = new HashMap<>();

    public HighlightProcessor() {
        init();
    }

    @Override
    public StyleSpans<Collection<String>> processHighlight(String text, Collection<StyleSpans<Collection<String>>> styleSpans) {
        StyleSpans<Collection<String>> result = processHighlight(text);
        for(StyleSpans<Collection<String>> spans : styleSpans) {
            result = mergeHighlights(result, spans);
        }
        return result;
    }

    @Override
    public StyleSpans<Collection<String>> processHighlight(String text) {
        String ignoreCaseText = text.toLowerCase();
        StyleSpans<Collection<String>> result = null;
        for (Map.Entry<String, HighlightPattern> pattern : patternMap.entrySet()) {
            StyleSpans<Collection<String>> cur = processHighlight(pattern.getKey(), ignoreCaseText);
            if (result == null) result = cur;
            else result = mergeHighlights(result, cur);
        }
        return result;
    }

    @Override
    public StyleSpans<Collection<String>> processHighlight(String pattern, String text) {
        Pattern pattern1 = getPatternFor(pattern);
        Matcher matcher = pattern1.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            Pair<Integer, String> pair = onGetStyleCSS(patternMap.get(pattern).getParams(), 0, matcher);
            String styleGroup = pair.getValue();
            String styleClass = getStyleFor(pattern).get(pair.getKey()).getValue();

            spansBuilder.add(Collections.emptyList(), matcher.start(styleGroup) - lastKwEnd);

            onAddSpansToText(spansBuilder, styleGroup, styleClass, matcher);

            lastKwEnd = matcher.end(styleGroup);
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void onAddSpansToText(StyleSpansBuilder<Collection<String>> spansBuilder, String styleGroupRegex, String styleClassCss, Matcher matcher) {
        spansBuilder.add(Collections.singleton(styleClassCss), matcher.end(styleGroupRegex) - matcher.start(styleGroupRegex));
    }

    public HighlightProcessor addHighlightPattern(HighlightPattern pattern) {
        patternMap.put(pattern.getPatternName(), pattern);
        return this;
    }

    @Override
    public CodeHighlightProcessor addCssStyleClass(String patternName, String regexpGroupName, String cssStyleName) {
        patternMap.get(patternName).putParam(regexpGroupName, cssStyleName);
        return this;
    }

    @Override
    public String computeCssStyle(String patternName, Matcher matcher) {
        Pair<Integer, String> pair = onGetStyleCSS(getStyleFor(patternName), 0, matcher);
        return getStyleFor(patternName).get(pair.getKey()).getValue();
    }

    @Override
    public List<Pair<String, String>> getStyleFor(String patternName) {
        return patternMap.get(patternName).getParams();
    }

    @Override
    public CodeHighlightProcessor addPattern(String patternName, Pattern pattern) {
        patternMap.put(patternName, HighlightPattern.name(patternName, pattern));
        return this;
    }

    @Override
    public Pattern getPatternFor(String patternName) {
        return patternMap.get(patternName).getPattern();
    }

    @Override
    public StyleSpans<Collection<String>> mergeHighlights(StyleSpans<Collection<String>> arg0, StyleSpans<Collection<String>> arg1) {
        return onMergeHighlights(arg0, arg1);
    }

    @Override
    public CodeHighlightProcessor removePattern(String patternName) {
        patternMap.remove(patternName);
        return this;
    }

    protected StyleSpans<Collection<String>> onMergeHighlights(StyleSpans<Collection<String>> highlightedSpans, StyleSpans<Collection<String>> markedSpans) {
        return HighlightUtils.mergeHighlights(highlightedSpans, markedSpans);
    }

    protected Pair<Integer, String> onGetStyleCSS(List<Pair<String, String>> cssStyles, int cssStyleIndex, Matcher matcher) {
        if (cssStyleIndex > cssStyles.size()) return null;
        return matcher.group(cssStyles.get(cssStyleIndex).getKey()) != null ? new Pair<>(cssStyleIndex, cssStyles.get(cssStyleIndex).getKey()) : onGetStyleCSS(cssStyles, cssStyleIndex + 1, matcher);
    }

}
