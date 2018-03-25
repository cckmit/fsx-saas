# FSX simconnect-as-a-service

FSX informations exposed by a JSON HTTP API server

## Installation
FSX-saas is portable, you just need to download the latest jar from the [releases page] and run it.

## Usage
    java -jar path/to/fsx-saas-1.0.0.jar
    
To change the application parameters pass the path of a json configuration file:

    java -jar path/to/fsx-saas-1.0.0.jar -conf path/to/config.json

The complete configuration file is this:

    {
        "port": 8080
        "scanInterval": 1000
    }

- `port`: tcp listening port for the server
- `scanInterval`: milliseconds between aircraft requests to fsx

#### Vertx options
Since this application is packaged with a Vertx launcher, all the vertx options can be passed.
For more informations see the [help page](http://vertx.io/docs/vertx-core/java/#_the_vertx_command_line)

## Quick start and examples
- Start FSX and load a flight
- Start FSX-saas
- open your browser and point it to [http://127.0.0.1:8080/](http://127.0.0.1:8080/) to see all the available API endpoints
- See all the aircrafts: [http://127.0.0.1:8080/aircrafts](http://127.0.0.1:8080/aircrafts)
```
[ {
  "id" : 990,
  "title" : "de Havilland Dash 8-100 Paint2",
  "atcType" : "DeHavilland",
  "atcModel" : "DH8A",
  "atcID" : "N7048R",
  "atcAirline" : "American Pacific",
  "atcFlightNumber" : "1123",
  "from" : "KBFI",
  "to" : "KIDA",
  "latitude" : 47.524769343755345,
  "longitude" : -122.29564771099874,
  "altitude" : 25.338651001296956,
  "altAgl" : 7.339984247004242,
  "airspeed" : 0,
  "pitch" : 0.002319927327334881,
  "bank" : 0.0,
  "heading" : 56.78009212492774,
  "aileron" : 0.0,
  "elevator" : 0.0,
  "rudder" : 0.0,
  "throttle" : 0.0
}, {
...
```
- See user aircraft: [http://127.0.0.1:8080/aircrafts/0](http://127.0.0.1:8080/aircrafts/0)
```
{
  "id" : 0,
  "title" : "Aircreation582SL red",
  "atcType" : "Ultralight",
  "atcModel" : "trike",
  "atcID" : "",
  "atcAirline" : "",
  "atcFlightNumber" : "",
  "from" : "",
  "to" : "",
  "latitude" : 48.502472910349816,
  "longitude" : -123.01101544961938,
  "altitude" : 1607.6589068715653,
  "altAgl" : 1607.6588833676299,
  "airspeed" : 52,
  "pitch" : 0.026875153183937073,
  "bank" : -2.423224722577144E-4,
  "heading" : 357.5731996999381,
  "aileron" : -6.103515625E-5,
  "elevator" : -6.103515625E-5,
  "rudder" : -6.103515625E-5,
  "throttle" : 73.236083984375
}
```
- See all the vors: [http://127.0.0.1:8080/vors](http://127.0.0.1:8080/vors)
```
[ {
  "icao" : "ITL",
  "latitude" : 49.19996581971645,
  "longitude" : -123.15604135394096,
  "altitude" : 4.267000198364258,
  "frequency" : 110550000,
  "localizer" : 100.01,
  "glideSlopeAngle" : 3.0,
  "glideLat" : 49.2051887512207,
  "glideLon" : -123.19614619016647,
  "glideAlt" : 4.267000198364258,
  "flags" : 15
}, {
...
```
- See all the airports: [http://127.0.0.1:8080/airports](http://127.0.0.1:8080/airports)
```
[ {
  "icao" : "WA77",
  "latitude" : 47.19565697014332,
  "longitude" : -122.02205613255501,
  "altitude" : 224.9420166015625
}, {
  "icao" : "7WA3",
  "latitude" : 48.88611115515232,
  "longitude" : -122.32861116528511,
  "altitude" : 48.768001556396484
}, {
  "icao" : "7WA5",
  "latitude" : 48.67287788540125,
  "longitude" : -123.1757453083992,
  "altitude" : 3.0480000972747803
}, {
...
```
Please note that data is limited to what FSX returns:
- aircrafts only exist in fsx inside a 199km radius around user aircraft
- same thing for airports and navigation aids, but since they have a fixed position,
they are stored forever in the cache since the flight start.

### Dependencies
FSX-saas depends on jSimconnect 0.8, which is **not** included with the code.
You can download it from the [original author site](http://lc0277.gratisim.fr/jsimconnect.html)
 or from [this github repository](https://github.com/mharj/jsimconnect).
 
It is needed only for development. Place it in the `lib/` folder and include it as a library in your IDE.
Library inclusion in the fat jar during build is managed by maven.

## Source code

If you want to develop on this codebase:
1. clone the repository into your IDE
2. download the jSimconnect jar and put it in the lib/ folder
 
## Feedback
If you think there is a bug in the program, or something is missing or wrong with the documentation/support files, feel free to [open an issue].

## License
This software is released under the LGPL V3 license.
The license terms are included in the LICENSE file.


[open an issue]: https://github.com/marcosox/fsx-saas/issues
[releases page]: https://github.com/marcosox/fsx-saas/releases
