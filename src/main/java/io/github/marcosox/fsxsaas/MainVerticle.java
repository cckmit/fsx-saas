package io.github.marcosox.fsxsaas;

import io.github.marcosox.fsxsaas.fsx.FSX;
import io.github.marcosox.fsxsaas.fsx.ObjectManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {
	private final static String APP_NAME = "FSX simconnect as a service";
	private final static String APP_VERSION = "1.0.0";
	private final static int DEFAULT_PORT = 8080;
	private final static int DEFAULT_TRAFFIC_SCAN_INTERVAL_MS = 1000;

	private ObjectManager manager;
	private FSX fsx;

	/**
	 * Main entry point
	 *
	 * @param fut Vert.x Future object
	 */
	@Override
	public void start(Future<Void> fut) {
		System.out.println("Welcome to " + APP_NAME + " version " + APP_VERSION);
		System.out.println("MainVerticle.start()");
		setup();
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.route().handler(CorsHandler.create("*")
				.allowedMethod(HttpMethod.GET)
				.allowedMethod(HttpMethod.POST)
				.allowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN.toString())
				.allowedHeader(HttpHeaders.CONTENT_TYPE.toString())
				.allowedHeader(HttpHeaders.ORIGIN.toString()));

		router.get("/aircrafts").handler(r -> this.handleGetAll(r, manager.getAircrafts()));
		router.get("/aircrafts/:id").handler(r -> this.handleGetItem(r, manager.getAircrafts()));
		router.get("/boats").handler(r -> this.handleGetAll(r, manager.getBoats()));
		router.get("/boats/:id").handler(r -> this.handleGetItem(r, manager.getBoats()));
		router.get("/airports").handler(r -> this.handleGetAll(r, manager.getAirports()));
		router.get("/airports/:id").handler(r -> this.handleGetItem(r, manager.getAirports()));
		router.get("/vors").handler(r -> this.handleGetAll(r, manager.getVors()));
		router.get("/vors/:id").handler(r -> this.handleGetItem(r, manager.getVors()));
		router.get("/ndbs").handler(r -> this.handleGetAll(r, manager.getNDBs()));
		router.get("/ndbs/:id").handler(r -> this.handleGetItem(r, manager.getNDBs()));
		router.get("/waypoints").handler(r -> this.handleGetAll(r, manager.getWaypoints()));
		router.get("/waypoints/:id").handler(r -> this.handleGetItem(r, manager.getWaypoints()));
		router.get("/cmd/:command").handler(this::handleFsxCommand);
		router.get("/shutdown").handler(r -> this.quit(r, 0));
		List<Route> routes = router.getRoutes();
		router.get("/").handler(r -> this.handleRootURL(r, routes));

		int port = config().getInteger("port", DEFAULT_PORT);
		vertx.createHttpServer().requestHandler(router::accept).listen(port);
		System.out.println("HTTP server ready and listening on port " + port);
	}

	/**
	 * Quits the application
	 *
	 * @param routingContext http request routing context
	 * @param status         exit status
	 */
	private void quit(RoutingContext routingContext, int status) {
		routingContext.response().putHeader("content-type", "text/plain").end("BYE");
		Vertx.vertx().close();
		System.exit(status);
	}

	/**
	 * Root url handler
	 *
	 * @param routingContext http request routing context
	 * @param routes         list of routes to display
	 */
	private void handleRootURL(RoutingContext routingContext, List<Route> routes) {
		StringBuilder routesList = new StringBuilder();
		for (Route r : routes) {
			if (r.getPath() != null) {
				routesList.append(r.getPath()).append("\n");
			}
		}
		routingContext.response().putHeader("content-type", "text/plain").end(routesList.toString());
	}

	/**
	 * Handler for the LIST action
	 *
	 * @param routingContext http request routing context
	 * @param map            items map
	 */
	private void handleGetAll(RoutingContext routingContext, Map<String, ?> map) {
		routingContext.response().putHeader("content-type", "application/json").end(Json.encodePrettily(map.values()));
	}

	/**
	 * Handler for the RETRIEVE action
	 *
	 * @param r     http request routing context
	 * @param items items map
	 */
	private void handleGetItem(RoutingContext r, Map<String, ?> items) {
		String id = r.request().getParam("id");
		if (id != null) {
			Object item = items.get("" + id);
			if (item != null) {
				r.response().putHeader("content-type", "application/json").end(Json.encodePrettily(item));
			} else {
				if (id.equals("0")) {
					// user aircraft not found until sim is unpaused and the first info is sent
					r.response().setStatusCode(449).end("Unpause FSX for a second and try again.");
				} else {
					r.response().setStatusCode(404).end("item id " + id + " not found");
				}
			}
		}
	}

	/**
	 * Handles fsx commands: start/stop/restart/clear
	 *
	 * @param routingContext http request routing context
	 */
	private void handleFsxCommand(RoutingContext routingContext) {
		String cmd = routingContext.request().getParam("command");
		String msg;
		switch (cmd) {
			case "help": {
				msg = "start: start simconnect\n" +
						"stop: stop simconnect\n" +
						"restart: guess what\n" +
						"clear: clears all fsx objects from memory (aircrafts, vors, airports etc.)";
				break;
			}
			case "stop": {
				try {
					fsx.stopSimConnect();
					msg = "OK";
				} catch (IOException e) {
					e.printStackTrace();
					msg = e.getMessage();
				}
				break;
			}
			case "start": {
				try {
					fsx.startSimConnect();
					msg = "OK";
				} catch (IOException e) {
					e.printStackTrace();
					msg = e.getMessage();
				}
				break;
			}
			case "restart": {
				try {
					fsx.restartSimConnect();
					msg = "OK";
				} catch (IOException e) {
					e.printStackTrace();
					msg = e.getMessage();
				}
				break;
			}
			case "clear": {
				manager.clearAll();
				msg = "OK";
				break;
			}
			default: {
				msg = "not a command - try help";
				break;
			}
		}
		JsonObject responseMsg = new JsonObject();
		responseMsg.put("response", msg);
		routingContext.response().putHeader("content-type", "application/json").end(responseMsg.encodePrettily());
	}

	/**
	 * Starts simconnect and the object manager
	 */
	private void setup() {
		this.manager = new ObjectManager();
		this.fsx = new FSX(manager, config().getInteger("scanInterval", DEFAULT_TRAFFIC_SCAN_INTERVAL_MS));
	}
}
