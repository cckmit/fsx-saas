package io.github.marcosox.fsxsaas.fsx.models;

import flightsim.simconnect.data.InitPosition;

public class Boat {
	private int id;
	private String title;
	private double latitude;
	private double longitude;
	private double altitude;
	private double altAgl;
	private int airspeed;
	private double groundSpeed;
	private double bank;
	private double heading;
	private double rudder;
	private double throttle;

	public Boat(int id, String title, InitPosition ip, double groundSpeed, double altAgl, double rudder, double throttle) {
		this.id = id;
		this.title = title;
		this.latitude = ip.latitude;
		this.longitude = ip.longitude;
		this.altitude = ip.altitude;
		this.airspeed = ip.airspeed;
		this.groundSpeed = groundSpeed;
		this.bank = ip.bank;
		this.heading = ip.heading;
		this.altAgl = altAgl;
		this.rudder = rudder;
		this.throttle = throttle;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
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

	public double getGroundSpeed() {
		return groundSpeed;
	}

	public double getBank() {
		return bank;
	}

	public double getHeading() {
		return heading;
	}

	public double getRudder() {
		return rudder;
	}

	public double getThrottle() {
		return throttle;
	}
}
