package io.github.marcosox.fsxsaas.fsx;

import flightsim.simconnect.FacilityListType;
import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.SimObjectType;
import flightsim.simconnect.recv.DispatcherTask;
import io.vertx.core.Vertx;

import java.io.IOException;
import java.util.LinkedList;

public class FSX {
	private final ObjectManager manager;
	private SimConnect simconnect;
	private DispatcherTask dispatcherTask;
	private long trafficScanID = -1;
	private long scanInterval;
	private LinkedList<MyDataDefinitionWrapper> aircraftData = new LinkedList<>();
	private LinkedList<MyDataDefinitionWrapper> boatData = new LinkedList<>();
	private LinkedList<MyDataDefinitionWrapper> vehicleData = new LinkedList<>();
	private LinkedList<MyDataDefinitionWrapper> helicopterData = new LinkedList<>();

	private enum DATA_DEFINITION_ID {
		BOAT_DETAIL, AIRCRAFT_DETAIL, VEHICLE_DETAIL, HELICOPTER_DETAIL
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
		prepareRequests();
		dispatcherTask.addHandlers(new FSXListener(manager, this.aircraftData, this.helicopterData, this.boatData, this.vehicleData));
		new Thread(dispatcherTask).start();
		startRequests();
	}

	private void prepareRequests() {
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC TYPE", null, SimConnectDataType.STRING32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC MODEL", null, SimConnectDataType.STRING32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC ID", null, SimConnectDataType.STRING32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC AIRLINE", null, SimConnectDataType.STRING64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC FLIGHT NUMBER", null, SimConnectDataType.STRING8));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ATC HEAVY", null, SimConnectDataType.INT32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC STATE", null, SimConnectDataType.STRING8));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC FROMAIRPORT", null, SimConnectDataType.STRING8));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC TOAIRPORT", null, SimConnectDataType.STRING8));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("TRANSPONDER CODE:1", null, SimConnectDataType.INT32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("STRUCT LATLONALT", null, SimConnectDataType.LATLONALT));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AIRSPEED TRUE", "KNOTS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("VERTICAL SPEED", "FEET", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("GROUND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("PLANE ALT ABOVE GROUND", "FEET", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("SIM ON GROUND", null, SimConnectDataType.INT32));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("PLANE PITCH DEGREES", "RADIANS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("PLANE BANK DEGREES", "RADIANS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("PLANE HEADING DEGREES TRUE", "DEGREES", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AILERON POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("ELEVATOR POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("RUDDER POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("GENERAL ENG THROTTLE LEVER POSITION:1", "PERCENT", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND DIRECTION", "DEGREES", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AMBIENT TEMPERATURE", "CELSIUS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AMBIENT PRESSURE", "inHg", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("BAROMETER PRESSURE", "MILLIBARS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("AMBIENT VISIBILITY", "KILOMETERS", SimConnectDataType.FLOAT64));
		this.aircraftData.addLast(new MyDataDefinitionWrapper("TITLE", null, SimConnectDataType.STRINGV));

		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC TYPE", null, SimConnectDataType.STRING32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC MODEL", null, SimConnectDataType.STRING32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC ID", null, SimConnectDataType.STRING32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC AIRLINE", null, SimConnectDataType.STRING32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC FLIGHT NUMBER", null, SimConnectDataType.STRING8));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ATC HEAVY", null, SimConnectDataType.INT32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC STATE", null, SimConnectDataType.STRING8));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC FROMAIRPORT", null, SimConnectDataType.STRING8));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC TOAIRPORT", null, SimConnectDataType.STRING8));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("TRANSPONDER CODE:1", null, SimConnectDataType.INT32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("STRUCT LATLONALT", null, SimConnectDataType.LATLONALT));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AIRSPEED TRUE", "KNOTS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("VERTICAL SPEED", "FEET", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("GROUND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("PLANE ALT ABOVE GROUND", "FEET", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("SIM ON GROUND", null, SimConnectDataType.INT32));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("PLANE PITCH DEGREES", "RADIANS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("PLANE BANK DEGREES", "RADIANS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("PLANE HEADING DEGREES TRUE", "DEGREES", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AILERON POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("ELEVATOR POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("RUDDER POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("GENERAL ENG THROTTLE LEVER POSITION:1", "PERCENT", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND DIRECTION", "DEGREES", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AMBIENT TEMPERATURE", "CELSIUS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AMBIENT PRESSURE", "inHg", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("BAROMETER PRESSURE", "MILLIBARS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("AMBIENT VISIBILITY", "KILOMETERS", SimConnectDataType.FLOAT64));
		this.helicopterData.addLast(new MyDataDefinitionWrapper("TITLE", null, SimConnectDataType.STRINGV));

		this.boatData.addLast(new MyDataDefinitionWrapper("STRUCT LATLONALT", null, SimConnectDataType.LATLONALT));
		this.boatData.addLast(new MyDataDefinitionWrapper("AIRSPEED TRUE", "KNOTS", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("GROUND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("PLANE ALT ABOVE GROUND", "FEET", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("PLANE BANK DEGREES", "RADIANS", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("PLANE HEADING DEGREES TRUE", "DEGREES", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("RUDDER POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("GENERAL ENG THROTTLE LEVER POSITION:1", "PERCENT", SimConnectDataType.FLOAT64));
		this.boatData.addLast(new MyDataDefinitionWrapper("TITLE", null, SimConnectDataType.STRINGV));

		this.vehicleData.addLast(new MyDataDefinitionWrapper("AI TRAFFIC STATE", null, SimConnectDataType.STRING8));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("STRUCT LATLONALT", null, SimConnectDataType.LATLONALT));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("GROUND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("PLANE HEADING DEGREES TRUE", "DEGREES", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("RUDDER POSITION", "POSITION", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("GENERAL ENG THROTTLE LEVER POSITION:1", "PERCENT", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND VELOCITY", "KNOTS", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("AMBIENT WIND DIRECTION", "DEGREES", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("AMBIENT TEMPERATURE", "CELSIUS", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("AMBIENT PRESSURE", "inHg", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("BAROMETER PRESSURE", "MILLIBARS", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("AMBIENT VISIBILITY", "KILOMETERS", SimConnectDataType.FLOAT64));
		this.vehicleData.addLast(new MyDataDefinitionWrapper("TITLE", null, SimConnectDataType.STRINGV));
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

		for (MyDataDefinitionWrapper d : this.aircraftData) {
			simconnect.addToDataDefinition(DATA_DEFINITION_ID.AIRCRAFT_DETAIL, d.getVarName(), d.getUnitsName(), d.getDataType());
		}
		for (MyDataDefinitionWrapper d : this.helicopterData) {
			simconnect.addToDataDefinition(DATA_DEFINITION_ID.HELICOPTER_DETAIL, d.getVarName(), d.getUnitsName(), d.getDataType());
		}
		for (MyDataDefinitionWrapper d : this.boatData) {
			simconnect.addToDataDefinition(DATA_DEFINITION_ID.BOAT_DETAIL, d.getVarName(), d.getUnitsName(), d.getDataType());
		}
		for (MyDataDefinitionWrapper d : this.vehicleData) {
			simconnect.addToDataDefinition(DATA_DEFINITION_ID.VEHICLE_DETAIL, d.getVarName(), d.getUnitsName(), d.getDataType());
		}
		simconnect.subscribeToFacilities(FacilityListType.AIRPORT, REQUEST_ID.AIRPORTS_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.VOR, REQUEST_ID.VOR_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.NDB, REQUEST_ID.NDB_SCAN);
		simconnect.subscribeToFacilities(FacilityListType.WAYPOINT, REQUEST_ID.WAYPOINTS_SCAN);

		this.trafficScanID = Vertx.vertx().setPeriodic(scanInterval, e -> {
			try {
				// check if user position is known and request metar
				if (manager.getAircrafts().containsKey("0")) {
					simconnect.weatherRequestObservationAtNearestStation(REQUEST_ID.METAR,
							manager.getAircrafts().get("0").getLatitude().floatValue(),
							manager.getAircrafts().get("0").getLongitude().floatValue());
				}
				simconnect.requestDataOnSimObjectType(REQUEST_ID.AIRCRAFTS_SCAN, DATA_DEFINITION_ID.AIRCRAFT_DETAIL, 0, SimObjectType.AIRCRAFT);
				simconnect.requestDataOnSimObjectType(REQUEST_ID.HELICOPTERS_SCAN, DATA_DEFINITION_ID.HELICOPTER_DETAIL, 0, SimObjectType.HELICOPTER);
				simconnect.requestDataOnSimObjectType(REQUEST_ID.BOATS_SCAN, DATA_DEFINITION_ID.BOAT_DETAIL, 0, SimObjectType.BOAT);
				simconnect.requestDataOnSimObjectType(REQUEST_ID.VEHICLES_SCAN, DATA_DEFINITION_ID.VEHICLE_DETAIL, 0, SimObjectType.GROUND);
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
