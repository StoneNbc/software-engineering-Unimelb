import java.util.List;
import java.util.Random;

// Environment Block class (Patch)
// This class represents a region in the simulation and manages hormone levels within that region.
public class Patch {



  private double anabolicHormoneMax = 200;
  private double catabolicHormoneMax = 250;
  private double anabolicHormoneMin = 50;
  private double catabolicHormoneMin = 52;

  public double anabolicHormone = 50;  // Hormone levels that promote muscle growth
  public double catabolicHormone = 52;  // Hormone levels that promote muscle breakdown
  
  public double hormoneDiffuseRate = 0.75; // Hormone diffusion rate

  public double nutrition_level; 
  public double nutrition_max = 150;
  public double nutrition_min = 50;

  private MuscleFiber muscleFiber;
  private double maxSize;



  private Random random = new Random();

  public Patch(double slowTwitchPercentage, double maxSize, double nutrition_level){
    this.maxSize = maxSize;
    this.nutrition_level = nutrition_level;
    createMuscleFiber(slowTwitchPercentage);
   
  }

  // Create muscle fibers
  public void createMuscleFiber(double slowTwitchPercentage) {
    for (int i = 0;  i < 20; i++) {
    if (random.nextDouble() * 100 >  slowTwitchPercentage) {
    maxSize += 1;  // Increase the maximum size of muscle fibers
    }
    }
    double initialSize = (0.2 + random.nextDouble() * 0.4) * maxSize; // Calculate the initial size
    muscleFiber = new MuscleFiber(initialSize, maxSize);  // Create new muscle fibers
  }

  // Call grow in fiber
  public void MuscleGrow(){
    muscleFiber.grow(anabolicHormone, catabolicHormone, nutrition_level);
  }

  //
  public void dailyActivity(){

    double fiberSize = muscleFiber.getFiberSize();
    double sizeLog = Math.log10(fiberSize);  // Use the natural logarithm
    catabolicHormone = catabolicHormone + 2.0 * sizeLog;
    anabolicHormone = anabolicHormone + 2.5 * sizeLog;
  }

  public void liftWeights(){
    
    double fiberSize = muscleFiber.getFiberSize();
    double sizeLog = Math.log10(fiberSize);  // Using natural logarithm
    catabolicHormone = catabolicHormone + 44 * sizeLog;
    anabolicHormone = anabolicHormone + 55 * sizeLog;

  }

  public void sleep(double hoursOfSleep){
    catabolicHormone = catabolicHormone - ( 0.5 * Math.log10(catabolicHormone ) * hoursOfSleep);
    anabolicHormone = anabolicHormone - ( 0.48 * Math.log10(anabolicHormone ) * hoursOfSleep);
  }


  // hormones diffues
  public void diffuseHormones(List<Patch> neighbor_patch) {
    // calculate the hormone received by every neighbor 
    double diffuseAnabolic = ( anabolicHormone * hormoneDiffuseRate) / 8;
    double diffuseCatabolic = ( catabolicHormone * hormoneDiffuseRate) / 8;

    // diffuse to every neighbor
    for (Patch neighbor : neighbor_patch) {
      // if its hormone is higher neighbors' hormone, diffuse to neighbor, or not
      // if( neighbor.anabolicHormone < anabolicHormone){
        neighbor.anabolicHormone += diffuseAnabolic; // neighbor recevie the hormone
        anabolicHormone -= diffuseAnabolic; // this patch lose the hormone
      // }
      // if(neighbor.catabolicHormone < catabolicHormone){
        neighbor.catabolicHormone += diffuseCatabolic;
        catabolicHormone -= diffuseCatabolic;
      // }        
      // regulate the hormone to ensure that they won't over the boundary
      
    }

  }
  // adjust nutrition
  public void adjustNutrition(){

    // The nutrition level was randomly adjusted to simulate real life
    nutrition_level = nutrition_level + random.nextDouble() * 20 - 10;
    // regulate the nutrition
    if (nutrition_level > nutrition_max)
      nutrition_level = nutrition_max;
    if (nutrition_level < nutrition_min)
      nutrition_level = nutrition_min;

  }

  // Regulate hormone levels to ensure they are within normal ranges
  public void regulateHormonesWithPatches() {
    if (anabolicHormone > anabolicHormoneMax) anabolicHormone = anabolicHormoneMax;
    if (anabolicHormone < anabolicHormoneMin) anabolicHormone = anabolicHormoneMin;
    if (catabolicHormone > catabolicHormoneMax) catabolicHormone = catabolicHormoneMax;
    if (catabolicHormone < catabolicHormoneMin) catabolicHormone = catabolicHormoneMin;
  }

  // public void setAnabolicHormone(double change) {
  //   anabolicHormone += change;
  // }

  // public void setCatabolicHormone(double change) {
  //   catabolicHormone += change;
  // }

  // print the patch
  public void printPatch(){
    System.out.println("ana: " + anabolicHormone);
    System.out.println("cata: " + catabolicHormone);
    System.out.println("fibersize: " + muscleFiber.getFiberSize());
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