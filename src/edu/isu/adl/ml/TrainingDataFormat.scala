package edu.isu.adl.ml

case class TrainingDataFormat(
    light: Double, 
    xAcce: Double,
    yAcce: Double,
    zAccr: Double,
    angle: Double,
    azimuth: Double,
    pitch: Double,
    roll: Double,
    latitude: Double,
    longitude: Double,
    altitude: Double,
    hour: Double,
    moving: Double,
    turning: Double,
    lightChanging: Double,
    dark: Double,
    accel: Double,
    chargingStatus: Double,
    screenOn: Double,
    earPlug: Double,
    //proximityDistance: Float
    actionLabel: String, //{ '711','BarberShop','BasementBuffet','Bathroom','BathroomBowel','BathroomPee','Bed','Bus','Bus2Zhongli','BusStation','Danshui','Discovery','DrChangOffice','Gaotie','Jiugong','JiugongJia','Lab','Lamian','Luwei','MingyueTangbao','Restaurant','RestaurantYongtai','SFOairport','SameAsBefore','Sleep','TAITrain','TRAINSTATION','Train','USplane','WalkCampus','WalkDept','WalkHotel','WalkOffCampus','WorkingOnPC','Zhongshanlou','aldi','artsuniversity','artsuniversity.','backames','buffet','building','cfresh','desmoineDriving','discovery','driving','hndAirport','jiugong','liangyi','liangyiHouse','meeting','plane','redwoodcity','sfo2ord','songshan','theater'}
    //sound: Float,
    
    //Label type
    workOrSleep: String,
    bodyAction: String
)
  
