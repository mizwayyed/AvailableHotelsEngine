# BeamHotels

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

In order to run AvailableHotels App, you will need the following: 

* JDK 1.8
* Maven


### Configuring

There are a few files that need to be configured, which are located in the Resources folder:

* `log4j.properties`
* `ProvidersList.properties`: Used to manage (Add/Remove) Hotel Providers 
* `ServerConfig.properties`: Used to change the Server Configs (e.g: port).

## Adding Provideres

In order to add a new Provider, use the `ServiceProvider` interface, and add the full class path to 'ProvidersList.properties'


## Built With

* [Jersey](https://jersey.github.io/) - JAX-RS Implementation
* [Maven](https://maven.apache.org/) - Dependency Management
* [LOG4J](https://logging.apache.org/log4j/2.x/) - Used for Logging

