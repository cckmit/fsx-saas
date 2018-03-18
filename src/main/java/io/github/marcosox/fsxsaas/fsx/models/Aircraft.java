package io.github.marcosox.fsxsaas.fsx.models;

import flightsim.simconnect.data.InitPosition;

public class Aircraft {
	private int id;
	private String title;
	private String atcType;
	private String atcModel;
	private String atcID;
	private String atcAirline;
	private String atcFlightNumber;
	private String from;
	private String to;
	private double latitude;
	private double longitude;
	private double altitude;
	private double altAgl;
	private int airspeed;
	private double pitch;
	private double bank;
	private double heading;
	private double aileron;
	private double elevator;
	private double rudder;
	private double throttle;

	public Aircraft(int id, String title, String atcType, String atcModel, String atcID, String atcAirline, String atcFlightNumber, InitPosition ip, double altAgl, double aileron, double elevator, double rudder, double throttle, String from, String to) {
		this.id = id;
		this.title = title;
		this.atcType = atcType;
		this.atcModel = atcModel;
		this.atcID = atcID;
		this.atcAirline = atcAirline;
		this.atcFlightNumber = atcFlightNumber;
		this.latitude = ip.latitude;
		this.longitude = ip.longitude;
		this.altitude = ip.altitude;
		this.airspeed = ip.airspeed;
		this.pitch = ip.pitch;
		this.bank = ip.bank;
		this.heading = ip.heading;
		this.altAgl = altAgl;
		this.aileron = aileron;
		this.elevator = elevator;
		this.rudder = rudder;
		this.throttle = throttle;
		this.from = from;
		this.to = to;
	}

	public double getAileron() {
		return aileron;
	}

	public double getElevator() {
		return elevator;
	}

	public double getRudder() {
		return rudder;
	}

	public double getThrottle() {
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

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public double getAltAgl() {
		return altAgl;
	}

	public int getAirspeed() {
		return airspeed;
	}

	public double getPitch() {
		return pitch;
	}

	public double getBank() {
		return bank;
	}

	public double getHeading() {
		return heading;
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
}
