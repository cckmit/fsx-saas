package io.github.marcosox.fsxsaas.fsx;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.recv.*;
import io.github.marcosox.fsxsaas.fsx.models.*;

import java.util.HashMap;
import java.util.LinkedList;

public class FSXListener implements SimObjectDataTypeHandler, FacilitiesListHandler, WeatherObservationHandler, ExceptionHandler {
	private final ObjectManager manager;
	private final LinkedList<MyDataDefinitionWrapper> aircraftData;
	private final LinkedList<MyDataDefinitionWrapper> boatData;
	private final LinkedList<MyDataDefinitionWrapper> vehicleData;
	private final LinkedList<MyDataDefinitionWrapper> helicopterData;

	/**
	 * Creates the object responsible for receiving the simconnect responses
	 *
	 * @param manager      object manager to which the parsed objects will be passed
	 * @param aircraftData aircraft data definition objects ordered list
	 * @param boatData     boat data definition objects ordered list
	 * @param vehicleData  vehicle data definition objects ordered list
	 */
	FSXListener(ObjectManager manager,
				LinkedList<MyDataDefinitionWrapper> aircraftData,
				LinkedList<MyDataDefinitionWrapper> helicopterData,
				LinkedList<MyDataDefinitionWrapper> boatData,
				LinkedList<MyDataDefinitionWrapper> vehicleData) {
		this.manager = manager;
		this.aircraftData = aircraftData;
		this.helicopterData = helicopterData;
		this.boatData = boatData;
		this.vehicleData = vehicleData;
	}

	@Override
	public void handleAirportList(SimConnect simConnect, RecvAirportList list) {
		System.out.println("handleAirportList");
		for (FacilityAirport f : list.getFacilities()) {
			manager.addAirport(new Airport(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude()));
		}
		System.out.println("end handleAirportList");
	}

	@Override
	public void handleWaypointList(SimConnect simConnect, RecvWaypointList list) {
		System.out.println("handleWaypointList");
		for (FacilityWaypoint f : list.getFacilities()) {
			manager.addWaypoint(new Waypoint(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude(), 0));
		}
	}

	@Override
	public void handleVORList(SimConnect simConnect, RecvVORList list) {
		System.out.println("handleVORList");
		for (FacilityVOR f : list.getFacilities()) {
			manager.addVOR(new VOR(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude(), f.getFrequency(), f.getLocalizer(), f.getGlideSlopeAngle(),
					f.getGlideLat(), f.getGlideLon(), f.getGlideAlt(), f.getFlags()));
		}
	}

	@Override
	public void handleNDBList(SimConnect simConnect, RecvNDBList list) {
		System.out.println("handleNDBList");
		for (FacilityNDB f : list.getFacilities()) {
			manager.addNDB(new NDB(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude(), 0, 0.0f));
		}
	}

	@Override
	public void handleException(SimConnect simConnect, RecvException e) {
		System.err.println("Simconnect exception: " + e.getException().getMessage() + " on request " + e.getSendID());
	}

	@Override
	public void handleSimObjectType(SimConnect simConnect, RecvSimObjectDataByType e) {
		int requestID = e.getRequestID();
		if (requestID == REQUEST_ID.AIRCRAFTS_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {
				System.out.println("received list of " + e.getOutOf() + " aircrafts");
				manager.clearAircrafts();
			} else if (entryNumber == 0) {
				System.out.println("no aircrafts present at the moment.");
				manager.clearAircrafts();
			}
//			System.out.println("Entry " + entryNumber + "/" + e.getOutOf());
			if (entryNumber > 0) {
				try {
					int id = e.getObjectID();
					if (id == 1) {
						id = 0;    // fix user id to 0 only
					}
					HashMap<String, Object> map = new HashMap<>();
					for (MyDataDefinitionWrapper d : this.aircraftData) {
//						System.out.println(d.getVarName()+" - AIRCRAFTS");
						map.put(d.getVarName(), d.getValue(e));
					}
					manager.addAircraft(new Aircraft(id, map));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} else if (requestID == REQUEST_ID.HELICOPTERS_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {
				manager.clearHelicopters();
				System.out.println("received list of " + e.getOutOf() + " helicopters");
			} else if (entryNumber == 0) {
				System.out.println("no helicopters present at the moment.");
				manager.clearHelicopters();
			}
//			System.out.println("Entry " + entryNumber + "/" + e.getOutOf());
			if (entryNumber > 0) {
				try {
					int id = e.getObjectID();
					if (id == 1) {
						id = 0;    // fix user id to 0 only
					}
					HashMap<String, Object> map = new HashMap<>();
					for (MyDataDefinitionWrapper d : this.helicopterData) {
//						System.out.println(d.getVarName() + " - HELICOPTERS");
						map.put(d.getVarName(), d.getValue(e));
					}
					manager.addHelicopter(new Aircraft(id, map));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else if (requestID == REQUEST_ID.BOATS_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {
				manager.clearBoats();
				System.out.println("received list of " + e.getOutOf() + " boats");
			} else if (entryNumber == 0) {
				System.out.println("no boats present at the moment.");
				manager.clearBoats();
			}
//			System.out.println("Entry " + entryNumber + "/" + e.getOutOf());
			if (entryNumber > 0) {
				try {
					int id = e.getObjectID();
					HashMap<String, Object> map = new HashMap<>();
					for (MyDataDefinitionWrapper d : this.boatData) {
//						System.out.println(d.getVarName()+" - BOATS");
						map.put(d.getVarName(), d.getValue(e));
					}
					manager.addBoat(new Boat(id, map));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else if (requestID == REQUEST_ID.VEHICLES_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {
				manager.clearVehicles();
				System.out.println("received list of " + e.getOutOf() + " ground vehicles");
			} else if (entryNumber == 0) {
				System.out.println("no ground vehicles present at the moment.");
				manager.clearVehicles();
			}
//			System.out.println("Entry " + entryNumber + "/" + e.getOutOf());
			if (entryNumber > 0) {
				try {
					int id = e.getObjectID();
					HashMap<String, Object> map = new HashMap<>();
					for (MyDataDefinitionWrapper d : this.vehicleData) {
//						System.out.println(d.getVarName()+" - VEHICLES");
						map.put(d.getVarName(), d.getValue(e));
					}
					manager.addVehicle(new Vehicle(id, map));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public void handleWeatherObservation(SimConnect simConnect, RecvWeatherObservation e) {
		int requestID = e.getRequestID();
		if (requestID == REQUEST_ID.METAR.ordinal()) {
			System.out.println("received METAR");
			String metar = e.getMetar();
			manager.setMetar(metar);
		}
	}
}
