import fx.codearea.highlight.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
//        CodeArea codeArea = makeXMLArea(readXMLFile());
//        CodeArea codeArea = makeJavaCodeArea(readJavaFile());
        CodeArea codeArea = makeSQLArea(readSQLFile());

//        codeArea.setEditable(false);

        VirtualizedScrollPane<CodeArea> vsPane = new VirtualizedScrollPane<>(codeArea);

        vsPane.setPrefSize(800, 600);
        vsPane.autosize();
        stage.setScene(new Scene(vsPane));
        stage.show();
    }

    private String readJavaFile() {
        try {
            return readFile("C:\\Users\\Bronetemk1n\\Downloads\\ktproj\\src\\main\\java\\XMLHighlight.java");
        } catch (IOException e) {
            e.printStackTrace();
            return "Some error";
        }
    }

    private String readXMLFile() {
        try {
            return readFile("C:\\Users\\Bronetemk1n\\Downloads\\ktproj\\src\\main\\java\\XMLHighlight.xml");
        } catch (IOException e) {
            e.printStackTrace();
            return "Some error";
        }
    }

    private String readSQLFile() {
        try {
            return readFile("C:\\Users\\Bronetemk1n\\Downloads\\ktproj\\src\\main\\java\\some.sql");
        } catch (IOException e) {
            e.printStackTrace();
            return "Some error";
        }
    }

    private String readFile(String path) throws IOException {
        return String.join("\n", Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
    }

    private CodeArea makeJavaCodeArea(String srcCode) {
        JavaHighlight highlight = new JavaHighlight();
        CodeArea codeArea = initCodeArea(highlight);

        codeArea.replaceText(0, 0, srcCode);

        return codeArea;
    }

    private CodeArea makeXMLArea(String srcText) {
        CodeArea codeArea = initCodeArea(new XMLHighlight());

        codeArea.replaceText(0, 0, srcText);

        return codeArea;
    }

    private CodeArea makeSQLArea(String srcText) {
        CodeArea codeArea = initCodeArea(new SQLHighlight());

        codeArea.replaceText(0, 0, srcText);

        return codeArea;
    }

    private CodeArea initCodeArea(CodeHighlightProcessor codeHighlightProcessor) {
        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(1))
                .subscribe(ignore -> codeArea.setStyleSpans(0, codeHighlightProcessor.processHighlight(codeArea.getText())));
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE ->
        {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });
        codeArea.setPrefSize(800, 600);
        codeArea.getStylesheets().addAll(getClass().getResource("/default-highlight.css").toExternalForm());
        return codeArea;
    }

}
