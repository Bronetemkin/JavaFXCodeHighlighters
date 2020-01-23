package fx.codearea.highlight;

import java.util.regex.Pattern;

public class XMLHighlight extends HighlightProcessor {

    public static final String ELEMENT = "(</?\\h*)(\\w+)([^<>]*)(\\h*/?>)";
    public static final String COMMENT = "<!--[^<>]+-->";
    public static final String STRING = "('.*'|\".*\")";
    private static final String ATTRIBUTES = "(\\w+\\h*)(=)(\\h*[\"][^\"]*[\"]|\\h*['][^\"]*['])";

    public static final Pattern PATTERN = Pattern.compile("(?<ELEMENT>" + ELEMENT + ")"
            + "|(?<COMMENT>" + COMMENT + ")"
            + "|(?<STRING>" + STRING + ")"
            + "|(?<ATTRIBUTES>" + ATTRIBUTES + ")"
    );

    @Override
    public void init() {
        addPattern("main", PATTERN);

        addCssStyleClass("main", "ELEMENT", "element");
        addCssStyleClass("main", "COMMENT", "comment");
        addCssStyleClass("main", "STRING", "string");
        addCssStyleClass("main", "ATTRIBUTES", "xml-attribute");
    }

}
