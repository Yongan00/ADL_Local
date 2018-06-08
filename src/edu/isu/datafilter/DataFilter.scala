package edu.isu.datafilter

import java.io._
import scala.io._



object DataFilter {
  def main(args: Array[String]){
      
    //load folder and set username and timeStamp
    val motionFolder = new File("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\motion")
    val statusFolder = new File("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\csv")
    val timeStamps = motionFolder.listFiles().map(x => x.getName.split('_').apply(1) + "_" + x.getName.split('_').apply(2))
    //val timeStamp = "20160706_161756"
    val username = "PhilSamsang"
    
    for(timeStamp <- timeStamps)
      filterData(username, timeStamp, motionFolder, statusFolder)
  }
  
  def filterData(username: String, timeStamp: String, motionFolder: File, statusFolder: File){
    val statusData = new StatusDataFilter(statusFolder.getAbsolutePath + "\\" + username + '_' + timeStamp + ".csv")
    
    //get light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude
    val basicStatusData = statusData.extractBasicStatus()
    //println(basicStatusData)
    
    //get hour
    val hour = timeStamp.substring(9, 11)
    //println(hour)
    
    //get moving, turning, lightChanging, dark, accel
    val motionData = MotionDataFilter.extractMotionData(motionFolder.getAbsolutePath + "\\" + username + '_' + timeStamp + "_motion.csv")
    //println(motionData)
    
    //get status, screenOn, earPlug
    val batStatusData = statusData.extractBatStatus()
    //println(batStatusData)
    
    //get actionLabel
    val actionLabel = statusData.extractActionLabel()
    
    //write to local file
    val writer = new FileWriter("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\dataResult.csv", true)
    writer.write(basicStatusData._1 + "," +
                basicStatusData._2 + "," +
                basicStatusData._3 + "," +
                basicStatusData._4 + "," +
                basicStatusData._5 + "," +
                basicStatusData._6 + "," +
                basicStatusData._7 + "," +
                basicStatusData._8 + "," +
                basicStatusData._9 + "," +
                basicStatusData._10 + "," +
                basicStatusData._11 + "," +
                hour + "," + 
                motionData._1 + "," +
                motionData._2 + "," +
                motionData._3 + "," +
                motionData._4 + "," +
                motionData._5 + "," +
                batStatusData._1 + "," +
                batStatusData._2 + "," +
                batStatusData._3 + "," +
                actionLabel + "," +
                username + "_" + timeStamp + '\n')
     writer.close()
  }

}