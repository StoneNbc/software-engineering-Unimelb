import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Main class (Simulation)
// This class is responsible for creating and managing muscle fibers and environmental blocks, as well as controlling the main loop of the simulation.
public class Simulation {

  private List<Patch> patches = new ArrayList<>();
  private double daysBetweenWorkouts;
  private boolean lift = true; // Whether to do weight training

  private static final double SLOW_TWITCH_PERCENTAGE = 60; // The slow-jumping fibers are assumed to be 60%


  String filePath = "data.csv";
  //days
  private int days;
  // matrix side length 
  private int grid;
  // patch sum number
  private double numberOfPatch;
  // Muscle intensity
  private double intensity;
  // hours of sleep
  private double hoursOfSleep;
  // slow percentage
  private double slowTwitchPercentage;
  // nutrition level
  private double nutrition_level;
  // random function
  private Random random = new Random();
  
  public static void main(String[] args) {
    // default test data
    Simulation sim = new Simulation(1800,17, 95, 8, 5, 50, 75);
    try {
      sim.run();
    } catch (IOException e) {
      System.out.println("error: " + e.getMessage());
    }
  }
  // constructor
  public Simulation(int days, int grid, double intensity, double hoursOfSleep, double daysBetweenWorkouts, double slowTwitchPercentage, double nutrition_level) {
    this.days = days;
    this.grid = grid;
    this.numberOfPatch = Double.valueOf(grid * grid);
    this.intensity = intensity;
    this.hoursOfSleep = hoursOfSleep;
    this.daysBetweenWorkouts = daysBetweenWorkouts;
    this.slowTwitchPercentage = slowTwitchPercentage;
    this.nutrition_level = nutrition_level;

    // Initializes muscle fiber and hormone levels
    for (int i = 0; i < numberOfPatch; i++) {
      Patch patch = new Patch(slowTwitchPercentage, 4, nutrition_level);
      patches.add(patch);
      
    }

    // regulate-hormones
    regulateHormones();
    
    // printInitialSettings();
  }
  
  // run 
  public void run() throws IOException {

    File datafile = new File(filePath);
    BufferedWriter writer = new BufferedWriter(new FileWriter(datafile));

    writer.write("Nutrition Extension\n");
    writer.write("DAY,");
    writer.write("Muscle Mass,");
    writer.write("Anabolic,");
    writer.write("Catabolic\n");

    for (int day = 1; day <= days; day++) {

      dailyActivity(); // Hormonal regulation of daily activities
      if (lift && (day % daysBetweenWorkouts == 0)) {
        liftWeights(); // If it's time to lift weights, perform weight-lifting hormone conditioning
        // System.out.println("Day " + (day + 1) + " (After lifting weights):");
        // printStatus(); // 打印举重后的状态
      }
      sleep(); // Perform hormonal regulation during sleep every night
      
      // regulate hormones
      regulateHormones();

      developMuscle();  // Develop muscle based on adjusted hormone levels

      // System.out.println("End of Day " + (day + 1) + ": Total Muscle Mass = " + muscleMass);
      double muscleMass = updateMuscleMass(); ;  // Develop muscle based on adjusted hormone levels
      double anabolicMean = updateAnaboliceMeans(); // store anabolic mean
      double catabolicMean = updateCatabolicMeans(); // store catabolic mean

      // 输出所有patch值 测试使用
     /**
      for (Patch patch : patches) {
        patch.printPatch();
      }
      */
      writer.write(day+",");
      writer.write(String.valueOf(muscleMass/100)+",");
      writer.write(String.valueOf(anabolicMean)+",");
      writer.write(String.valueOf(catabolicMean)+"\n");

      // The values that need to be output to the csv file, other than sout, are test output
      System.out.println("day: " + day + "\nmuscleMass " + muscleMass);
      System.out.println("anabolic: " + anabolicMean);
      System.out.println("catabolic: " + catabolicMean);
      System.out.println("-----------------");
    }

    writer.flush();
    writer.close();
  }


  // develop muscle
  private void developMuscle() {
    for (Patch patch: patches){
      patch.MuscleGrow();
    }
    
  }

  // daily acticity
  private void dailyActivity() {
    for ( Patch patch: patches){
      patch.dailyActivity();
    }
    
  }

  // lift weight
  private void liftWeights() {
    for ( Patch patch: patches){
      if (random.nextDouble() < (intensity / 100 * intensity / 100) ) {
        patch.liftWeights();
      }
    }

  }

