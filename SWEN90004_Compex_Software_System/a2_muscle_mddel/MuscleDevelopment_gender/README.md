# Muscle Development Simulation

## Overview

This project simulates muscle development using an agent-based model. The model considers various factors influencing muscle growth, including gender differences. The simulation is written in Java and is inspired by the NetLogo modeling environment.

## Files

- **MuscleFiber.java**: Defines the properties and behaviors of individual muscle fibers.
- **Patch.java**: Represents patches of muscle tissue in the simulation.
- **Simulation.java**: Manages the overall simulation, including initializing and running the model.

## Classes

### MuscleFiber.java

This class represents individual muscle fibers in the simulation. Key attributes and methods include:

- **Attributes**:
    - `double size`: The size of the muscle fiber.
    - `String gender`: The gender of the individual (male or female) to account for differences in muscle development.

- **Methods**:
    - `grow()`: Simulates the growth of the muscle fiber over time.
    - `shrink()`: Simulates the shrinking of the muscle fiber under certain conditions.
    - `updateGenderImpact()`: Adjusts the growth rate based on the gender attribute.

### Patch.java

This class represents a patch of muscle tissue, containing multiple muscle fibers. Key attributes and methods include:

- **Attributes**:
    - `List<MuscleFiber> fibers`: A list of muscle fibers within the patch.

- **Methods**:
    - `addFiber(MuscleFiber fiber)`: Adds a muscle fiber to the patch.
    - `removeFiber(MuscleFiber fiber)`: Removes a muscle fiber from the patch.
    - `updatePatch()`: Updates the state of the patch, including the growth of all muscle fibers within it.

### Simulation.java

This class manages the overall simulation. Key attributes and methods include:

- **Attributes**:
    - `List<Patch> patches`: A list of patches in the simulation.
    - `int timeSteps`: The number of time steps the simulation will run.

- **Methods**:
    - `initialize()`: Initializes the simulation by creating patches and populating them with muscle fibers.
    - `run()`: Runs the simulation for the specified number of time steps.
    - `updateSimulation()`: Updates the state of the entire simulation at each time step.

## Usage

1. **Compile the Java files**:
   javac MuscleFiber.java Patch.java Simulation.java
2. **Run the simulation**:
   java Simulation
3. **The simulation will output the growth of muscle fibers over time, considering the gender-specific differences in development**.

## Future Work

- Implement more detailed physiological factors influencing muscle growth.
- Add user interface for visualizing the simulation.
- Conduct parameter sensitivity analysis.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.

## Acknowledgments

- Inspired by the NetLogo modeling environment.
- Special thanks to the course instructors and peers for their support and feedback.