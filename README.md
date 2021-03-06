##### Purpose
This module illustrates the components of an OBP2 language plugin.

 
##### Syntax

````
nbConfigurations nbTransitions nbClocks nbConfigurationElements
- clocks-names
clock-name-1 ... clock-name-nbClocks
- variable-names
variable-name-1 ... variable-name-n // the list of variable names in the model
- configurations
//the list of configurations, one configuration corresponds to the values of all model variables for one state in the state-space
conf-id value-1 ... value-n //a list of nbConfigurationElements
...
conf-id value-1 ... value-n //a list of nbConfigurationElements

- initial
initial-configuration-ID

- transitions
//the list of transitions
source-conf-id <clock-id-1> ... <clock-id-i> target-conf-id
...
source-conf-id <clock-id-1> ... <clock-id-j> target-conf-id