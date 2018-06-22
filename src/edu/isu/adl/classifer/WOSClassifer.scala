package edu.isu.adl.classifer

import java.util.Properties
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.classification.DecisionTreeClassificationModel

object WOSClassifer {
  def main(args: Array[String]){
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val url = "jdbc:mysql://localhost:3306/adl?useSSL=false"
    val tableName = "adl.tmpdata"
    val dbUsername = "adlDev_yongan"
    val dbPassword = "2375033"
    
    val ss = SparkSession
            .builder()
            .appName("WOSClassifer")
            .master("local[*]")
            .getOrCreate()
    val connectionProperties = new Properties()
    connectionProperties.setProperty("user", dbUsername)
    connectionProperties.setProperty("password", dbPassword)
    val jdbcDF = ss.read.jdbc(url, tableName, connectionProperties)
    //jdbcDF.printSchema()
    
    //load trained model from disk
    val model = PipelineModel.load("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\workOrSleepModel")
    //val model = PipelineModel.load("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\bodyActionModel")
    //val treeModel = model.stages(4).asInstanceOf[DecisionTreeClassificationModel]
    //println(s"Learned classification tree model:\n ${treeModel.toDebugString}")
    val result = model.transform(jdbcDF)
    //result.printSchema()
    //result.select("id", "username", "timeStamp", "workOrSleepFeatures", "probability", "predictionLabel").show(20)
    result.select("username", "timeStamp", "predictionLabel")
          .write.csv("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\report2")
    ss.close()
  }
}