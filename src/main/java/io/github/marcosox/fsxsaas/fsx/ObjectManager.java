package io.github.marcosox.fsxsaas.fsx;

import io.github.marcosox.fsxsaas.fsx.models.*;

import java.util.HashMap;
import java.util.Map;

public class ObjectManager {

	private Map<String, Aircraft> aircrafts = new HashMap<>();
	private Map<String, Aircraft> helicopters = new HashMap<>();
	private Map<String, Boat> boats = new HashMap<>();
	private Map<String, Vehicle> vehicles = new HashMap<>();
	private Map<String, Airport> airports = new HashMap<>();
	private Map<String, VOR> vor = new HashMap<>();
	private Map<String, NDB> ndb = new HashMap<>();
	private Map<String, Waypoint> waypoints = new HashMap<>();
	private final Object aircraftsLock = new Object();
	private final Object boatsLock = new Object();
	private final Object vehiclesLock = new Object();
	private final Object helicoptersLock = new Object();
	private String metar = "";

	void addAircraft(Aircraft aircraft) {
		synchronized (this.aircraftsLock) {
			aircrafts.put("" + aircraft.getId(), aircraft);
		}
	}

	void addHelicopter(Aircraft helicopter) {
		synchronized (this.helicoptersLock) {
			helicopters.put("" + helicopter.getId(), helicopter);
		}
	}

	void addBoat(Boat boat) {
		synchronized (this.boatsLock) {
			boats.put("" + boat.getId(), boat);
		}
	}

	void addVehicle(Vehicle vehicle) {
		synchronized (this.vehiclesLock) {
			vehicles.put("" + vehicle.getId(), vehicle);
		}
	}

	void addAirport(Airport airport) {
		this.airports.put(airport.getIcao(), airport);
	}

	void addWaypoint(Waypoint waypoint) {
		this.waypoints.put(waypoint.getIcao(), waypoint);
	}

	void addVOR(VOR vor) {
		this.vor.put(vor.getIcao(), vor);
	}

	void addNDB(NDB ndb) {
		this.ndb.put(ndb.getIcao(), ndb);
	}

	public Map<String, Aircraft> getAircrafts() {
		Map<String, Aircraft> val;
		synchronized (this.aircraftsLock) {
			val = new HashMap<>(this.aircrafts);
		}
		return val;
	}

	public Map<String, Aircraft> getHelicopters() {
		Map<String, Aircraft> val;
		synchronized (this.helicoptersLock) {
			val = new HashMap<>(this.helicopters);
		}
		return val;
	}

	public Map<String, Boat> getBoats() {
		Map<String, Boat> val;
		synchronized (this.boatsLock) {
			val = new HashMap<>(this.boats);
		}
		return val;
	}

	public Map<String, Vehicle> getVehicles() {
		Map<String, Vehicle> val;
		synchronized (this.vehiclesLock) {
			val = new HashMap<>(this.vehicles);
		}
		return val;
	}

	public Map<String, Airport> getAirports() {
		return this.airports;
	}

	public Map<String, Waypoint> getWaypoints() {
		return this.waypoints;
	}

	public Map<String, VOR> getVors() {
		return this.vor;
	}

	public Map<String, NDB> getNDBs() {
		return this.ndb;
	}

	void clearAircrafts() {
		synchronized (this.aircraftsLock) {
			this.aircrafts.clear();
		}
	}

	void clearHelicopters() {
		synchronized (this.helicoptersLock) {
			this.helicopters.clear();
		}
	}

	void clearBoats() {
		synchronized (this.boatsLock) {
			this.boats.clear();
		}
	}

	void clearVehicles() {
		synchronized (this.vehiclesLock) {
			this.vehicles.clear();
		}
	}

	private void clearAirports() {
		this.airports.clear();
	}

	private void clearVORs() {
		this.vor.clear();
	}

	private void clearNDBs() {
		this.ndb.clear();
	}

	private void clearWaypoints() {
		this.waypoints.clear();
	}

	public void clearAll() {
		clearAircrafts();
		clearHelicopters();
		clearBoats();
		clearVehicles();
		clearAirports();
		clearVORs();
		clearNDBs();
		clearWaypoints();
	}

	void setMetar(String metar) {
		this.metar = metar;
	}

	public String getMetar() {
		return metar;
	}
}
