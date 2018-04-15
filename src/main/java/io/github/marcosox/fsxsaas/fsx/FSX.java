package io.github.marcosox.fsxsaas.fsx;

import flightsim.simconnect.FacilityListType;
import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.SimObjectType;
import flightsim.simconnect.recv.DispatcherTask;
import io.vertx.core.Vertx;

import java.io.IOException;

public class FSX {
	private final ObjectManager manager;
	private SimConnect simconnect;
	private DispatcherTask dispatcherTask;
	private long trafficScanID = -1;
	private long scanInterval;

	private enum DATA_DEFINITION_ID {
		AIRCRAFT_DETAIL
	}

	public FSX(ObjectManager manager, int scanInterval) {
		this.manager = manager;
		this.scanInterval = scanInterval;
		try {
			startSimConnect();
		} catch (IOException e) {
			System.out.println("Could not connect to FSX! The server will not show any object until fsx is running. Start fsx and trigger /cmd/restart");
			e.printStackTrace();
		}
	}

	public void startSimConnect() throws IOException {
		try {
			if (simconnect != null) {
				stopSimConnect();
				simconnect = null;
			}
		} catch (IOException e) {
			System.out.println("warning closing old instance of simconnect");
			e.printStackTrace();
		}
		simconnect = new SimConnect("fsx-saas");
		dispatcherTask = new DispatcherTask(simconnect);
		dispatcherTask.addHandlers(new FSXListener(manager));
		new Thread(dispatcherTask).start();
		startRequests();
	}

	public void stopSimConnect() throws IOException {
		if (simconnect != null) {
			stopRequests();
		}
		if (dispatcherTask != null) {
			dispatcherTask.tryStop();
		}
		if (simconnect != null) {
			simconnect.close();
			simconnect = null;
		}
	}

	public void restartSimConnect() throws IOException {
		stopSimConnect();
		startSimConnect();
	}

	public void removeHandler(Object handler) {
		dispatcherTask.removeHandlers(handler);
	}

	private void startRequests() throws IOException {
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "TITLE", null, SimConnectDataType.STRING32);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ATC TYPE", null, SimConnectDataType.STRING32);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ATC MODEL", null, SimConnectDataType.STRING32);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ATC ID", null, SimConnectDataType.STRING32);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ATC AIRLINE", null, SimConnectDataType.STRING64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ATC FLIGHT NUMBER", null, SimConnectDataType.STRING8);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "AI TRAFFIC FROMAIRPORT", null, SimConnectDataType.STRING8);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "AI TRAFFIC TOAIRPORT", null, SimConnectDataType.STRING8);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "STRUCT LATLONALT", null, SimConnectDataType.LATLONALT);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "AIRSPEED TRUE", "KNOTS", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "GROUND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "PLANE ALT ABOVE GROUND", "FEET", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "PLANE PITCH DEGREES", "RADIANS", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "PLANE BANK DEGREES", "RADIANS", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "PLANE HEADING DEGREES TRUE", "DEGREES", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "AILERON POSITION", "POSITION", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "ELEVATOR POSITION", "POSITION", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "RUDDER POSITION", "POSITION", SimConnectDataType.FLOAT64);
		simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, "GENERAL ENG THROTTLE LEVER POSITION:1", "PERCENT", SimConnectDataType.FLOAT64);

		simconnect.subscribeToFacilities(FacilityListType.AIRPORT, REQUEST_ID.AIRPORTS_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.VOR, REQUEST_ID.VOR_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.NDB, REQUEST_ID.NDB_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.WAYPOINT, REQUEST_ID.WAYPOINTS_SCAN);

		this.trafficScanID = Vertx.vertx().setPeriodic(scanInterval, e -> {
			try {
				simconnect.requestDataOnSimObjectType(REQUEST_ID.TRAFFIC_SCAN, DATA_DEFINITION_ID.AIRCRAFT_DETAIL, 0, SimObjectType.AIRCRAFT);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void stopRequests() {
		for (FacilityListType f : new FacilityListType[]{
				FacilityListType.AIRPORT,
				FacilityListType.WAYPOINT,
				FacilityListType.VOR,
				FacilityListType.NDB
		}) {
			try {
				simconnect.unSubscribeToFacilities(f);
			} catch (IOException ioe) {
				System.out.println("exception unsubscribing to facility type " + f.toString() + ":");
				ioe.printStackTrace();
			}
		}
		Vertx.vertx().cancelTimer(this.trafficScanID);
	}
}