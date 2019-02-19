// This class implements the process described at this website:
// http://www.stargazing.net/kepler/altaz.html
// and prints out the necessary altitude and azimuth needed to view
// a specific Right Ascension and Declination in the sky from
// a latitude and longitude

import java.util.*;
import java.time.*;

public class TelescopeAltAzCalc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Enter Right Ascension, Declination, Latitude, Longitude
		// Ensure all numbers are in decimal format
		calcAltAz(0,0,0,0);

	}
	
	public static void calcAltAz(double ra, double dec, double lat, double lon) {
		// Step 1: Get the days since the Epoch
		//946728000 is turn of the century from the epoch
		double epochToCent = 946728000;  
		double epoch = System.currentTimeMillis()/1000;
		double secSinceCent = epoch - epochToCent;				// Seconds since J2000
		double daySinceCent = secSinceCent/60/60/24;				// Days since J2000
		//System.out.print("Days Since Century: "); System.out.println(daySinceCent);
		
		// Step 2: Get the LST
		double LST = calcLST(daySinceCent,lon);
		//System.out.print("LST: "); System.out.println(LST);
		
		// Step 3: Get Hour Angle
		double ha = LST - ra;
		if(ha < 0) ha += 360;
		//System.out.print("HA: "); System.out.println(ha);
		
		// Step 4: Get sin(Alt) and calculate Altitude.  Need to convert degree values to radians.
		double alt = calcAlt(ha,dec,lat);
		
		// Optional Step 5: Calculate Azimuth
		double az = getAz(alt,dec,lat);
		if(!(Math.sin(Math.toRadians(ha)) < 0)) az = 360 - az;
		
		System.out.println("Altidude and Azimuth necessary to view sky at RA: " + Double.toString(ra) + " and Dec: " + Double.toString(dec));
		System.out.print("Altitude: "); System.out.println(alt);
		System.out.print("Azimuth: "); System.out.println(az);
	}
	
	public static double calcAlt(double ha, double dec, double lat) {
//
// sin(ALT) = sin(DEC)*sin(LAT)+cos(DEC)*cos(LAT)*cos(HA)
//
		double haRad = Math.toRadians(ha);
		double decRad = Math.toRadians(dec);
		double latRad = Math.toRadians(lat);
		
		double sinAlt = Math.sin(decRad) * Math.sin(latRad) + Math.cos(decRad) * Math.cos(latRad) * Math.cos(haRad);
		
		return Math.toDegrees(Math.asin(sinAlt)); 
	}
	
	public static double calcLST(double daySinceCent, double lon) {
		ZoneId timeZoneID = ZoneId.of("GMT");
		LocalTime now = LocalTime.now(timeZoneID);
		double ut = now.getHour() + now.getMinute()/60.0;
		double lst = 100.46 + 0.985647*daySinceCent + lon + 15*ut;
		if(lst < 0) lst += 360;   // Gives positive value in case negative		
		return lst;
	}
	
	public static double getAz(double alt, double dec, double lat) {
//                 sin(DEC) - sin(ALT)*sin(LAT)
// cos(Az)   =   ---------------------------------
//                       cos(ALT)*cos(LAT)
		
		alt = Math.toRadians(alt);
		dec = Math.toRadians(dec);
		lat = Math.toRadians(lat);
		
		double cos = (Math.sin(dec) - Math.sin(alt) * Math.sin(lat))/(Math.cos(alt) * Math.cos(lat));
		//System.out.print("  cos(radians) = "); System.out.print(Math.acos(cos)); 
		return Math.toDegrees(Math.acos(cos));
	}
}
