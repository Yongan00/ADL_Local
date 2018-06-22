package edu.isu.adl.scrip

import java.io._
import scala.io._
import scala.collection.JavaConverters._
import edu.isu.datafilter._
import javax.json._;

//The function of this scrip is filtering the labels that we need: workOrSleep and bodyAction
//from the result of TrainingDataWriter.scala
object TrainingDataWriter2 {
  def main(args: Array[String]){
      val writer = new FileWriter("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult2.csv", true)
      
      var currentBodyAction: String = null
      val lines = Source.fromFile("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult.csv").getLines()
      for(line <- lines){
        val attr = line.split(',')
        val labels = attr(22).split('_')
        
        def findWorkOrSleep(labels: List[String]): String = {
          if(labels.contains("Sleeping"))
              return "sleeping"
          else if(labels.contains("Working"))
              return "working"
          else return "others"
        }
        
        def findBodyAction(labels: List[String]): String = {
          val standing = "standing"
          val sitting = "sitting"
          //val walking = "walking"
          val lying = "lying"
          if(labels.contains("Standing")){
            currentBodyAction = standing
            return standing
          }
          else if(labels.contains("Sitting")){
              currentBodyAction = sitting
              return sitting
          }
          else if(labels.contains("Lying")){
              currentBodyAction = lying
              return lying
          }
          else return currentBodyAction
        }
        
        val workOrSleep = findWorkOrSleep(labels.toList)
        val bodyAction = findBodyAction(labels.toList)
        
        writer.write(attr(0) + "," +
                     attr(1) + "," +
                     attr(2) + "," +
                     attr(3) + "," +
                     attr(4) + "," +
                     attr(5) + "," +
                     attr(6) + "," +
                     attr(7) + "," +
                     attr(8) + "," +
                     attr(9) + "," +
                     attr(10) + "," +
                     attr(11) + "," + 
                     attr(12) + "," +
                     attr(13) + "," +
                     attr(14) + "," +
                     attr(15) + "," +
                     attr(16) + "," +
                     attr(17) + "," +
                     attr(18) + "," +
                     attr(19) + "," +
                     attr(20) + "," +
                     attr(21) + "," + 
                     workOrSleep + "," +
                     bodyAction + '\n')
      }
      writer.flush()
      writer.close()
    }
}