package plug.language.state_event.diagnosis.dsl;

public class ConditionalExp extends DiagnosisExp {
    DiagnosisExp condition;
    DiagnosisExp trueBranch;
    DiagnosisExp falseBranch;
}
