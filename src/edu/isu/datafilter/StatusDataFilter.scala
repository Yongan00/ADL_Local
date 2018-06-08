package edu.isu.datafilter

import scala.io.Source

class StatusDataFilter(filePath: String) {
  
  private val lines = Source.fromFile(filePath).getLines()
  private val firstLine = lines.next()
  private var batLine: String = null
  private var apLine: String = null
  
  while(lines.hasNext){
    val line = lines.next()
    if(line.startsWith("bat"))
      batLine = line
    if(line.startsWith("ap"))
      apLine = line
  }
  
  //get light, xAcce, yAcce, zAccr, angle, azimuth, pitch, roll, latitude, longitude, altitude
  def extractBasicStatus() 
  : (Float, Float, Float, Float, Float, Float, Float, Float, Float, Float, Float) = 
  {
    val attr = firstLine.split(',')
    //val light = attr(0).split('.').apply(0) + "." + attr(0).split('.').apply(1).substring(0, 2)
    return (attr(0).toFloat, attr(1).toFloat, attr(2).toFloat, attr(3).toFloat, attr(4).toFloat, attr(15).toFloat, attr(16).toFloat, attr(17).toFloat, attr(9).toFloat, attr(10).toFloat, attr(11).toFloat)
  }
  
  def extractActionLabel()
  : (String) = 
  {
    return firstLine.split(',')(12).trim()
  }
  
  //get status, screenOn, earPlug
  def extractBatStatus()
  : (Float, Float, Float) = 
  {
    var defaultBat = Array[Float](0, 0, 0)
    if(batLine != null){
      val batAttr = batLine.split(',')
      if(batAttr.length > 10){
        defaultBat(0) = batAttr(1).toFloat
        defaultBat(1) = batAttr(10).trim().toFloat
      }
      if(batAttr.length > 12)
        defaultBat(2) = batAttr(11).trim().toFloat
      }
    return (defaultBat(0), defaultBat(1), defaultBat(2))
  }
}