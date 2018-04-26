package io.github.marcosox.fsxsaas.fsx;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.recv.*;
import io.github.marcosox.fsxsaas.fsx.models.*;

import java.util.HashMap;
import java.util.LinkedList;

public class FSXListener implements SimObjectDataTypeHandler, FacilitiesListHandler, ExceptionHandler {
	private final ObjectManager manager;
	private final LinkedList<MyDataDefinitionWrapper> aircraftData;
	private final LinkedList<MyDataDefinitionWrapper> boatData;

	/**
	 * Creates the object responsible for receiving the simconnect responses
	 *
	 * @param manager      object manager to which the parsed objects will be passed
	 * @param aircraftData aircraft data definition objects ordered list
	 * @param boatData     boat data definition objects ordered list
	 */
	FSXListener(ObjectManager manager, LinkedList<MyDataDefinitionWrapper> aircraftData, LinkedList<MyDataDefinitionWrapper> boatData) {
		this.manager = manager;
		this.aircraftData = aircraftData;
		this.boatData = boatData;
	}

	@Override
	public void handleAirportList(SimConnect simConnect, RecvAirportList list) {
		System.out.println("handleAirportList");
		for (FacilityAirport f : list.getFacilities()) {
			manager.addAirport(new Airport(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude()));
		}
	}

	@Override
	public void handleWaypointList(SimConnect simConnect, RecvWaypointList list) {
		System.out.println("handleWaypointList");
		for (FacilityWaypoint f : list.getFacilities()) {
			manager.addWaypoint(new Waypoint(f.getIcao(), f.getLatitude(), f.getLongitude(), f.getAltitude(), f.getMagVar()));
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
				System.out.println("received list of aircrafts");
				manager.clearAircrafts();
			}
			int id = e.getObjectID();
			if (id == 1) {
				id = 0;    // fix user id to 0 only
			}
			HashMap<String, Object> map = new HashMap<>();
			for (MyDataDefinitionWrapper d : this.aircraftData) {
				map.put(d.getVarName(), d.getValue(e));
			}
			try {
				manager.addAircraft(new Aircraft(id, map));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} else if (requestID == REQUEST_ID.BOATS_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {
				manager.clearBoats();
				System.out.println("received list of boats");
			}
			int id = e.getObjectID();
			HashMap<String, Object> map = new HashMap<>();
			for (MyDataDefinitionWrapper d : this.boatData) {
				map.put(d.getVarName(), d.getValue(e));
			}
			try {
				manager.addBoat(new Boat(id, map));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
