import android.location.Location;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SQL sql = new SQL();
		try {
			//sql.readDataBase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Location origin = new Location("");
		origin.setLatitude(53.553363);
		origin.setLongitude(9.997559);
		
		Location dst = new Location("");
		dst.setLatitude(48.396385);
		dst.setLongitude(9.981079);
		float d = origin.distanceTo(dst);
		System.out.println(d);
	}

}
 