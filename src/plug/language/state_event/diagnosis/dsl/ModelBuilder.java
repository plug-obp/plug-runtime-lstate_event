package plug.language.state_event.diagnosis.dsl;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import plug.language.state_event.diagnosis.dsl.grammar.SEDiagBaseListener;
import plug.language.state_event.diagnosis.dsl.grammar.SEDiagParser;
import plug.language.state_event.diagnosis.dsl.model.*;
import plug.utils.parsing.TreeAnnotator;

public class ModelBuilder extends SEDiagBaseListener implements TreeAnnotator {
    public ParseTreeProperty<Object> values = new ParseTreeProperty<>();
    @Override
    public ParseTreeProperty<Object> getValues() {
        return values;
    };

    @Override
    public void exitValueExp(SEDiagParser.ValueExpContext ctx) {
        setValue(ctx, new ValueExp(Integer.parseInt(ctx.NUMBER().getText())));
    }

    @Override
    public void exitClockExp(SEDiagParser.ClockExpContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        identifier = identifier.substring(1, identifier.length()-1);

        setValue(ctx, new ClockRef(identifier));
    }

    @Override
    public void exitTransitionExp(SEDiagParser.TransitionExpContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        identifier = identifier.substring(1, identifier.length()-1);
        int index = -1;
        try {
            index = Integer.parseInt(identifier);
        } catch (NumberFormatException e) {
            throw new RuntimeException("t[number] expected but found " + ctx.getText());
        }
        setValue(ctx, new TransitionRef(index));
    }

    @Override
    public void exitVariableExp(SEDiagParser.VariableExpContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        identifier = identifier.substring(1, identifier.length()-1);

        VariableRef variableRef = new VariableRef(identifier);
        if (ctx.NEXT() != null) {
            setValue(ctx, new NextExp(variableRef));
            return;
        }
        setValue(ctx, new VariableRef(identifier));
    }

    @Override
    public void exitParenExp(SEDiagParser.ParenExpContext ctx) {
        setValue(ctx, getValue(ctx.expression()));
    }

    @Override
    public void exitUnaryExp(SEDiagParser.UnaryExpContext ctx) {
        UnaryExp.Operator operator = null;
        switch (ctx.operator.getType()) {
            case SEDiagParser.NOT:
                operator = UnaryExp.Operator.NOT;
                break;
            case SEDiagParser.MINUS:
                operator = UnaryExp.Operator.MINUS;
                break;
            default: //nothing
        }

        DiagnosisExp operand = getValue(ctx.expression(), DiagnosisExp.class);

        setValue(ctx, new UnaryExp(operator, operand));
    }

    @Override
    public void exitBinaryExp(SEDiagParser.BinaryExpContext ctx) {
        BinaryExp.Operator operator = null;
        switch (ctx.operator.getType()) {
            case SEDiagParser.MULT:
                operator = BinaryExp.Operator.MULT;
                break;
            case SEDiagParser.MOD:
                operator = BinaryExp.Operator.MOD;
                break;
            case SEDiagParser.PLUS:
                operator = BinaryExp.Operator.PLUS;
                break;
            case SEDiagParser.MINUS:
                operator = BinaryExp.Operator.MINUS;
                break;
            case SEDiagParser.LT:
                operator = BinaryExp.Operator.LT;
                break;
            case SEDiagParser.LTE:
                operator = BinaryExp.Operator.LTE;
                break;
            case SEDiagParser.GT:
                operator = BinaryExp.Operator.GT;
                break;
            case SEDiagParser.GTE:
                operator = BinaryExp.Operator.GTE;
                break;
            case SEDiagParser.EQ:
                operator = BinaryExp.Operator.EQ;
                break;
            case SEDiagParser.NEQ:
                operator = BinaryExp.Operator.NEQ;
                break;
            default: //nothing
        }

        DiagnosisExp lhs = getValue(ctx.expression().get(0), DiagnosisExp.class);
        DiagnosisExp rhs = getValue(ctx.expression().get(1), DiagnosisExp.class);

        setValue(ctx, new BinaryExp(lhs, operator, rhs));
    }

    @Override
    public void exitConditionalExp(SEDiagParser.ConditionalExpContext ctx) {
        DiagnosisExp condition = getValue(ctx.expression(0), DiagnosisExp.class);
        DiagnosisExp trueBranch = getValue(ctx.expression(1), DiagnosisExp.class);
        DiagnosisExp falseBranch = getValue(ctx.expression(2), DiagnosisExp.class);

        setValue(ctx, new ConditionalExp(condition, trueBranch, falseBranch));
    }
}
