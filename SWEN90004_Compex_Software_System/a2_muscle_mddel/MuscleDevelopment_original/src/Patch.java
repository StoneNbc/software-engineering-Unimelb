import java.util.List;
import java.util.Random;

//环境区块类 (Patch)
//这个类代表模拟中的一个区域，管理该区域内的激素水平。
public class Patch {



  private double anabolicHormoneMax = 200;
  private double catabolicHormoneMax = 250;
  private double anabolicHormoneMin = 50;
  private double catabolicHormoneMin = 52;

  public double anabolicHormone = 50; // 促进肌肉增长的激素水平
  public double catabolicHormone = 52; // 促进肌肉分解的激素水平

  public double hormoneDiffuseRate = 0.75; // 激素的扩散率

  private MuscleFiber muscleFiber;
  private double maxSize;



  private Random random = new Random();

  public Patch(double slowTwitchPercentage, double maxSize){
    this.maxSize = maxSize;
    createMuscleFiber(slowTwitchPercentage);

  }



  //创建肌肉纤维
  public void createMuscleFiber(double slowTwitchPercentage) {
    for (int i = 0; i < 20; i++) {
      if (random.nextDouble() * 100 > slowTwitchPercentage) {
        maxSize += 1; // 增加肌肉纤维的最大大小
      }
    }
    double initialSize = (0.2 + random.nextDouble() * 0.4) * maxSize; // 计算初始大小
    muscleFiber = new MuscleFiber(initialSize, maxSize); // 创建新的肌肉纤维
  }

  // 调用fiber中的grow
  public void MuscleGrow(){
    muscleFiber.grow(anabolicHormone, catabolicHormone);
  }

  //
  public void dailyActivity(){

    double fiberSize = muscleFiber.getFiberSize();
    double sizeLog = Math.log10(fiberSize);  // 使用自然对数，+1以避免对数为负
    catabolicHormone = catabolicHormone + 2.0 * sizeLog;
    anabolicHormone = anabolicHormone + 2.5 * sizeLog;
  }

  public void liftWeights(){
    
    double fiberSize = muscleFiber.getFiberSize();
    double sizeLog = Math.log10(fiberSize);  // 使用自然对数，+1以避免对数为负
    catabolicHormone = catabolicHormone + 44 * sizeLog;
    anabolicHormone = anabolicHormone + 55 * sizeLog;

  }

  public void sleep(double hoursOfSleep){
    catabolicHormone = catabolicHormone - ( 0.5 * Math.log10(catabolicHormone ) * hoursOfSleep);
    anabolicHormone = anabolicHormone - ( 0.48 * Math.log10(anabolicHormone ) * hoursOfSleep);
  }


  // 激素扩散逻辑 hormones diffues
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
  
  // 调节激素水平，确保它们在正常范围内
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