package plug.language.state_event.diagnosis.dsl.model;

public class NextExp extends DiagnosisExp {
    VariableRef variableRef;

    public NextExp(VariableRef variableRef) {
        this.variableRef = variableRef;
    }
}
