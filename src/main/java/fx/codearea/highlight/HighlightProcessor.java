package fx.codearea.highlight;

import javafx.util.Pair;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HighlightProcessor implements CodeHighlightProcessor, Initable {

    private Map<String, Pattern> patternMap = new HashMap<>();
    private Map<String, List<Pair<String, String>>> cssStylesMap = new HashMap<>();

    public HighlightProcessor() {
        init();
    }

    @Override
    public StyleSpans<Collection<String>> processHighlight(String text) {
        String ignoreCaseText = text.toLowerCase();
        StyleSpans<Collection<String>> result = null;
        for (Map.Entry<String, Pattern> pattern : patternMap.entrySet()) {
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
            Pair<Integer, String> pair = onGetStyleCSS(cssStylesMap.get(pattern), 0, matcher);
            String styleGroup = pair.getValue();
            onStyleGroup(styleGroup, matcher);
            String styleClass = getStyleFor(pattern).get(pair.getKey()).getValue();

            spansBuilder.add(Collections.emptyList(), matcher.start(styleGroup) - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end(styleGroup) - matcher.start(styleGroup));

            lastKwEnd = matcher.end(styleGroup);
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    @Override
    public CodeHighlightProcessor addCssStyleClass(String patternName, String regexpGroupName, String cssStyleName) {
        List<Pair<String, String>> styleList = cssStylesMap.containsKey(patternName) ? cssStylesMap.get(patternName) : new ArrayList<>();
        styleList.add(new Pair<>(regexpGroupName, cssStyleName));
        cssStylesMap.put(patternName, styleList);
        return this;
    }

    @Override
    public String computeCssStyle(String patternName, Matcher matcher) {
        Pair<Integer, String> pair = onGetStyleCSS(getStyleFor(patternName), 0, matcher);
        return getStyleFor(patternName).get(pair.getKey()).getValue();
    }

    @Override
    public List<Pair<String, String>> getStyleFor(String patternName) {
        return cssStylesMap.get(patternName);
    }

    @Override
    public CodeHighlightProcessor addPattern(String patternName, Pattern pattern) {
        patternMap.put(patternName, pattern);
        return this;
    }

    @Override
    public Pattern getPatternFor(String patternName) {
        return patternMap.get(patternName);
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

    protected void onStyleGroup(String styleGroup, Matcher matcher) {}

}
