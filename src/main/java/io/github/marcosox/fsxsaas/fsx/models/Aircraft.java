package io.github.marcosox.fsxsaas.fsx.models;

import flightsim.simconnect.data.LatLonAlt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Aircraft {
	private int id;
	private String title;
	private String atcType;
	private String atcModel;
	private String atcID;
	private String atcAirline;
	private String atcFlightNumber;
	private int atcHeavy;
	private String atcState;
	private String from;
	private String to;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal altitude;
	private BigDecimal altAgl;
	private int onGround;
	private BigDecimal airspeed;
	private BigDecimal groundSpeed;
	private BigDecimal verticalSpeed;
	private BigDecimal pitch;
	private BigDecimal bank;
	private BigDecimal heading;
	private BigDecimal aileron;
	private BigDecimal elevator;
	private BigDecimal rudder;
	private BigDecimal throttle;
	private String transponder;
	private BigDecimal windSpeed;
	private BigDecimal windDirection;
	private BigDecimal visibility;
	private BigDecimal ambientTemperature;
	private BigDecimal ambientPressure;
	private BigDecimal barometerPressure;

	public Aircraft(int id, HashMap<String, Object> map) {
		this.id = id;
		this.title = (String) map.getOrDefault("TITLE", "");
		this.atcType = (String) map.getOrDefault("ATC TYPE", "");
		this.atcModel = (String) map.getOrDefault("ATC MODEL", "");
		this.atcID = (String) map.getOrDefault("ATC ID", "");
		this.atcAirline = (String) map.getOrDefault("ATC AIRLINE", "");
		this.atcFlightNumber = (String) map.getOrDefault("ATC FLIGHT NUMBER", "");
		this.atcHeavy = (int) map.getOrDefault("ATC HEAVY", 0);
		this.atcState = (String) map.getOrDefault("AI TRAFFIC STATE", "");
		this.from = (String) map.getOrDefault("AI TRAFFIC FROMAIRPORT", "");
		this.to = (String) map.getOrDefault("AI TRAFFIC TOAIRPORT", "");
		this.to = (String) map.getOrDefault("AI TRAFFIC TOAIRPORT", "");
		StringBuilder transponderStr = new StringBuilder(String.valueOf(map.getOrDefault("TRANSPONDER CODE:1", "")));
		while (transponderStr.length() < 4) {
			transponderStr.insert(0, "0");    // leftpad(map.getOrDefault(...), 4, "0");
		}
		this.transponder = transponderStr.toString();
		this.latitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).latitude);
		this.longitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).longitude);
		this.altitude = new BigDecimal(((LatLonAlt) map.getOrDefault("STRUCT LATLONALT", new LatLonAlt(0, 0, 0))).altitude);
		this.airspeed = new BigDecimal((double) map.getOrDefault("AIRSPEED TRUE", 0.0));
		this.verticalSpeed = new BigDecimal((double) map.getOrDefault("VERTICAL SPEED", 0.0));
		this.groundSpeed = new BigDecimal((double) map.getOrDefault("GROUND VELOCITY", 0.0));
		this.pitch = new BigDecimal((double) map.getOrDefault("PLANE PITCH DEGREES", 0.0));
		this.bank = new BigDecimal((double) map.getOrDefault("PLANE BANK DEGREES", 0.0));
		this.heading = new BigDecimal((double) map.getOrDefault("PLANE HEADING DEGREES TRUE", 0.0));
		this.altAgl = new BigDecimal((double) map.getOrDefault("PLANE ALT ABOVE GROUND", 0.0));
		this.onGround = (int) map.getOrDefault("SIM ON GROUND", 0);
		this.aileron = new BigDecimal((double) map.getOrDefault("AILERON POSITION", 0.0));
		this.elevator = new BigDecimal((double) map.getOrDefault("ELEVATOR POSITION", 0.0));
		this.rudder = new BigDecimal((double) map.getOrDefault("RUDDER POSITION", 0.0));
		this.throttle = new BigDecimal((double) map.getOrDefault("GENERAL ENG THROTTLE LEVER POSITION:1", 0.0));
		this.windSpeed = new BigDecimal((double) map.getOrDefault("AMBIENT WIND VELOCITY", 0.0));
		this.windDirection = new BigDecimal((double) map.getOrDefault("AMBIENT WIND DIRECTION", 0.0));
		this.ambientTemperature = new BigDecimal((double) map.getOrDefault("AMBIENT TEMPERATURE", 0.0));
		this.ambientPressure = new BigDecimal((double) map.getOrDefault("AMBIENT PRESSURE", 0.0));
		this.barometerPressure = new BigDecimal((double) map.getOrDefault("BAROMETER PRESSURE", 0.0));
		this.visibility = new BigDecimal((double) map.getOrDefault("AMBIENT VISIBILITY", 0.0));
	}

	public BigDecimal getAileron() {
		return aileron;
	}

	public BigDecimal getElevator() {
		return elevator;
	}

	public BigDecimal getRudder() {
		return rudder;
	}

	public BigDecimal getThrottle() {
		return throttle;
	}

	public String getAtcType() {
		return atcType;
	}

	public String getAtcModel() {
		return atcModel;
	}

	public String getAtcID() {
		return atcID;
	}

	public String getAtcAirline() {
		return atcAirline;
	}

	public String getAtcFlightNumber() {
		return atcFlightNumber;
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

	public BigDecimal getPitch() {
		return pitch.setScale(3, RoundingMode.DOWN);
	}

	public BigDecimal getBank() {
		return bank.setScale(3, RoundingMode.DOWN);
	}

	public BigDecimal getHeading() {
		return heading.setScale(3, RoundingMode.DOWN);
	}

	public String getTitle() {
		return title;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public int getAtcHeavy() {
		return atcHeavy;
	}

	public String getAtcState() {
		return atcState;
	}

	public String getTransponder() {
		return transponder;
	}

	public BigDecimal getVerticalSpeed() {
		return verticalSpeed.setScale(3, RoundingMode.DOWN);
	}

	public int getOnGround() {
		return onGround;
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
