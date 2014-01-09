import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class SQL {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private double radius = 500;
	private double myPosLat;
	private double myPosLong;
	private double centrumLat;
	private double centrumLong;
	private ArrayList<Double> latValues;
	private ArrayList<Double> longValues;

//	private static final double MIN_LAT = Math.toRadians(-90d); // -PI/2
//	private static final double MAX_LAT = Math.toRadians(90d); // PI/2
//	private static final double MIN_LON = Math.toRadians(-180d); // -PI
//	private static final double MAX_LON = Math.toRadians(180d); // PI
	
	static double DEG_TO_RAD = 0.017453292519943295769236907684886;
	static double EARTH_RADIUS_IN_KM = 6371;

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/test?"
							+ "user=root&password=");

			// Statements allow to issue SQL queries to the database

			preparedStatement = connect
					.prepareStatement("SELECT* from geolocations");
			resultSet = preparedStatement.executeQuery();

			latValues = new ArrayList<>();
			longValues = new ArrayList<>();

			while (resultSet.next()) {
				longValues.add(resultSet.getDouble(4));
				latValues.add(resultSet.getDouble(3));
				// System.out.println(resultSet.getString(4));

			}
			System.out.println(longValues);
			System.out.println(latValues);
			myPosLat = latValues.get(1);
			myPosLong = longValues.get(1);

			centrumLat = latValues.get(0);
			centrumLong = longValues.get(0);

			calcDistance();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	public double calcDistance() {
		System.out.println(centrumLat);
		System.out.println(centrumLong);
		
		double dLat = (myPosLat - centrumLat) * DEG_TO_RAD;
		double dLon = (myPosLong - centrumLong) * DEG_TO_RAD;
		double latMyPos = myPosLat * DEG_TO_RAD;
		double latCentrum = centrumLat * DEG_TO_RAD;

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(latMyPos) * Math.cos(latCentrum); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = EARTH_RADIUS_IN_KM * c;
		
		System.out.println("Distanz : " + d);
		
		double inRadius = radius - d;
		System.out.println(inRadius);
		return d;
		
		
//		double distance = Math.acos(Math.sin(myPosLat) * Math.sin(centrumLat)
//				+ Math.cos(myPosLat) * Math.cos(centrumLat)
//				* Math.cos(myPosLong - centrumLong))
//				* radius;
//
//		System.out.println(distance);

		//isInRange(distance);
	}
	

//	public void isInRange(double distance) {
//		if (radius < 0d || distance < 0d)
//			System.out.println("möp");
//
//		double radDist = distance / radius;
//
//		double minLat = myPosLat - radDist;
//		double maxLat = myPosLat + radDist;
//
//		double minLon, maxLon;
//		if (minLat > MIN_LAT && maxLat < MAX_LAT) {
//			double deltaLon = Math.asin(Math.sin(radDist) / Math.cos(myPosLat));
//			minLon = myPosLong - deltaLon;
//			if (minLon < MIN_LON)
//				minLon += 2d * Math.PI;
//			maxLon = myPosLong + deltaLon;
//			if (maxLon > MAX_LON)
//				maxLon -= 2d * Math.PI;
//		} else {
//			// a pole is within the distance
//			minLat = Math.max(minLat, MIN_LAT);
//			maxLat = Math.min(maxLat, MAX_LAT);
//			minLon = MIN_LON;
//			maxLon = MAX_LON;
//
//			System.out.println("Ist drin");
//		}
//
//	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

}
