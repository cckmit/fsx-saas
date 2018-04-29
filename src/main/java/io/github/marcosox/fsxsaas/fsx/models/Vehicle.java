package io.github.marcosox.fsxsaas.fsx.models;

import flightsim.simconnect.data.LatLonAlt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Vehicle {
	private int id;
	private String title;
	private String atcState;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal groundSpeed;
	private BigDecimal heading;
	private BigDecimal rudder;
	private BigDecimal throttle;
	private BigDecimal windSpeed;
	private BigDecimal windDirection;
	private BigDecimal visibility;
	private BigDecimal ambientTemperature;
	private BigDecimal ambientPressure;
	private BigDecimal barometerPressure;

	public Vehicle(int id, HashMap<String, Object> map) {
		this.id = id;
		this.title = (String) map.getOrDefault("TITLE", "");
		this.atcState = (String) map.getOrDefault("AI TRAFFIC STATE", "");
		this.latitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).latitude);
		this.longitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).longitude);
		this.groundSpeed = new BigDecimal((double) map.getOrDefault("GROUND VELOCITY", 0.0));
		this.heading = new BigDecimal((double) map.getOrDefault("PLANE HEADING DEGREES TRUE", 0.0));
		this.rudder = new BigDecimal((double) map.getOrDefault("RUDDER POSITION", 0.0));
		this.throttle = new BigDecimal((double) map.getOrDefault("GENERAL ENG THROTTLE LEVER POSITION:1", 0.0));
		this.windSpeed = new BigDecimal((double) map.getOrDefault("AMBIENT WIND VELOCITY", 0.0));
		this.windDirection = new BigDecimal((double) map.getOrDefault("AMBIENT WIND DIRECTION", 0.0));
		this.ambientTemperature = new BigDecimal((double) map.getOrDefault("AMBIENT TEMPERATURE", 0.0));
		this.ambientPressure = new BigDecimal((double) map.getOrDefault("AMBIENT PRESSURE", 0.0));
		this.barometerPressure = new BigDecimal((double) map.getOrDefault("BAROMETER PRESSURE", 0.0));
		this.visibility = new BigDecimal((double) map.getOrDefault("AMBIENT VISIBILITY", 0.0));
	}

	public BigDecimal getRudder() {
		return rudder;
	}

	public BigDecimal getThrottle() {
		return throttle;
	}

	public int getId() {
		return id;
	}

	public BigDecimal getLatitude() {
		return latitude.setScale(9, RoundingMode.DOWN);
	}

	public BigDecimal getLongitude() {
		return longitude.setScale(9, RoundingMode.DOWN);
	}

	public BigDecimal getGroundSpeed() {
		return groundSpeed.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getHeading() {
		return heading.setScale(3, RoundingMode.DOWN);
	}

	public String getTitle() {
		return title;
	}

	public String getAtcState() {
		return atcState;
	}

	public BigDecimal getWindSpeed() {
		return windSpeed.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getWindDirection() {
		return windDirection.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getVisibility() {
		return visibility.setScale(0, RoundingMode.DOWN);
	}

	public BigDecimal getAmbientTemperature() {
		return ambientTemperature.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getAmbientPressure() {
		return ambientPressure.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal getBarometerPressure() {
		return barometerPressure.setScale(2, RoundingMode.DOWN);
	}
}
