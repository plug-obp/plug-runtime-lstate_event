##### Purpose
The Explicit runtime exist only for development and debug purposes.

The programs are written in JSON and are mapped to ExplicitProgram using
Jackson-databind.

An example program :

    {
      "variables": {
        "a": 0,
        "b": 1,
        "c": 2
      },
      "initial": 0,
      "states": [
        [1, 2, 3],
        [3, 2, 1]
      ],
      "fanout": [
        [0, 1],
        [1]
      ]
    }

The ``variables`` field define a map between variable names and their 
index in the configuration.
The ``initial`` field gives the index of the initial state in the states
array.
The ``states`` are an array of configurations. 
Each configuration is an array of *int*.
The ``fanout`` field is an array which at index **i** contains the fanout
out the configuration **i**.

##### A more complex example
- In some situation we encounter models with multiple initial states.
- When firing a transition multiple target configurations could be reached
    - in the case of m-input/n-output synchronizations with side effects
    
An example representation for these cases :


    {
      "variables": {
        "a": 0,
        "b": 1,
        "children": 2
      },
      "initial": [0, 2],
      "states": [
        [1, 2, 3],
        [1, 3, 2],
        [2, 1, 3],
        [2, 3, 1],
        [3, 1, 2],
        [3, 2, 1],
      ],
      "transitions":[
        [0, 1],
        [3],
        [2, 3],
        [5],
        [0, 4],
      ],
      "fanout": [
        [0, 1],
        [1],
        [1, 2]
      ]
    }
