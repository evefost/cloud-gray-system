package com.xie.server.a.constant;

public enum SiteDistanceMapping {

    ONE_KM(1,6),
    FIVE_KM(5,5),
    TWENTY_KM(20,4),
    ONE_HUNDRED_FIFTY_KM(150,3);
    private int distance;

    private int level;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private SiteDistanceMapping(int distance,int level){
        this.distance = distance;
        this.level = level;
    }

   public static SiteDistanceMapping transform(int distance){
      if(distance<=1){
          return ONE_KM;
      }else if(distance<=FIVE_KM.getDistance()){
          return FIVE_KM;
      }else if(distance<=TWENTY_KM.getDistance()){
          return TWENTY_KM;
      }else if(distance<=ONE_HUNDRED_FIFTY_KM.getDistance()){
          return ONE_HUNDRED_FIFTY_KM;
      }else {
          return FIVE_KM;
      }
   }

}
