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
}
