import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//主控制器 (Simulation)
//这个类负责创建和管理肌肉纤维和环境区块，以及控制模拟的主循环。
public class Simulation {

  private List<Patch> patches = new ArrayList<>();
  private double daysBetweenWorkouts;
  private boolean lift = true; // 是否进行举重训练

  private static final double SLOW_TWITCH_PERCENTAGE = 60; // 假定慢跳纤维占60%
  // private double muscleMass;  // 存储肌肉总质量
  // private double anabolicMean; // 存anabolic平均值
  // private double catabolicMean; // 存 catabolic平均值

  String filePath = "data.csv";
  //days
  private int days;
  // matrix side length 
  private int grid;
  // patch sum number
  private double numberOfPatch;
  // 肌肉强度
  private double intensity;
  // 睡眠质量
  private double hoursOfSleep;
  //减缓速率
  private double slowTwitchPercentage;
  
  private Random random = new Random();
  
  public static void main(String[] args) {
    //测试
    Simulation sim = new Simulation(800,17,70, 3, 7, 70);
    try {
      sim.run();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("error: " + e.getMessage());
    }
  }

  public Simulation(int days, int grid, double intensity, double hoursOfSleep, double daysBetweenWorkouts, double slowTwitchPercentage) {
    this.days = days;
    this.grid = grid;
    this.numberOfPatch = Double.valueOf(grid * grid);
    this.intensity = intensity;
    this.hoursOfSleep = hoursOfSleep;
    this.daysBetweenWorkouts = daysBetweenWorkouts;
    this.slowTwitchPercentage = slowTwitchPercentage;

    // 初始化肌肉纤维和激素水平
    for (int i = 0; i < numberOfPatch; i++) {
      Patch patch = new Patch(slowTwitchPercentage, 4);
      patches.add(patch);
      
    }

    // regulate-hormones
    regulateHormones();
    
    // printInitialSettings();
  }
  
  public void run() throws IOException {

    File datafile = new File(filePath);
    BufferedWriter writer = new BufferedWriter(new FileWriter(datafile));

    for (int day = 1; day <= days; day++) {

      dailyActivity(); // 执行日常活动的激素调节
      if (lift && (day % daysBetweenWorkouts == 0)) {
        liftWeights(); // 如果到了举重的日子，执行举重激素调节
        // System.out.println("Day " + (day + 1) + " (After lifting weights):");
        // printStatus(); // 打印举重后的状态
      }
      sleep(); // 每天晚上执行睡眠期间的激素调节
      
      // regulate hormones
      regulateHormones();

      developMuscle();  // 基于调整后的激素水平发展肌肉

      // System.out.println("End of Day " + (day + 1) + ": Total Muscle Mass = " + muscleMass);
      double muscleMass = updateMuscleMass(); ;  // 存储肌肉总质量
      double anabolicMean = updateAnaboliceMeans(); // 存anabolic平均值
      double catabolicMean = updateCatabolicMeans(); // 存 catabolic平均值

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

      // 需要输出到csv文件的值， 除此处sout外，其他皆为测试输出
      System.out.println("day: " + day + "\nmuscleMass " + muscleMass);
      System.out.println("anabolic: " + anabolicMean);
      System.out.println("catabolic: " + catabolicMean);
      System.out.println("-----------------");
    }

    writer.flush();
    writer.close();
  }


  // 增肌
  private void developMuscle() {
    for (Patch patch: patches){
      patch.MuscleGrow();
    }
    
  }

  // 日常锻炼
  private void dailyActivity() {
    for ( Patch patch: patches){
      patch.dailyActivity();
    }
    
  }

  // 举重
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
    // 对所有补丁进行一次激素调节
    for (Patch patch : patches) {
      patch.regulateHormonesWithPatches();
    }
  }

  // 辅助方法，用于找到指定补丁的所有邻居
  private List<Patch> findNeighbors(int index) {
    List<Patch> neighbors = new ArrayList<>();
    int row = index / grid;
    int column = index % grid;
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue; // 跳过自身
        int neighborRow = row + i;
        int neighborColumn = column + j;
        if (isValidLocation(neighborRow, neighborColumn)) {
          neighbors.add(patches.get(neighborRow * grid + neighborColumn));
        }
      }
    }
    return neighbors;
  }

  // 检查位置是否有效
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


  // 计算muslcemass总和
  private double updateMuscleMass() {
    double muscleMass = 0;
    for (Patch patch: patches){
      muscleMass += patch.getMuscleFiber().getFiberSize();
      
    }
    return muscleMass;

  }

  //计算anabolic平均值
  private double updateAnaboliceMeans(){
    double anabolicSum = 0;
    for (Patch patch: patches){
     anabolicSum += patch.getAnabolicHormone();
    }
    // System.out.println("test ana sum " + anabolicSum);
    double anabolicMean = anabolicSum / numberOfPatch;
    return anabolicMean;
  }

  // 计算catabolic平均值
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