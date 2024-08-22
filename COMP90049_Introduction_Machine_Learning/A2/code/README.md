# Assignment 2
Stone 

This code contains Python code that demonstrates how to apply a variety of machine learning classifiers on a dataset. In addition, the effectiveness of `semi-supervised` learning with SelfTrainingClassifier is also discussed.

## Description
The project consists of the following parts:

* **Classifier evaluation:** Train and evaluate the accuracy and other metrics of multiple classifiers such as **naive Bayes**, **logistic regression**, **decision trees**, and **random forests**.
* **Semi-supervised learning:** Shows how to use logistic regression to use `SelfTrainingClassifier` in a semi-supervised scenario to improve model performance.
* **Baseline model:** Implemented a baseline model using `DummyClassifier `with policies such as `most_frequent` and `uniform` to establish benchmarks for performance comparison.

## Requirements

This project requires the following Python libraries:

* scikit-learn
* pandas (if data manipulation is needed)
* numpy

You can install these packages via pip:
```
pip install numpy pandas scikit-learn matplotlib

```

## Usage
* **Prepare dataset**: Ensure your dataset is formatted correctly, with features and labels suitable for classification.
* **Run the classifier evaluations**: You can modify and run the classification_models.py to evaluate different classifiers on your data.
* **Explore semi-supervised learning**: Use semi_supervised_learning.py to see how semi-supervised techniques might improve your model performance.
* **Evaluate baselines:** Run baseline_models.py to understand the baseline accuracies using simple strategies.