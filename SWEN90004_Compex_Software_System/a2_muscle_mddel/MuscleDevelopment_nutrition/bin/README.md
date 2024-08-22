# Muscle Fiber Simulation

## 简介
本项目模拟了肌肉纤维的行为，包含三个主要类：`MuscleFiber`，`Patch`，和`Simulation`。通过这些类的交互，我们可以模拟肌肉纤维在不同条件下的反应和行为。

## 文件说明
- `MuscleFiber.java`：定义了肌肉纤维的属性和行为。
- `Patch.java`：表示肌肉纤维的一个部分，包含了相关的物理属性和方法。
- `Simulation.java`：负责运行整个模拟过程，协调不同的`MuscleFiber`和`Patch`实例。

## 如何运行
1. 确保你已经安装了Java开发环境（JDK）。
2. 将所有Java文件放在同一目录下。
3. 打开终端或命令提示符，导航到文件所在的目录。
4. 编译所有Java文件：
   ```bash
   javac *.java
