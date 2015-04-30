package com.appleframework.commons.map.utility;

import org.apache.log4j.Logger;

import com.appleframework.commons.map.google.MapPointFix;

public class GpsUtility {

	private final static double PI = 3.14159265358979323; // 圆周率
	private final static double R = 6371229; // 地球的半径

	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	private static Logger logger = Logger.getLogger(GpsUtility.class);

	public static String bd_encrypt(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return bd_lat + "," + bd_lon;
	}

	public static String bd_decrypt(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return gg_lat + "," + gg_lon;
	}

	/*
	 * type 
	 * 0:google转百度,
	 * 1:google转gps,
	 * 2:百度转google,
	 * 3:百度转gps，
	 * 4：gps转google，
	 * 5：gps转百度
	 */
	public static String convert(Double lat, Double lon, int type) {
		String ret = "";
		try {
			switch (type) {
			case 0:
				ret = bd_encrypt(lat, lon);
				break;
			case 1:
				String[] pointFixed = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), false);
				ret = pointFixed[1] + "," + pointFixed[0];
				break;
			case 2:
				ret = bd_decrypt(lat, lon);
				break;
			case 3:
				String res = bd_decrypt(lat, lon);
				String arr_p[] = res.split(",");
				String[] pointFixed1 = MapPointFix.getFixedPoint(arr_p[1], arr_p[0], false);
				ret = pointFixed1[1] + "," + pointFixed1[0];
				break;
			case 4:
				String[] pointFixed2 = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), true);
				ret = pointFixed2[1] + "," + pointFixed2[0];
				break;
			case 5:
				String[] pointFixed3 = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), true);
				ret = bd_encrypt(Double.valueOf(pointFixed3[1]),Double.valueOf(pointFixed3[0]));
				break;
			}

		} catch (Exception e) {
			logger.error("error:", e);
		}

		return ret;
	}
	
	/*
	 * type 
	 * 0:google转百度,
	 * 1:google转gps,
	 * 2:百度转google,
	 * 3:百度转gps，
	 * 4：gps转google，
	 * 5：gps转百度
	 */
	public static String[] convert2(Double lat, Double lon, int type) {
		String ret = "";
		try {
			switch (type) {
			case 0:
				ret = bd_encrypt(lat, lon);
				break;
			case 1:
				String[] pointFixed = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), false);
				ret = pointFixed[1] + "," + pointFixed[0];
				break;
			case 2:
				ret = bd_decrypt(lat, lon);
				break;
			case 3:
				String res = bd_decrypt(lat, lon);
				String arr_p[] = res.split(",");
				String[] pointFixed1 = MapPointFix.getFixedPoint(arr_p[1], arr_p[0], false);
				ret = pointFixed1[1] + "," + pointFixed1[0];
				break;
			case 4:
				String[] pointFixed2 = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), true);
				ret = pointFixed2[1] + "," + pointFixed2[0];
				break;
			case 5:
				String[] pointFixed3 = MapPointFix.getFixedPoint(lon.toString(), lat.toString(), true);
				ret = bd_encrypt(Double.valueOf(pointFixed3[1]),Double.valueOf(pointFixed3[0]));
				break;
			}

		} catch (Exception e) {
			logger.error("error:", e);
		}

		return ret.split(",");
	}

	public static double getDistance(double longt1, double lat1, double longt2, double lat2) {
		double x, y, distance;
		x = (longt2 - longt1) * PI * R
				* Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		return distance;
	}
	
	public static void main(String[] args) {
		String str = GpsUtility.convert(22.540080,113.952460, 4);
		System.out.println(str);
	}

}
