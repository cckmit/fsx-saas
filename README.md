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
   "id" : 1187,
    "title" : "McDonnell-Douglas/Boeing MD-83",
    "atcType" : "Boeing",
    "atcModel" : "MD80",
    "atcID" : "I-MAFW",
    "atcAirline" : "Soar",
    "atcFlightNumber" : "3382",
    "atcHeavy" : 0,
    "atcState" : "enroute",
    "from" : "EDDF",
    "to" : "LIRF",
    "latitude" : 41.707100915,
    "longitude" : 12.331678205,
    "altitude" : 775.67,
    "altAgl" : 2544.86,
    "onGround" : 0,
    "airspeed" : 222.81,
    "groundSpeed" : 223.31,
    "verticalSpeed" : -2.422,
    "pitch" : -0.058,
    "bank" : 0.000,
    "heading" : 317.456,
    "aileron" : -0.00042724609375,
    "elevator" : 0.1611328125,
    "rudder" : 0.002197265625,
    "throttle" : 20.64208984375,
    "transponder" : "4263",
    "windSpeed" : 0.00,
    "windDirection" : 0.00,
    "visibility" : 10000000,
    "ambientTemperature" : 10.19,
    "ambientPressure" : 27.27,
    "barometerPressure" : 923.55
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
    "atcHeavy" : 0,
    "atcState" : "init",
    "from" : "",
    "to" : "",
    "latitude" : 41.795835751,
    "longitude" : 12.252768755,
    "altitude" : 70.11,
    "altAgl" : 216.02,
    "onGround" : 0,
    "airspeed" : 0.00,
    "groundSpeed" : 0.00,
    "verticalSpeed" : 0.000,
    "pitch" : 0.118,
    "bank" : 0.000,
    "heading" : 342.649,
    "aileron" : -0.00006103515625,
    "elevator" : -0.00006103515625,
    "rudder" : -0.00006103515625,
    "throttle" : 0,
    "transponder" : "1200",
    "windSpeed" : 0.00,
    "windDirection" : 0.00,
    "visibility" : 10000000,
    "ambientTemperature" : 14.77,
    "ambientPressure" : 29.67,
    "barometerPressure" : 1004.81
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
