package io.github.marcosox.fsxsaas.fsx;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.data.InitPosition;
import flightsim.simconnect.data.LatLonAlt;
import flightsim.simconnect.recv.*;
import io.github.marcosox.fsxsaas.fsx.models.*;

public class FSXListener implements SimObjectDataTypeHandler, FacilitiesListHandler, ExceptionHandler {
	private final ObjectManager manager;

	/**
	 * Creates the object responsible for receiving the simconnect responses
	 *
	 * @param manager object manager to which the parsed objects will be passed
	 */
	FSXListener(ObjectManager manager) {
		this.manager = manager;
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
		System.out.println("handleSimObjectType");
		int requestID = e.getRequestID();
		if (requestID == REQUEST_ID.TRAFFIC_SCAN.ordinal()) {
			int entryNumber = e.getEntryNumber();
			if (entryNumber == 1) {    // not 0 as docs say!
				manager.clearAircrafts();
			}
			int id = e.getObjectID();
			if (id == 1) {
				id = 0;    // fix user id to 0 only
			}
			String title = e.getDataString32();
			String atcType = e.getDataString32();
			String atcModel = e.getDataString32();
			String atcID = e.getDataString32();
			String atcAirline = e.getDataString64();
			String atcFlightNumber = e.getDataString8();
			String from = e.getDataString8();
			String to = e.getDataString8();
			LatLonAlt lla = e.getLatLonAlt();
			double spd = e.getDataFloat64();
			double groundSpd = e.getDataFloat64();
			double agl = e.getDataFloat64();
			double pit = e.getDataFloat64();
			double bnk = e.getDataFloat64();
			double hdg = e.getDataFloat64();
			double aileron = e.getDataFloat64();
			double elevator = e.getDataFloat64();
			double rudder = e.getDataFloat64();
			double throttle = e.getDataFloat64();    //doesnt work?

			InitPosition ip = new InitPosition();
			ip.setLatLonAlt(lla);
			ip.onGround = false; // set it manually because we didn't receive it
			ip.pitch = (pit);
			ip.bank = (bnk);
			ip.heading = (hdg);
			ip.airspeed = (int) Math.floor(spd);

			manager.addAircraft(new Aircraft(id, title, atcType,
					atcModel,
					atcID,
					atcAirline,
					atcFlightNumber, ip, groundSpd, agl, aileron, elevator, rudder, throttle, from, to));
		}
	}
}
