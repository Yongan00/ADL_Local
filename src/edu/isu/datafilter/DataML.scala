package edu.isu.datafilter

import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.mllib.linalg._
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.Pipeline

object DataML {
  
  def resultDataMapper(line: String): (DataFormat) = {
    val attr = line.split(',')
    val label = attr(22).split('_')
    return DataFormat(attr(0).toDouble, 
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
                      attr(20), label.toList)
  }
  
  def main(args: Array[String]){
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val ss = SparkSession
      .builder()
      .appName("motionFilter")
      .master("local[*]")
      .getOrCreate()      
      
    import ss.implicits._
    val lines = ss.sparkContext.textFile("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult.csv")
    val data = lines.map(resultDataMapper).toDS()
    
    //println("Here is our inferred schema:")
    //data.printSchema()
    
    //println("Let's select the label column:")
    //data.select("label").show()
    
    //prepare raw data
    //Index ActionLabel(String) to number
    val actionLabelIndexer = new StringIndexer()
        .setInputCol("actionLabel")
        .setOutputCol("indexedActionLabel")
        .fit(data)
    val dataWithAllNum = actionLabelIndexer.transform(data)
    dataWithAllNum.show()
    
    //Assemble multiple columns to one column as feature
    val dataAssembler = new VectorAssembler()
        .setInputCols(Array("light", "xAcce", "yAcce", "zAccr", "angle", "azimuth", "pitch", "roll", "latitude", "longitude", "altitude", "hour", "moving", "turning", "lightChanging", "dark", "accel", "status", "screenOn", "earPlug", "indexedActionLabel"))
        .setOutputCol("features")
    val dataAssemble = dataAssembler.transform(dataWithAllNum)
    //dataAssemble.select("features").show(50)
    
    //val Array(trainingData, testData) = dataAssemble.randomSplit(Array(0.7, 0.3))
    
    //pre-process the data and configure the decision tree
    val featureIndexer = new VectorIndexer()
        .setInputCol("features")
        .setOutputCol("indexedFeatures")
        .setMaxCategories(3)
        .fit(dataAssemble)
        
    val dataIndexed = featureIndexer.transform(dataAssemble)
    //dataIndexed.select("indexedFeatures", "features").show(20)
//    val labelIndexer = new StringIndexer()
//        .setInputCol("labels")
//        .setOutputCol("indexedLabels")
//        .fit(dataProcessed)
//        
//    val decTree = new DecisionTreeClassifier()
//        .setFeaturesCol("indexedFeatures")
//        .setLabelCol("indexedLabels")
//        
//    val labelConverter = new IndexToString()
//        .setInputCol("prediction")
//        .setOutputCol("predictionLabel")
//        .setLabels(labelIndexer.labels)
//        
//    //train model and test     
//    val pipeline = new Pipeline()
//        .setStages(Array(labelIndexer, featureIndexer, decTree, labelConverter))
//    
//    val model = pipeline.fit(trainingData)
//    
//    val predictions = model.transform(testData)
//    
//    predictions.select("predictionLabel", "label", "feature").show(20)
        
        
    
    
    ss.stop()
  }
}