  //sleep
  private void sleep() {
    for (Patch patch : patches) {
      patch.sleep(hoursOfSleep);
    }
    
  }


  private void regulateHormones() {
    for (int j = 0; j < patches.size(); j++) {
      Patch currentPatch = patches.get(j);
      List<Patch> neighbors = findNeighbors(j);
      currentPatch.diffuseHormones(neighbors);
      
    }
    // All patches are hormonally regulated once
    for (Patch patch : patches) {
      patch.regulateHormonesWithPatches();
    }
  }

  // Helper method to find all neighbors of a specified patch
  private List<Patch> findNeighbors(int index) {
    List<Patch> neighbors = new ArrayList<>();
    int row = index / grid;
    int column = index % grid;
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue; // Skip self
        int neighborRow = row + i;
        int neighborColumn = column + j;
        if (isValidLocation(neighborRow, neighborColumn)) {
          neighbors.add(patches.get(neighborRow * grid + neighborColumn));
        }
      }
    }
    return neighbors;
  }

  // Check that the position is valid
  private boolean isValidLocation(int row, int column) {
    return row >= 0 && row < grid && column >= 0 && column < grid;
  }
  /**
   * private void regulateHormones(){
    // 找到相邻的八个
     for (int j = 0; j < patches.size(); j++) {
      int row = j / grid;
      int column = j % grid;
     for (int i = 0; i < patches.size(); i++) {
       int neighbor_row = i / grid;
       int neighbor_column = i % grid;
       if (Math.abs(row - neighbor_row) <= 1 && Math.abs(column - neighbor_column) <= 1 && !(row == neighbor_row && column == neighbor_column)){
         //对每个邻居进行扩散操作
        patches.get(j).diffuseHormones(patches.get(i));
       }
     }
   }
     for (int i = 0 ; i < patches.size();i++){
       // 模拟激素在相邻区域之间的扩散
       patches.get(i).regulateHormones(); // 调节激素水平
     }
  }
   */

  //  // 对每个邻居进行扩散操作
  //  for (Patch neighbor : neighbors) {
  //   // 只有当激素水平 >= 25 时才进行扩散
  //   if (currentPatch.anabolicHormone >= 25) {
  //     double anabolicDiffuseAmount = currentPatch.anabolicHormone * 0.65;
  //     neighbor.anabolicHormone += anabolicDiffuseAmount;
  //     currentPatch.anabolicHormone -= anabolicDiffuseAmount;
  //   }
  //   if (currentPatch.catabolicHormone >= 25) {
  //     double catabolicDiffuseAmount = currentPatch.catabolicHormone * 0.65;
  //     neighbor.catabolicHormone += catabolicDiffuseAmount;
  //     currentPatch.catabolicHormone -= catabolicDiffuseAmount;
  //   }


  // Calculate the muslcemass sum
  private double updateMuscleMass() {
    double muscleMass = 0;
    for (Patch patch: patches){
      muscleMass += patch.getMuscleFiber().getFiberSize();
      
    }
    return muscleMass;

  }

  // Calculate the anabolic average
  private double updateAnaboliceMeans(){
    double anabolicSum = 0;
    for (Patch patch: patches){
     anabolicSum += patch.getAnabolicHormone();
    }
    // System.out.println("test ana sum " + anabolicSum);
    double anabolicMean = anabolicSum / numberOfPatch;
    return anabolicMean;
  }

  // Calculate the catabolic average
  private double updateCatabolicMeans(){
    double catabolicSum = 0;
    for (Patch patch: patches){
     catabolicSum += patch.getCatabolicHormone();
    }
    // System.out.println("test cata sum " + catabolicSum);
    double catabolicMean = catabolicSum / numberOfPatch;
    return catabolicMean;
  }

  // private void printStatus() {
  //   for (int i = 0; i < muscleFibers.size(); i++) {
  //     System.out.println("Muscle Fiber " + (i + 1) + ": Size = " + muscleFibers.get(i).getFiberSize() +
  //         ", Anabolic Hormone = " + patches.get(i).getAnabolicHormone() +
  //         ", Catabolic Hormone = " + patches.get(i).getCatabolicHormone());
  //   }
  // }

  // private void printInitialSettings() {
  //   System.out.println("Simulation Settings:");
  //   System.out.println("Intensity: " + intensity);
  //   System.out.println("Hours of Sleep: " + hoursOfSleep);
  //   System.out.println("Days Between Workouts: " + daysBetweenWorkouts);
  //   System.out.println("Percentage of Slow-Twitch Fibers: " + slowTwitchPercentage + "%");
  // }
}