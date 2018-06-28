package edu.isu.adl.ml

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.PipelineModel

object DataMLForBodyAction {
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
  
  def main(args:Array[String]){
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val ss = SparkSession
      .builder()
      .appName("trainingDataForBodyAction")
      .master("local[*]")
      .getOrCreate()
      
    import ss.implicits._
    val lines = ss.sparkContext.textFile("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult2.csv")
    val data = lines.map(resultDataMapper).toDS().cache()
    
    //Step1: Assemble multiple features into one feature vector
    val dataAssembler = new VectorAssembler()
        .setInputCols(Array("xAcce", "yAcce", "zAccr", "moving", "accel", "azimuth", "pitch", "roll"))
        .setOutputCol("bodyActionFeatures")
        
    //Step2: Index label bodyAction from String to indexed number
    val labelIndexer = new StringIndexer()
        .setInputCol("bodyAction")
        .setOutputCol("indexedBodyAction")
        .fit(data)  
    
    //Step3: Define and configure a decision tree
    val decTree = new DecisionTreeClassifier()
        .setFeaturesCol("bodyActionFeatures")
        .setLabelCol("indexedBodyAction")
        .setMaxBins(20)
        .setPredictionCol("indexedBodyActionPredic")
        
    //Step4: Convert indexed label back from indexed number to String
    val labelConverter = new IndexToString()
        .setInputCol("indexedBodyActionPredic")
        .setOutputCol("bodyActionPredic")
        .setLabels(labelIndexer.labels)
        
    val pipeline = new Pipeline()
                  .setStages(Array(dataAssembler, labelIndexer, decTree, labelConverter))
                  
    val model = pipeline.fit(data)
   
    val predictions = model.transform(data)
    
    predictions.select("bodyActionPredic", "bodyAction").show(10)
        
    val evaluator = new MulticlassClassificationEvaluator()
                    .setLabelCol("indexedBodyAction")
                    .setPredictionCol("indexedBodyActionPredic")
                    .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println(s"Test Error = ${(1.0 - accuracy)}")
    
    val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
    println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
    
    model.write.overwrite().save("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\bodyActionModel")
    ss.stop()
  }
}