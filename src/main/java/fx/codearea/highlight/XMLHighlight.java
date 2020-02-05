package fx.codearea.highlight;

import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLHighlight extends HighlightProcessor {

    public static final String ELEMENT = "(</?\\h*)(\\w+)([^<>]*)(\\h*/?>)";
    public static final String COMMENT = "<!--[^<>]+-->";
    public static final String STRING = "("+ PATTERN_QUOTED_STRING +"|" + PATTERN_DOUBLE_QUOTED_STRING + ")";
    public static final String ATTRIBUTES = "\\w+\\h*=\\h*(\"[^\"']*\"|\\h*'[^\"']*')";

    public static final Pattern PATTERN = Pattern.compile("(?<ELEMENT>" + ELEMENT + ")"
            + "|(?<COMMENT>" + COMMENT + ")"
            + "|(?<STRING>" + STRING + ")"
    );
    public static final Pattern ADDITIONAL_PATTERN = Pattern.compile("(?<ATTRIBUTES>" + ATTRIBUTES + ")");

    @Override
    public void init() {
        addHighlightPattern(HighlightPattern.name("additional", ADDITIONAL_PATTERN)
                .putParam("ATTRIBUTES", "attribute"));
        addHighlightPattern(HighlightPattern.name("main", PATTERN)
                .putParam("ELEMENT", "element")
                .putParam("COMMENT", "comment")
                .putParam("STRING", "string")
        );
    }

    @Override
    public void onAddSpansToText(StyleSpansBuilder<Collection<String>> spansBuilder, String styleGroupRegex, String styleClassCss, Matcher matcher) {
        if(styleGroupRegex.equals("ATTRIBUTES")) {
            int beforeString = matcher.group(1).indexOf(matcher.group(2));
            spansBuilder.add(Collections.singletonList(styleClassCss), beforeString);
            spansBuilder.add(Collections.singletonList("string"), matcher.group(2).length());
        } else {
            super.onAddSpansToText(spansBuilder, styleGroupRegex, styleClassCss, matcher);
        }
    }
}
