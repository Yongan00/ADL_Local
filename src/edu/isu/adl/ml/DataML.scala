package edu.isu.adl.ml

import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.mllib.linalg._
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.PipelineModel

object DataML {
  
  def resultDataMapper(line: String): (TrainingDataFormat) = {
    val attr = line.split(',')
    //val label = attr(22).split('_')
    return TrainingDataFormat(attr(0).toDouble, 
                      attr(1).toDouble,
                      attr(2).toDouble,
                      attr(3).toDouble,
                      attr(4).toDouble,
                      attr(5).toDouble,
                      attr(6).toDouble,
                      attr(7).toDouble,
                      attr(8).toDouble,
                      attr(9).toDouble,
                      attr(10).toDouble,
                      attr(11).toDouble,
                      attr(12).toDouble,
                      attr(13).toDouble,
                      attr(14).toDouble,
                      attr(15).toDouble,
                      attr(16).toDouble,
                      attr(17).toDouble,
                      attr(18).toDouble,
                      attr(19).toDouble,
                      attr(20), 
                      attr(22),
                      attr(23))
  }
  
  def main(args: Array[String]){
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val ss = SparkSession
      .builder()
      .appName("trainingData")
      .master("local[*]")
      .getOrCreate()      
      
    import ss.implicits._
    val lines = ss.sparkContext.textFile("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult2.csv")
    val data = lines.map(resultDataMapper).toDS().cache()
    //println("Here is our inferred schema:")
    //data.printSchema()
    //println("Let's select the label column:")
    //data.select("label").show()
    
    //Step1: Index ActionLabel(String) to number
    val actionLabelIndexer = new StringIndexer()
        .setInputCol("actionLabel")
        .setOutputCol("indexedActionLabel")
        .fit(data)
    //val dataWithAllNum = actionLabelIndexer.transform(data)
    //dataWithAllNum.show()
    
    //Step2: Assemble multiple columns to one column as feature
    val dataAssembler = new VectorAssembler()
        .setInputCols(Array("light", "hour", "moving", "turning", "screenOn", "earPlug", "indexedActionLabel"))
        .setOutputCol("workOrSleepFeatures")
    //val dataAssemble = dataAssembler.transform(dataWithAllNum)
    //dataAssemble.select("features").show(50)
    //Step3: convert assembled features to indexed features
    val featureIndexer = new VectorIndexer()
        .setInputCol("workOrSleepFeatures")
        .setOutputCol("indexedWOSFeatures")
        .setMaxCategories(2)
    //val dataIndexed = featureIndexer.transform(dataAssemble)
    //dataIndexed.select("indexedFeatures", "features").show(20) 

    //Step4: Index label column workOrSleep to number
    val labelIndexer = new StringIndexer()
        .setInputCol("workOrSleep")
        .setOutputCol("indexedWorkOrSleep")
        .fit(data)   
    //Step5: Configure a decision tree classifier
    val decTree = new DecisionTreeClassifier()
        .setFeaturesCol("indexedWOSFeatures")
        .setLabelCol("indexedWorkOrSleep")
        .setMaxBins(55)
    //Step6: Convert back from indexed label number to label name
    val labelConverter = new IndexToString()
        .setInputCol("prediction")
        .setOutputCol("predictionLabel")
        .setLabels(labelIndexer.labels)
        
    //train model and test
    //val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))     
    val pipeline = new Pipeline()
       .setStages(Array(actionLabelIndexer, dataAssembler, featureIndexer, labelIndexer, decTree, labelConverter))
    
    val model = pipeline.fit(data)
   
    val predictions = model.transform(data)
    
    predictions.select("predictionLabel", "workOrSleep").show(10)
        
    val evaluator = new MulticlassClassificationEvaluator()
                    .setLabelCol("indexedWorkOrSleep")
                    .setPredictionCol("prediction")
                    .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println(s"Test Error = ${(1.0 - accuracy)}")
    
    val treeModel = model.stages(4).asInstanceOf[DecisionTreeClassificationModel]
    println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
    
    //save model and pipeline to disk
    model.write.overwrite().save("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\workOrSleepModel")
    pipeline.write.overwrite().save("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\workOrSleepPipeline")
    ss.stop()
  }
}