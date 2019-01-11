package com.xie.server.a.constant;

public enum SiteDistanceMapping {

    ONE_KM(1000,6),
    FIVE_KM(5000,5),
    TWENTY_KM(20000,4),
    ONE_HUNDRED_FIFTY_KM(150000,3);
    private long distance;

    private int level;

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
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

   public static SiteDistanceMapping transform(long distance){
      if(distance<=ONE_KM.getDistance()){
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
