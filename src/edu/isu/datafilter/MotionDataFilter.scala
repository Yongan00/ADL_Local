package edu.isu.datafilter

import scala.io.Source

object MotionDataFilter {
  def extractMotionData(filePath: String) : (Float, Float, Float, Float, Float) = {
    
    var stepList = List[Float]()
    var orientList = List[Float]()
    var lightList = List[Float]()
    var accelList = List[Float]()
    
    val lines = Source.fromFile(filePath).getLines()
    for(line <- lines){
      val attr = line.split(',')
      if(attr(0) == "S")
        stepList = stepList ++ List(attr(1).toFloat)
      else if(attr(0) == "O")
        orientList = orientList ++ List(attr(1).toFloat)
      else if(attr(0) == "L")
        lightList = lightList ++ List(attr(1).toFloat)
      else if(attr(0) == "A")
        accelList = accelList ++ List(attr(1).toFloat)
    }
    
    val stepListI = stepList
    val orientListI = orientList
    val lightListI = lightList
    val accelListI = accelList
    
    def extractMoving : Float = {
      if(stepListI.length > 1 && stepListI(stepListI.length - 1) - stepListI(0) > 3){
        return stepListI(stepListI.length - 1) - stepListI(0)
      }else{
        return 0
      }
    }
    
    def extractTurning: Float = {
      if(orientListI.length > 1 && orientListI.max - orientListI.min > 20)
        return orientListI.max - orientListI.min
      else
        return 0
    }
    
    def extractLightChanging: Float = {
      if(lightListI.length >1)
        return lightListI.max - lightListI.min
      else
        return 0
    }
    
    def extractDark: Float = {
      if(lightListI.length > 1 && lightListI.max < 10)
        return lightListI.max
      else
        return 0
    }
    
    def extractAccel: Float = {
      if(accelListI.length > 1 && accelListI.max - accelListI.min > 2)
        return accelListI.length
      else
        return 0
    }
      
    return (extractMoving, extractTurning, extractLightChanging, extractDark, extractAccel)
  }
}