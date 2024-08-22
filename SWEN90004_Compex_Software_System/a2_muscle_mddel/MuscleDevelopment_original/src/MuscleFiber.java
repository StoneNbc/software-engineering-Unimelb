//肌肉纤维类 (MuscleFiber)
//这个类代表一个肌肉纤维，拥有其大小和允许的最大大小。
public class MuscleFiber {
  private double fiberSize; // 肌肉纤维的当前大小
  private double maxSize; // 肌肉纤维的最大可能大小

  public MuscleFiber(double initialSize, double maxSize) {
    this.fiberSize = initialSize;
    this.maxSize = maxSize;
    regulateMuscleFiber();
  }

  // 调整肌肉纤维的大小确保其不超出限制
  // 确保纤维大小在合理范围内
  public void regulateMuscleFiber() {
    if (fiberSize < 1) fiberSize = 1;
    if (fiberSize > maxSize) fiberSize = maxSize;
  }

  // 模拟肌肉纤维的生长和萎缩过程
  // 更新肌肉纤维的大小基于激素水平
  public void grow(double anabolicHormone, double catabolicHormone) {
    // 计算基于catabolic hormone的减少影响
    double catabolicEffect = 0.20 * Math.log10(catabolicHormone); // +1 防止取对数时值为负或零
    fiberSize -= catabolicEffect;

    // 计算基于anabolic hormone的增长影响
    double anabolicLog = Math.log10(anabolicHormone );
    double catabolicLog = Math.log10(catabolicHormone ) * 1.05;
    double anabolicEffect = 0.20 * Math.min(anabolicLog, catabolicLog);
    fiberSize += anabolicEffect;

    // 确保肌肉纤维的大小在合理范围内
    regulateMuscleFiber();
  }

  public double getFiberSize() {
    return fiberSize;
  }
}