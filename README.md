# springcamp2017
eventsourcing &amp; cqrs demo project for springcamp2017

## Required

* Java8
* Redis 2.x - using default port ( 6379 )
* Mysql 5.x - using default port ( 3306 )
* Zookeeper - using default port( 2181 )
* Kafka - using port ( 9092 )
* Git

## Install
1. git clone https://github.com/jaceshim/springcamp2017.git
2. cd your_clone_path
3. create database & table ( default user is root )
	* execute script into each service directory/scripts
	* e.g. member/scripts/create_database_table.sql
4. Add hosts information to hosts file on your Operation System.
	* 127.0.0.1 member.jaceshim.com order.jaceshim.com product.jaceshim.com

## Run
1. Execute SpringBoot Application
	* execute command "../gradlew bootRun" into each service directory
	* e.g. cd member; ../gradlew bootRun

2. If you ran each applications then You can access the URL ( http://member.jaceshi.com:10001/regist )