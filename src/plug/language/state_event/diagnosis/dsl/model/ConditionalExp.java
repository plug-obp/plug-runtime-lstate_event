package plug.language.state_event.diagnosis.dsl.model;

public class ConditionalExp extends DiagnosisExp {
    DiagnosisExp condition;
    DiagnosisExp trueBranch;
    DiagnosisExp falseBranch;

    public ConditionalExp(DiagnosisExp condition, DiagnosisExp trueBranch, DiagnosisExp falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    @Override
    public Type getType() {
        if (type == null || type == Type.UNKNOWN) {
            Type condT = condition.getType();
            Type trueT = trueBranch.getType();
            Type falseT = falseBranch.getType();

            if (condT != Type.BOOLEAN) {
                throw new RuntimeException("the condition should be boolean");
            }
            if (trueT != falseT) {
                throw new RuntimeException("both conditional branches should have the same type");
            }
            return type = trueT;
        }
        return type;
    }
}
