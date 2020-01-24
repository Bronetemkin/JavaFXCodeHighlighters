package fx.codearea.highlight;

import java.util.regex.Pattern;

public class SQLHighlight extends HighlightProcessor {

    public static final String DIGITS_PATTERN = "\\b(\\d+)\\b";
    public static final String STRING_PATTERN = "\".*?\"|'.*?'";
    public static final String COMMENT_PATTERN = "--.*\n";
    public static final String DYNAMIC_PARAMS_PATTERN = "\\{\\d+}";
    public static final String[] KEYWORDS = new String[]{
            "add", "constraint", "alter", "alter", "column", "all", "and", "any", "as",
            "asc", "backup", "between", "case", "check", "column", "constraint", "create",
            "create", "create index", "create", "or", "replace", "view", "table", "create", "procedure",
            "create", "unique", "index", "create", "view", "database", "default", "delete", "desc", "distinct", "drop",
            "drop", "column", "drop ", "drop", "database", "drop", "default", "drop", "index", "drop", "view",
            "exec", "exists", "foreign", "key", "from", "full", "outer", "group", "by", "having", "in", "index", "inner",
            "insert", "is", "null", "is", "null", "join", "left", "like", "limit", "not",
            "or", "order", "outer", "primary", "right", "rownum", "select",
            "set", "table", "top", "truncate", "union", "execute", "immediate",
            "union all", "unique", "update", "values", "view", "where", "begin", "declare", "end", "into", "to", "rename",
            "exception", "when", "others", "then", "sqlcode", "sqlerrm"
    };
    public static final String[] SIGNS = new String[] {
            "\\,", "\\;"
    };
    public static final String METHOD = "(\\h*|\\h*\\.)(?<METHOD>\\w+)\\(";
    public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public static final Pattern MAIN_PATTERN = Pattern.compile(
            "(?<ELEMENT>" + KEYWORD_PATTERN + ")"
                    + "|(?<DIGITS>" + DIGITS_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|" + METHOD
                    + "|(?<SIGNS>(" + String.join("|", SIGNS) + "))"
    );
    public static final Pattern DPARAMS_PATTERN = Pattern.compile("(?<DPARAM>" + DYNAMIC_PARAMS_PATTERN + ")");

    @Override
    public void init() {
        addPattern("main", MAIN_PATTERN);
        addPattern("additional", DPARAMS_PATTERN);
        addPattern("error", Pattern.compile("(?<ERROR>to_char)"));

        addCssStyleClass("main", "ELEMENT", "element");
        addCssStyleClass("main", "SIGNS", "element");
        addCssStyleClass("main", "DIGITS", "digit");
        addCssStyleClass("main", "STRING", "string");
        addCssStyleClass("main", "COMMENT", "comment");
        addCssStyleClass("main", "METHOD", "method");

        addCssStyleClass("additional", "DPARAM", "dparam");

        addCssStyleClass("error", "ERROR", "error");
    }

}

