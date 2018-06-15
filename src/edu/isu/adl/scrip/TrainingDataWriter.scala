package edu.isu.adl.scrip

import java.io._
import scala.io._
import scala.collection.JavaConverters._
import edu.isu.datafilter._
import javax.json._;

object TrainingDataWriter {
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
      
      //get label
      val is: InputStream = new FileInputStream(new File("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\model\\PhilSamsang.json"));
			val jrd: JsonReader  = Json.createReader(is);
			val ja:JsonArray = jrd.readArray();

			def findLabels(ja: JsonArray): List[String] = {
  			for(adlObject:JsonObject <- ja.getValuesAs(classOf[JsonObject]).asScala.toList) {
  			  val date = adlObject.getString("time").split('-')(0)
  			  val time = adlObject.getString("time").split('-')(1)
  			  if(date.equals(timeStamp.split('_')(0)) && time.equals(timeStamp.split('_')(1))){
  			    val result = adlObject.getString("action").split(',').toList
  			    val resultWithoutBlank = result.filter(x => !x.equals(" "))
  			    val resultWithoutFrontBlank = resultWithoutBlank.map(x => {if(x.startsWith(" ")) x.substring(1) else x})
  			    return resultWithoutFrontBlank
  			  }
  				//System.out.println(adlObject.getString("time"));
  				//System.out.println(adlObject.getString("action"));
  			}
  			return null
			}
			val labels: List[String] = findLabels(ja)
			//println(labels)
			
      //write to local file
      val writer = new FileWriter("C:\\Users\\yongan\\CCLearning\\CC\\RecognitionOfADL\\data\\PhilAmes20160706_20160829\\PhilSamsang20160706_20160803\\myTrainingModel\\trainingResult.csv", true)
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
                  username + "_" + timeStamp + ",")
                  
       for(label: String <- labels)
         writer.write(label + "_")
       writer.write('\n')
       writer.close()
    }
}