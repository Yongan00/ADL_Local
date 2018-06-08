package edu.isu.datafilter

case class DataFormat (
    light: Float, 
    xAcce: Float,
    yAcce: Float,
    zAccr: Float,
    angle: Float,
    azimuth: Float,
    pitch: Float,
    roll: Float,
    latitude: Float,
    longitude: Float,
    altitude: Float,
    hour: Float,
    moving: Float,
    turning: Float,
    lightChanging: Float,
    dark: Float,
    accel: Float,
    status: Float,
    screenOn: Float,
    earPlug: Float,
    //proximityDistance: Float
    actionLabel: String, //{ '711','BarberShop','BasementBuffet','Bathroom','BathroomBowel','BathroomPee','Bed','Bus','Bus2Zhongli','BusStation','Danshui','Discovery','DrChangOffice','Gaotie','Jiugong','JiugongJia','Lab','Lamian','Luwei','MingyueTangbao','Restaurant','RestaurantYongtai','SFOairport','SameAsBefore','Sleep','TAITrain','TRAINSTATION','Train','USplane','WalkCampus','WalkDept','WalkHotel','WalkOffCampus','WorkingOnPC','Zhongshanlou','aldi','artsuniversity','artsuniversity.','backames','buffet','building','cfresh','desmoineDriving','discovery','driving','hndAirport','jiugong','liangyi','liangyiHouse','meeting','plane','redwoodcity','sfo2ord','songshan','theater'}
    sound: Float
   )