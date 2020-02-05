package fx.codearea.highlight;

import javafx.util.Pair;
import org.fxmisc.richtext.model.StyleSpans;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CodeHighlightProcessor {

    StyleSpans<Collection<String>> processHighlight(String text, Collection<StyleSpans<Collection<String>>> styleSpansBuilders);

    StyleSpans<Collection<String>> processHighlight(String patternName, String text);

    StyleSpans<Collection<String>> processHighlight(String text);

    CodeHighlightProcessor addCssStyleClass(String patternName, String regexpGroupName, String cssStyleName);

    String computeCssStyle(String patternName, Matcher matcher);

    CodeHighlightProcessor addPattern(String patternName, Pattern pattern);

    CodeHighlightProcessor removePattern(String patternName);

    Pattern getPatternFor(String patternName);

    List<Pair<String, String>> getStyleFor(String patternName);

    StyleSpans<Collection<String>> mergeHighlights(StyleSpans<Collection<String>> arg0, StyleSpans<Collection<String>> arg1);

}
