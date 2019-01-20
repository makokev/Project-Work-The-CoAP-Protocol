package it.unibo.radar.coap;

public class RadarPoint{
	
	private final int distance;
	private final int angle;
	
	public static RadarPoint convertFromString(String stringPoint){
		int distance = Integer.parseInt(stringPoint.split(",")[0]);
		int angle = Integer.parseInt(stringPoint.split(",")[1]);		
		try{
			return new RadarPoint(distance, angle);
		}catch(Exception e){
			return null; // Return null if point is invalid
		}
	}
	
	public RadarPoint(){
		this(0,0);
	}
	
	public RadarPoint(int distance, int angle){
		if(distance > 90 || distance <0 || angle > 360 || angle < 0)
			throw new IllegalArgumentException("Distance or angle out of range (distance [0, 90], angle [0, 360]).");
		this.distance = distance;
		this.angle = angle;
	}
	
	public int getDistance(){
		return distance;
	}
	
	public int getAngle(){
		return angle;
	}
	
	public String compactToString(){
		return this.distance+","+this.angle;
	}
	
	@Override
	public String toString(){
		return "("+this.distance+", "+this.angle+")";
	}
		
}

