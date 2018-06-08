package edu.isu.datafilter

import org.apache.spark.SparkContext
import org.apache.log4j._
import org.apache.spark._
import org.apache.spark.sql._

object DataML {
  
  def resultDataMapper(line: String): (DataFormat) = {
    val attr = line.split(',')
    return DataFormat(attr(0).toFloat, 
                      attr(1).toFloat,
                      attr(2).toFloat,
                      attr(3).toFloat,
                      attr(4).toFloat,
                      attr(5).toFloat,
                      attr(6).toFloat,
                      attr(7).toFloat,
                      attr(8).toFloat,
                      attr(9).toFloat,
                      attr(10).toFloat,
                      attr(11).toFloat,
                      attr(12).toFloat,
                      attr(13).toFloat,
                      attr(14).toFloat,
                      attr(15).toFloat,
                      attr(16).toFloat,
                      attr(17).toFloat,
                      attr(18).toFloat,
                      attr(19).toFloat,
                      attr(20),0)
  }
  
  def main(args: Array[String]){
    
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val ss = SparkSession
      .builder()
      .appName("motionFilter")
      .master("local[*]")
      .getOrCreate()      
    
    val data = ss.sparkContext.textFile("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\dataResult.csv").map(resultDataMapper)
    data.foreach(println)  

    
    ss.stop()
  }
}