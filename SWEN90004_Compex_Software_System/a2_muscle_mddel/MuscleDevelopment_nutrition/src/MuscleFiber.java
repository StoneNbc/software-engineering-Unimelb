// MuscleFiber
// This class represents a muscle fiber that has its size and the maximum allowed size.
public class MuscleFiber {

  private double fiberSize;  // Current size of muscle fibers
  private double maxSize;  // Maximum possible size of muscle fibers

  public MuscleFiber(double initialSize, double maxSize) {
    this.fiberSize = initialSize;
    this.maxSize = maxSize;
    regulateMuscleFiber();
  }

// Adjust the size of the muscle fibers to ensure that they do not exceed the limit
// Make sure the fiber size is within a reasonable range
  public void regulateMuscleFiber() {
    if (fiberSize < 1) fiberSize = 1;
    if (fiberSize > maxSize) fiberSize = maxSize;
  }


  // Simulate the growth and atrophy of muscle fibers
  // The size of renewed muscle fibers is based on hormone levels
  public void grow(double anabolicHormone, double catabolicHormone, double nutrition_level) {

    // Calculate the nutrition factor
    double nutrition_factor = 0.1 + 0.9 *((nutrition_level - 50 ) / 100);

    // Calculations are based on the reduced impact of catabolic hormone
    double catabolicEffect = 0.20 * Math.log10(catabolicHormone) * nutrition_factor;
    fiberSize -= catabolicEffect;

    // Calculate the effects of nutrition on hormone
    double anabolicLog = Math.log10(anabolicHormone ) * nutrition_factor;
    double catabolicLog = Math.log10(catabolicHormone ) * 1.05 * nutrition_factor;
    double anabolicEffect = 0.20 * Math.min(anabolicLog, catabolicLog);
    fiberSize += anabolicEffect;

    // Make sure the muscle fibers are within a reasonable size range by regulating
    regulateMuscleFiber();
  }

  public double getFiberSize() {
    return fiberSize;
  }
}