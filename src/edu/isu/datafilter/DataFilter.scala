package edu.isu.datafilter

import java.io._
import scala.io._
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Timestamp
import java.sql.Connection
import java.sql.DriverManager


object DataFilter {
  
  def filterData(userName: String, timeStamp: String, motionFolder: File, statusFolder: File){
    val statusData = new StatusDataFilter(statusFolder.getAbsolutePath + "\\" + userName + '_' + timeStamp + ".csv")
    
    //get light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude
    val basicStatusData = statusData.extractBasicStatus()
    //println(basicStatusData)
    
    //get hour
    val hour = timeStamp.substring(9, 11)
    //println(hour)
    
    //get moving, turning, lightChanging, dark, accel
    val motionData = MotionDataFilter.extractMotionData(motionFolder.getAbsolutePath + "\\" + userName + '_' + timeStamp + "_motion.csv")
    //println(motionData)
    
    //get status, screenOn, earPlug
    val batStatusData = statusData.extractBatStatus()
    //println(batStatusData)
    
    //get actionLabel
    val actionLabel = statusData.extractActionLabel()
    
    //get timeStamp
    val timeFormat: DateFormat = new SimpleDateFormat("yyyyMMdd_kkmmss")
    val sqlTimeStamp:Timestamp = new Timestamp(timeFormat.parse(timeStamp).getTime)
    //println(sqlTimeStamp.toString())
    //write to local file
/*    val writer = new FileWriter("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\dataResult.csv", true)
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
     writer.close()*/
    
    
    //write to MySQL Database through JDBC
    val url = "jdbc:mysql://localhost:3306/adl?useSSL=false"
    val driver = "com.mysql.cj.jdbc.Driver"
    Class.forName(driver)
    val dbUsername = "adlDev_yongan"
    val dbPassword = "2375033"

    try{
    val connection: Connection = DriverManager.getConnection(url, dbUsername, dbPassword)
    val sql = "INSERT INTO adl.tmpdata(light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude, hour, moving, turning, lightChanging, dark, accel, chargingStatus, screenOn, earPlug, actionLabel, username, timeStamp) " +
                                      "values(" + 
                                      basicStatusData._1 + "," +
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
                                      batStatusData._3 + ",?,?,?)"
    val pStatement = connection.prepareStatement(sql)
    pStatement.setString(1, actionLabel)
    pStatement.setString(2, userName)
    pStatement.setTimestamp(3, sqlTimeStamp)
    pStatement.execute()
     connection.close()
     println("successfully insert")
    }catch{
      case e: Exception => e.printStackTrace()
    }
  }
  
  
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

}