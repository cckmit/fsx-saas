package io.github.marcosox.fsxsaas.fsx;

import io.github.marcosox.fsxsaas.fsx.models.*;

import java.util.HashMap;
import java.util.Map;

public class ObjectManager {

	private Map<String, Aircraft> aircrafts = new HashMap<>();
	private Map<String, Boat> boats = new HashMap<>();
	private Map<String, Airport> airports = new HashMap<>();
	private Map<String, VOR> vor = new HashMap<>();
	private Map<String, NDB> ndb = new HashMap<>();
	private Map<String, Waypoint> waypoints = new HashMap<>();
	private final Object aircraftsLock = new Object();
	private final Object boatsLock = new Object();

	public void addAircraft(Aircraft aircraft) {
		synchronized (this.aircraftsLock) {
			aircrafts.put("" + aircraft.getId(), aircraft);
		}
	}

	public Map<String, Aircraft> getAircrafts() {
		Map<String, Aircraft> val;
		synchronized (this.aircraftsLock) {
			val = this.aircrafts;
		}
		return val;
	}

	public void addBoat(Boat boat) {
		synchronized (this.boatsLock) {
			boats.put("" + boat.getId(), boat);
		}
	}

	public Map<String, Boat> getBoats() {
		Map<String, Boat> val;
		synchronized (this.boatsLock) {
			val = this.boats;
		}
		return val;
	}

	public void addAirport(Airport airport) {
		this.airports.put(airport.getIcao(), airport);
	}

	public Map<String, Airport> getAirports() {
		return this.airports;
	}

	public void addWaypoint(Waypoint waypoint) {
		this.waypoints.put(waypoint.getIcao(), waypoint);
	}

	public Map<String, Waypoint> getWaypoints() {
		return this.waypoints;
	}

	public void addVOR(VOR vor) {
		this.vor.put(vor.getIcao(), vor);
	}

	public Map<String, VOR> getVors() {
		return this.vor;
	}

	public void addNDB(NDB ndb) {
		this.ndb.put(ndb.getIcao(), ndb);
	}

	public Map<String, NDB> getNDBs() {
		return this.ndb;
	}

	public void clearAircrafts() {
		synchronized (this.aircraftsLock) {
			this.aircrafts.clear();
		}
	}

	public void clearBoats() {
		synchronized (this.boatsLock) {
			this.boats.clear();
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
		clearAirports();
		clearVORs();
		clearNDBs();
		clearWaypoints();
	}
}
