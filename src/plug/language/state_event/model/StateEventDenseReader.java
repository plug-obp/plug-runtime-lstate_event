package plug.language.state_event.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Ciprian TEODOROV on 14/04/17.
 */
public class StateEventDenseReader {
    BufferedReader reader;

    public StateEventModel read(File inFile) throws IOException {
        StateEventModel sem = new StateEventModel();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            readModel(sem);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sem;
    }

    void readModel(StateEventModel ioSEM) throws IOException {
        readDimensions(ioSEM);
        readEventNames(ioSEM);
        readVariableNames(ioSEM);
        readConfigurations(ioSEM);
        readInitialConfiguration(ioSEM);
        readTransitions(ioSEM);
    }

    void readDimensions(StateEventModel ioSEM) throws IOException {
        StringTokenizer sT = new StringTokenizer(reader.readLine());

        if (sT.countTokens() != 4) {
            throw new Error("expected nbConfigurations nbTransitions nbClocks nbConfigurationElements");
        }

        ioSEM.nbConfigurations = Integer.parseInt(sT.nextToken());
        ioSEM.nbTransitions = Integer.parseInt(sT.nextToken());
        ioSEM.nbEvents = Integer.parseInt(sT.nextToken());
        ioSEM.nbVariables = Integer.parseInt(sT.nextToken());
    }

    void readEventNames(StateEventModel ioSEM) throws IOException {
        if (!reader.readLine().startsWith("- clock-names")) {
            throw new Error("expected '- clock-names'");
        }
        StringTokenizer sT = new StringTokenizer(reader.readLine());

        if (sT.countTokens() != ioSEM.nbEvents) {
            throw new Error("expected " + ioSEM.nbEvents + " but found " + sT.countTokens() + " event names");
        }

        ioSEM.events = new HashMap<>(ioSEM.nbEvents);
        for (int i = 0; i<ioSEM.nbEvents; i++) {
            ioSEM.events.put(sT.nextToken(), i);
        }
    }
    void readVariableNames(StateEventModel ioSEM) throws IOException {
        if (!reader.readLine().startsWith("- variable-names")) {
            throw new Error("expected '- variable-names'");
        }
        StringTokenizer sT = new StringTokenizer(reader.readLine());

        if (sT.countTokens() != ioSEM.nbVariables) {
            throw new Error("expected " + ioSEM.nbVariables + " but found " + sT.countTokens() + " variable names");
        }

        ioSEM.variables = new HashMap<>(ioSEM.nbVariables);
        for (int i = 0; i<ioSEM.nbVariables; i++) {
            ioSEM.variables.put(sT.nextToken(), i+1);
        }
    }
    Map<Integer, Integer> configurationID_to_tableID = new HashMap<>();
    void readConfigurations(StateEventModel ioSEM) throws IOException {
        if (!reader.readLine().startsWith("- configurations")) {
            throw new Error("expected '- configurations'");
        }
        ioSEM.configurations = new int[ioSEM.nbConfigurations][];
        for (int i=0; i<ioSEM.nbConfigurations; i++) {
            readConfiguration(i, ioSEM);
        }
    }

    void readConfiguration(int inID, StateEventModel ioSEM) throws IOException {
        StringTokenizer sT = new StringTokenizer(reader.readLine());
        ioSEM.configurations[inID] = new int[sT.countTokens()];

        for (int i=0; i < ioSEM.configurations[inID].length; i++) {
            ioSEM.configurations[inID][i] = Integer.parseInt(sT.nextToken());
            if (i==0) {
                configurationID_to_tableID.put(ioSEM.configurations[inID][i], inID);
            }
        }
    }

    void readInitialConfiguration(StateEventModel ioSEM) throws IOException {
        if (!reader.readLine().startsWith("- initial")) {
            throw new Error("expected '- initial'");
        }

        StringTokenizer sT = new StringTokenizer(reader.readLine());

        if (sT.countTokens() != 1) {
            throw new Error("expected 1 initial configuration");
        }

        ioSEM.initialConfiguration = configurationID_to_tableID.get(Integer.parseInt(sT.nextToken()));
    }

    void readTransitions(StateEventModel ioSEM) throws IOException {
        if (!reader.readLine().startsWith("- transitions")) {
            throw new Error("expected '- transitions'");
        }
        ioSEM.fanout = new HashMap<>(ioSEM.nbTransitions);
        for (int i = 0; i < ioSEM.nbTransitions; i++) {
            readTransition(i, ioSEM);
        }
    }

    void readTransition(int inTransitionID, StateEventModel ioSEM) throws IOException {
        StringTokenizer sT = new StringTokenizer(reader.readLine());
        int nbEventsOnTheTransition = sT.countTokens() - 2;

        //get the source state
        int sourceID = configurationID_to_tableID.get(Integer.parseInt(sT.nextToken()));

        //create the transition
        StateEventTransition transition = new StateEventTransition();

        //get the events
        transition.events = new int[nbEventsOnTheTransition];
        for (int i = 0; i < nbEventsOnTheTransition; i++) {
            transition.events[i] = Integer.parseInt(sT.nextToken());
        }

        //get the target
        transition.target = configurationID_to_tableID.get(Integer.parseInt(sT.nextToken()));
        transition.id = inTransitionID;

        addTransition(sourceID, transition, ioSEM);
    }

    void addTransition(int sourceID, StateEventTransition inSET, StateEventModel ioSEM) {
        Collection<StateEventTransition> out = ioSEM.fanout.get(sourceID);
        if (out==null) {
            out = new ArrayList<>();
            ioSEM.fanout.put(sourceID, out);
        }
        out.add(inSET);
    }
}
