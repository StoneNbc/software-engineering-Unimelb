// Muscle Fiber Class
// This class represents a muscle fiber with its size and maximum allowed size.
public class MuscleFiber {

  private double fiberSize; // Current size of the muscle fiber
  private double maxSize; // Maximum possible size of the muscle fiber

  // Constructor to initialize muscle fiber with initial size and maximum size
  public MuscleFiber(double initialSize, double maxSize) {
    this.fiberSize = initialSize;
    this.maxSize = maxSize;
    regulateMuscleFiber(); // Ensure muscle fiber size stays within limits
  }

  // Adjusts the size of the muscle fiber to ensure it doesn't exceed the limit
  // Ensures the fiber size remains within a reasonable range
  public void regulateMuscleFiber() {
    if (fiberSize < 1) {
      fiberSize = 1;
    }
    if (fiberSize > maxSize) {
      fiberSize = maxSize;
    }
  }

  // Simulates the growth and shrinkage process of the muscle fiber
  // Updates the size of the muscle fiber based on hormone levels
  public void grow(double anabolicHormone, double catabolicHormone) {
    // Calculate the decrease effect based on catabolic hormone
    double catabolicEffect = 0.20 * Math.log10(catabolicHormone); // +1 to prevent logarithm of zero or negative value
    fiberSize -= catabolicEffect;

    // Calculate the growth effect based on anabolic hormone
    double anabolicLog = Math.log10(anabolicHormone);
    double catabolicLog = Math.log10(catabolicHormone) * 1.05;
    double anabolicEffect = 0.20 * Math.min(anabolicLog, catabolicLog);
    fiberSize += anabolicEffect;

    // Ensure the muscle fiber size remains within a reasonable range
    regulateMuscleFiber();
  }

  // Getter method to retrieve the size of the muscle fiber
  public double getFiberSize() {
    return fiberSize;
  }
}
