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
- See user aircraft: [http://127.0.0.1:8080/aircrafts/0](http://127.0.0.1:8080/aircrafts/0)
- See all the vors: [http://127.0.0.1:8080/vors](http://127.0.0.1:8080/vors)
- See all the airports: [http://127.0.0.1:8080/airports](http://127.0.0.1:8080/airports)

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