import java.util.List;
import java.util.Random;

// Environment Block Class (Patch)
// This class represents a region in the simulation, managing hormone levels within that region.
public class Patch {


  private double anabolicHormoneMax = 200;
  private double catabolicHormoneMax = 250;
  private double anabolicHormoneMin = 50;
  private double catabolicHormoneMin = 52;

  double maleMetabolicRate = 1.6;  // male metabolic rate

  double femaleMetabolicRate = 1.0;  // female metabolic rate
  public double anabolicHormone = 50; // The hormone levels that promote muscle growth
  public double catabolicHormone = 52; // The hormone levels that promote muscle breakdown

  public double hormoneDiffuseRate = 0.75; // The diffusion rate of hormones

  private MuscleFiber muscleFiber;
  private double maxSize;


  private Random random = new Random();

  public Patch(double slowTwitchPercentage, double maxSize) {
    this.maxSize = maxSize;
    createMuscleFiber(slowTwitchPercentage);

  }


  //Creating muscle fibers
  public void createMuscleFiber(double slowTwitchPercentage) {
    for (int i = 0; i < 20; i++) {
      if (random.nextDouble() * 100 > slowTwitchPercentage) {
        maxSize += 1; // Increasing the maximum size of muscle fibers
      }
    }
    double initialSize = (0.2 + random.nextDouble() * 0.4) * maxSize; // Calculating initial size
    muscleFiber = new MuscleFiber(initialSize, maxSize); // Creating new muscle fibers
  }

  // Calling the "grow" function/method in the "fiber"
  public void MuscleGrow() {
    muscleFiber.grow(anabolicHormone, catabolicHormone);
  }


  public void dailyActivity(String gender) {
    if (gender.equals("male")) {
      // Get the size of muscle fiber
      double fiberSize = muscleFiber.getFiberSize();

      // Calculate the logarithm of fiber size
      double sizeLog = Math.log10(fiberSize);

      // Update catabolic hormone level based on male metabolic rate
      catabolicHormone = catabolicHormone + 2.0 * sizeLog * maleMetabolicRate;

      // Update anabolic hormone level based on male metabolic rate
      anabolicHormone = anabolicHormone + 2.5 * sizeLog * maleMetabolicRate;
    } else {
      // Get the size of muscle fiber
      double fiberSize = muscleFiber.getFiberSize();

      // Calculate the logarithm of fiber size
      double sizeLog = Math.log10(fiberSize);

      // Update catabolic hormone level based on female metabolic rate
      catabolicHormone = catabolicHormone + 2.0 * sizeLog * femaleMetabolicRate;

      // Update anabolic hormone level based on female metabolic rate
      anabolicHormone = anabolicHormone + 2.5 * sizeLog * femaleMetabolicRate;
    }
  }


  public void liftWeights() {
    // Get the size of muscle fiber
    double fiberSize = muscleFiber.getFiberSize();

    // Calculate the logarithm of fiber size
    double sizeLog = Math.log10(fiberSize);

    // Increase catabolic hormone level based on the logarithm of fiber size
    catabolicHormone = catabolicHormone + 44 * sizeLog;

    // Increase anabolic hormone level based on the logarithm of fiber size
    anabolicHormone = anabolicHormone + 55 * sizeLog;
  }

  public void sleep(double hoursOfSleep, String gender) {
    // Adjust hormone levels based on sleep duration and metabolic rate

    // Check if the gender is male
    if (gender.equals("male")) {
      // Decrease catabolic hormone level based on sleep duration and male metabolic rate
      catabolicHormone = catabolicHormone - (0.5 * Math.log10(catabolicHormone) * hoursOfSleep * maleMetabolicRate);

      // Decrease anabolic hormone level based on sleep duration and male metabolic rate
      anabolicHormone = anabolicHormone - (0.48 * Math.log10(anabolicHormone) * hoursOfSleep * maleMetabolicRate);
    } else { // If gender is not male
      // Decrease catabolic hormone level based on sleep duration
      catabolicHormone = catabolicHormone - (0.5 * Math.log10(catabolicHormone) * hoursOfSleep);

      // Decrease anabolic hormone level based on sleep duration
      anabolicHormone = anabolicHormone - (0.48 * Math.log10(anabolicHormone) * hoursOfSleep);
    }
  }


  // hormones diffuse in the netlogo diffuse function
  public void diffuseHormones(List<Patch> neighbor_patch) {
    // calculate the hormone received by every neighbor 
    double diffuseAnabolic = (anabolicHormone * hormoneDiffuseRate) / 8;
    double diffuseCatabolic = (catabolicHormone * hormoneDiffuseRate) / 8;

    // diffuse to every neighbor
    for (Patch neighbor : neighbor_patch) {
      // if its hormone is higher neighbors' hormone, diffuse to neighbor, or not
      // if( neighbor.anabolicHormone < anabolicHormone){
      neighbor.anabolicHormone += diffuseAnabolic; // neighbor recevie the hormone
      anabolicHormone -= diffuseAnabolic; // this patch lose the hormone
      neighbor.catabolicHormone += diffuseCatabolic;
      catabolicHormone -= diffuseCatabolic;

    }

  }

  // Regulate hormone levels to ensure they are within normal ranges
  public void regulateHormonesWithPatches() {
    if (anabolicHormone > anabolicHormoneMax) {
      anabolicHormone = anabolicHormoneMax;
    }
    if (anabolicHormone < anabolicHormoneMin) {
      anabolicHormone = anabolicHormoneMin;
    }
    if (catabolicHormone > catabolicHormoneMax) {
      catabolicHormone = catabolicHormoneMax;
    }
    if (catabolicHormone < catabolicHormoneMin) {
      catabolicHormone = catabolicHormoneMin;
    }
  }

  public double getAnabolicHormone() {
    return anabolicHormone;
  }

  public double getCatabolicHormone() {
    return catabolicHormone;
  }

  public MuscleFiber getMuscleFiber() {
    return muscleFiber;
  }

}