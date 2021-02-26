Coverage: 82%
# Inventory Management System

A fairly simple MVC styled Java command line application, modeling a simple order system system. Tracks items, customers and orders from those customers for items.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

```
Maven v3.6 - https://maven.apache.org/
Java v1.8+ - https://www.oracle.com/uk/java/technologies/javase-downloads.html
Mysql - https://dev.mysql.com/downloads/installer/
```

### Installing

1) Fork the project and clone the project to your directory of choice.

```
>git clone https://github.com/KLRyder/IMS-Starter.git
```

2) Change the Username, Password, and url to a valid configuration in db.properties to a valid user for your Mysql setup.

```
db.url=jdbc:mysql://localhost:3306/ims
db.user=root
db.password=root
```
Please not that the project currently requires the use of the ims schema. If you wish to change this alterations to the codebase must be made appropreatly.

3) Ensure that you have installed all by building the project using maven to package the project from its root directory

```
> mvn clean package
> cd target
> java -jar ims-1.1.0-jar-with-dependencies
```

4) If this runs the aplication enter the command init and tables will automatically be generated for your mysql server.

```
> java -jar ims-1.1.0-jar-with-dependencies
Welcome to the Inventory Management System!
Which entity would you like to use?
CUSTOMER: Information about customers
ITEM: Individual Items
ORDER: Purchases of items
STOP: To close the application
INIT: To setup a new database (deletes old data)
>init
new tables created
```


## Running the tests


### Unit Tests 

Unit tests have been provided for the following classes:
```
Controllers
-----------
CustomerController
ItemController
OrderController

DOAs
-----------
CustomerDoa
ItemDoa
OrderDoa

Domain classes
-----------
Customer
Item
Order
```
These tests simply ensure that the database accsess and manipulation works as they should, and help to dignose where the issue is if any is found.

Provided that you hve your maven installed and configured correctly these tests will run when this is run from the root folder via command line:

```
>mvn clean test
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Chris Perrins** - *Initial work* - [christophperrins](https://github.com/christophperrins)
* **Kieran Ryder** - *Item and Order system implementations* - [KLRyder](https://github.com/KLRyder)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

