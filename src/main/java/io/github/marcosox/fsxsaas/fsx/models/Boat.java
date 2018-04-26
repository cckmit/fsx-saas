package io.github.marcosox.fsxsaas.fsx.models;

import flightsim.simconnect.data.LatLonAlt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Boat {
	private int id;
	private String title;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal altitude;
	private BigDecimal altAgl;
	private BigDecimal airspeed;
	private BigDecimal groundSpeed;
	private BigDecimal bank;
	private BigDecimal heading;
	private BigDecimal rudder;
	private BigDecimal throttle;

//	public Boat(int id, String title, InitPosition ip, double groundSpeed, double altAgl, double rudder, double throttle) {
//		this.id = id;
//		this.title = title;
//		this.latitude = ip.latitude;
//		this.longitude = ip.longitude;
//		this.altitude = ip.altitude;
//		this.airspeed = ip.airspeed;
//		this.groundSpeed = groundSpeed;
//		this.bank = ip.bank;
//		this.heading = ip.heading;
//		this.altAgl = altAgl;
//		this.rudder = rudder;
//		this.throttle = throttle;
//	}

	public Boat(int id, HashMap<String, Object> map) {
		this.id = id;
		this.title = (String) map.getOrDefault("TITLE", "");
		this.latitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).latitude);
		this.longitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).longitude);
		this.altitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).altitude);
		this.airspeed = new BigDecimal((double) map.getOrDefault("AIRSPEED TRUE", 0.0));
		this.groundSpeed = new BigDecimal((double) map.getOrDefault("GROUND VELOCITY", 0.0));
		this.altAgl = new BigDecimal((double) map.getOrDefault("PLANE ALT ABOVE GROUND", 0.0));
		this.bank = new BigDecimal((double) map.getOrDefault("PLANE BANK DEGREES", 0.0));
		this.heading = new BigDecimal((double) map.getOrDefault("PLANE HEADING DEGREES TRUE", 0.0));
		this.rudder = new BigDecimal((double) map.getOrDefault("RUDDER POSITION", 0.0));
		this.throttle = new BigDecimal((double) map.getOrDefault("GENERAL ENG THROTTLE LEVER POSITION:1", 0.0));
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public BigDecimal getLatitude() {
		return latitude.setScale(9, RoundingMode.DOWN);
	}

	public BigDecimal getLongitude() {
		return longitude.setScale(9, RoundingMode.DOWN);
	}

	public BigDecimal getAltitude() {
		return altitude.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getAltAgl() {
		return altAgl.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getAirspeed() {
		return airspeed.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getGroundSpeed() {
		return groundSpeed.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getBank() {
		return bank.setScale(3, RoundingMode.DOWN);
	}

	public BigDecimal getHeading() {
		return heading.setScale(3, RoundingMode.DOWN);
	}

	public BigDecimal getRudder() {
		return rudder.setScale(3, RoundingMode.DOWN);
	}

	public BigDecimal getThrottle() {
		return throttle.setScale(3, RoundingMode.DOWN);
	}
}
