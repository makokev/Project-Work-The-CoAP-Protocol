package it.unibo.radar;

/**
 * 
 * This class represents a point in our radar space.<br>
 * Each point is defined by polar coordinates.
 *
 */
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
	
	/**
	 * @param distance
	 * represents the distance between the point and the center of the radar. It must be in range [0,90].
	 * @param angle
	 * represents the angle between the vertical axe and the line connecting the center of the radar and the point. It must be in range [0, 360].
	 */
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
	
	@Override
	public String toString(){
		return "("+this.distance+", "+this.angle+")";
	}
		
}

