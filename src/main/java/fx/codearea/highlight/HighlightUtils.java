package fx.codearea.highlight;

import org.fxmisc.richtext.model.StyleSpans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HighlightUtils {

    public static StyleSpans<Collection<String>> mergeHighlights(StyleSpans<Collection<String>> highlightedSpans, StyleSpans<Collection<String>> markedSpans) {
        return highlightedSpans.overlay(
                markedSpans,
                (originalList, addedList) -> {
                    if (addedList.isEmpty()) {
                        // no need to create a new list if it just stores the original list's contents
                        return originalList;
                    }

                    List<String> l = new ArrayList<>(originalList.size() + addedList.size());
                    l.addAll(originalList);
                    l.addAll(addedList);
                    return l;
                }
        );
    }

}
