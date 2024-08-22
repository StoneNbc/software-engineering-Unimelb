import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


// Main Controller (Simulation)
// This class is responsible for creating and managing muscle fibers and environment blocks, as well as controlling the main loop of the simulation.
public class Simulation implements Runnable {

  private List<Patch> patches = new ArrayList<>();
  private double daysBetweenWorkouts;
  private boolean lift = true; // Whether to engage in weightlifting training" in English

  private static final double SLOW_TWITCH_PERCENTAGE = 0; // Assuming slow twitch fibers account for 0%
  // create file
  String filePath;
  //days
  private int days;
  // matrix side length 
  private int grid;
  // patch sum number
  private double numberOfPatch;
  // muscle intensity
  private double intensity;
  // sleep time
  private double hoursOfSleep;
  //slow muscle
  private double slowTwitchPercentage;

  //Gender
  private String gender;
  private Random random = new Random();

  public static void main(String[] args) {
    //male simulation use thread to choose different gender start and show the result in different pages
    Simulation simMale = new Simulation(800, 17, 95, 8, 5, 35, "male", "maleData.csv");
    Thread maleThread = new Thread(simMale);
    maleThread.start();
    try {
      maleThread.join();
    } catch (InterruptedException e) {
      System.out.println("error: " + e.getMessage());
    }
    // After male simulation is done, run female simulation
    Simulation simFemale = new Simulation(800, 17, 95, 8, 5, 46, "female", "femaleData.csv");
    Thread femaleThread = new Thread(simFemale);
    femaleThread.start();
    try {
      femaleThread.join();
    } catch (InterruptedException e) {
      System.out.println("error: " + e.getMessage());
    }
  }


  public Simulation(int days, int grid, double intensity, double hoursOfSleep, double daysBetweenWorkouts,
      double slowTwitchPercentage, String gender, String filePath) {
    this.days = days;
    this.grid = grid;
    this.numberOfPatch = Double.valueOf(grid * grid);
    this.intensity = intensity;
    this.hoursOfSleep = hoursOfSleep;
    this.daysBetweenWorkouts = daysBetweenWorkouts;
    this.slowTwitchPercentage = slowTwitchPercentage;
    this.gender = gender;
    this.filePath = filePath;
    // 初始化肌肉纤维和激素水平
    for (int i = 0; i < numberOfPatch; i++) {
      Patch patch = new Patch(slowTwitchPercentage, 4);
      patches.add(patch);

    }

    // regulate-hormones
    regulateHormones();

    // printInitialSettings();
  }

  public String getGender() {
    return gender;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void run(Simulation simulation) throws IOException {
    // Get the gender of the simulation
    String gender1 = simulation.getGender();

    // Create a file to store simulation data
    File datafile = new File(simulation.getFilePath());
    BufferedWriter writer = new BufferedWriter(new FileWriter(datafile));

    writer.write("Gender\n");
    writer.write("DAY,");
    writer.write(gender1 + ":Muscle Mass,");
    writer.write(gender1 + ":Anabolic,");
    writer.write(gender1 + ":Catabolic\n");
    // Iterate over each day of the simulation
    for (int day = 1; day <= days; day++) {
      // Perform daily hormone regulation activities
      dailyActivity(gender1);

      // Check if weightlifting should be performed on this day
      if (lift && (day % daysBetweenWorkouts == 0)) {
        // Perform weightlifting hormone regulation
        liftWeights();
      }

      // Perform hormone regulation during sleep every night
      sleep(gender1);

      // Regulate hormones
      regulateHormones();

      // Develop muscle based on adjusted hormone levels
      developMuscle();

      // Store total muscle mass
      double muscleMass = updateMuscleMass();

      // Store mean anabolic value
      double anabolicMean = updateAnaboliceMeans();

      // Store mean catabolic value
      double catabolicMean = updateCatabolicMeans();

      // Write data to the file
      writer.write(day + ",");
      writer.write(String.valueOf(muscleMass) + ",");
      writer.write(String.valueOf(anabolicMean) + ",");
      writer.write(String.valueOf(catabolicMean) + "\n");

      // Print values for testing purposes
      System.out.println("day: " + day + "\n" + gender1 + ":muscleMass " + muscleMass);
      System.out.println(gender1 + ":anabolic: " + anabolicMean);
      System.out.println(gender1 + ":catabolic: " + catabolicMean);
      System.out.println("-----------------");
    }

    // Flush and close the writer
    writer.flush();
    writer.close();
  }


  // developMuscle
  private void developMuscle() {
    for (Patch patch : patches) {
      patch.MuscleGrow();
    }

  }

  // dailyActivity
  private void dailyActivity(String gender1) {
    for (Patch patch : patches) {
      patch.dailyActivity(gender1);
    }

  }

  // liftWeights
  private void liftWeights() {
    // every patch need to lift
    for (Patch patch : patches) {
      if (random.nextDouble() < (intensity / 100 * intensity / 100)) {
        patch.liftWeights();
      }
    }

  }

  //sleep
  private void sleep(String gender) {
    for (Patch patch : patches) {
      patch.sleep(hoursOfSleep, gender);
    }

  }


  private void regulateHormones() {
    for (int j = 0; j < patches.size(); j++) {
      Patch currentPatch = patches.get(j);
      List<Patch> neighbors = findNeighbors(j);
      currentPatch.diffuseHormones(neighbors);

    }
// Regulate hormones for all patches once
    for (Patch patch : patches) {
      patch.regulateHormonesWithPatches();
    }
  }

  // Helper method to find all neighbors of a specified patch
  private List<Patch> findNeighbors(int index) {
    // List to store neighboring patches
    List<Patch> neighbors = new ArrayList<>();
    // Calculate row and column of the specified patch
    int row = index / grid;
    int column = index % grid;

    // Iterate over neighboring cells
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        // Skip the current patch
        if (i == 0 && j == 0) {
          continue;
        }

        // Calculate row and column of the neighboring patch
        int neighborRow = row + i;
        int neighborColumn = column + j;

        // Check if the neighboring location is valid
        if (isValidLocation(neighborRow, neighborColumn)) {
          // Add the neighboring patch to the list
          neighbors.add(patches.get(neighborRow * grid + neighborColumn));
        }
      }
    }
    return neighbors; // Return the list of neighboring patches
  }

  // isValidLocation
  private boolean isValidLocation(int row, int column) {
    return row >= 0 && row < grid && column >= 0 && column < grid;
  }

  // updateMuscleMass
  private double updateMuscleMass() {
    double muscleMass = 0;
    for (Patch patch : patches) {
      muscleMass += patch.getMuscleFiber().getFiberSize();

    }
    return muscleMass;

  }

  //updateAnaboliceMeans
  private double updateAnaboliceMeans() {
    double anabolicSum = 0;
    for (Patch patch : patches) {
      anabolicSum += patch.getAnabolicHormone();
    }
    // System.out.println("test ana sum " + anabolicSum);
    double anabolicMean = anabolicSum / numberOfPatch;
    return anabolicMean;
  }

  // updateCatabolicMeans
  private double updateCatabolicMeans() {
    double catabolicSum = 0;
    for (Patch patch : patches) {
      catabolicSum += patch.getCatabolicHormone();
    }
    // System.out.println("test cata sum " + catabolicSum);
    double catabolicMean = catabolicSum / numberOfPatch;
    return catabolicMean;
  }


  @Override
  public void run() {
    try {
      // Run the detailed simulation and output results
      run(this);
    } catch (IOException e) {
      System.out.println("error: " + e.getMessage());
    }
  }
}
