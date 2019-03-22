package plug.language.state_event.diagnosis.dsl;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import plug.language.state_event.diagnosis.dsl.grammar.SEDiagLexer;
import plug.language.state_event.diagnosis.dsl.grammar.SEDiagParser;
import plug.language.state_event.diagnosis.dsl.model.*;

import java.text.ParseException;
import java.util.function.Consumer;

public class SEDiagnosisParser {
    private static SEDiagnosisParser instance = new SEDiagnosisParser();

    public DiagnosisExp parse(String expression, Consumer<Exception> errorHandler) {
        ANTLRInputStream is = new ANTLRInputStream(expression);
        return instance.parse(is, DiagnosisExp.class, errorHandler);
    }

    public <T extends DiagnosisExp> T parse(ANTLRInputStream is, Class<T> type) {
        try {
            return parse(is, type, (e) -> {});
        } catch (Error e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends DiagnosisExp> T parse(ANTLRInputStream is, Class<T> type, Consumer<Exception> errorHandler) {
        SEDiagLexer lexer = new SEDiagLexer(is);
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errorHandler.accept(new ParseException(msg, charPositionInLine));
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SEDiagParser parser = new SEDiagParser(tokens);
        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void reportError(org.antlr.v4.runtime.Parser recognizer, RecognitionException e) {
                errorHandler.accept(e);
            }
        });

        ParseTree tree = parser.expression();
        ModelBuilder builder = new ModelBuilder();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(builder, tree);

        T result = builder.getValue(tree, type);
        if (result.getType() != Type.BOOLEAN) {
            throw new RuntimeException("Diagnosis expression should have a boolean return type");
        }
        return result;
    }
}
