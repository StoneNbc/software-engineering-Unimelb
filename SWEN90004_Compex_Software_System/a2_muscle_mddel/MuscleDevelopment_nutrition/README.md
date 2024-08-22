# Muscle Fiber Simulation
bichuan liu
## Introduction
This project simulates the behavior of muscle fibers, consisting of three main classes: `MuscleFiber`, `Patch`, and `Simulation`. Through the interaction of these classes, we can simulate the response and behavior of muscle fibers under different conditions.

## File Descriptions
- `MuscleFiber.java`: Defines the properties and behaviors of a muscle fiber.
- `Patch.java`: Represents a segment of the muscle fiber, containing related physical properties and methods.
- `Simulation.java`: Manages the entire simulation process, coordinating different instances of `MuscleFiber` and `Patch`.

## How to Run
1. Ensure you have Java Development Kit (JDK) installed.
2. Place all Java files in the same directory.
3. Open a terminal or command prompt and navigate to the directory containing the files.
4. Compile all Java files:
   ```bash
   javac Simulation.java
   ```
5. Run the simulation
```bash
java Simulation.class
```
Besides this, runing the main function in Simulation.java in IDE can work as well.

## Extension
We introduced a new variable named nutrition_level to quantify the nutritional state and regulate the growth and decomposition of muscle fibers through this variable. The specific method is to introduce a nutrition_factor based on the calculation of nutrient level in the growth process of muscle fibers. This factor directly affects fiber size changes caused by protein breakdown and synthesis.

NUTRITION_LEVEL: This variable represents nutrient levels and adjusts hormone levels and muscle growth rates according to this level. Nutrient levels can be regulated through daily activities, diet, and specific supplement intake.
```java
    // Calculate the nutrition factor
    double nutrition_factor = 0.1 + 0.9 *((nutrition_level - 50 ) / 100);
```
Through calculation, nutrition is scaled to between 0.1 and 1.
```java
    // Calculate the effects of nutrition on hormone
    double anabolicLog = Math.log10(anabolicHormone ) * nutrition_factor;
    double catabolicLog = Math.log10(catabolicHormone ) * 1.05 * nutrition_factor;
    double anabolicEffect = 0.20 * Math.min(anabolicLog, catabolicLog);
    fiberSize += anabolicEffect;
```
 
Calculate the effects of nutrition on hormone
