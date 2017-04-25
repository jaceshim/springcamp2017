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
4. Add hosts information to hosts file on your Operating System.
	* 127.0.0.1 member.jaceshim.com order.jaceshim.com product.jaceshim.com

## Run
1. Execute SpringBoot Application
	* execute command "../gradlew bootRun" into each service directory
	* e.g. cd member; ../gradlew bootRun

2. If you ran each applications then You can access the URL ( http://member.jaceshim.com:10001/regist )

## Additional Information
If you using docker then Please execute blow commands.
* docker run -d -p 2181:2181 --name zookeeper  dockerkafka/zookeeper
* docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper -e KAFKA_ADVERTISED_HOST_NAME=localhost -e KAFKA_ADVERTISED_PORT=9092 ches/kafka
* docker run --name redis -d --restart=always -publish 6379:6379 sameersbn/redis:latest
* docker run --name mysql -p 3306:3306 -v {your_mysqld_charset.cnf_file_directory}:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=root -d mysql
	> mysqld_charset.cnf
	
		[mysqld]
		character_set_server=utf8
		character_set_filesystem=utf8
		collation-server=utf8_general_ci
		init-connect='SET NAMES utf8'
		init_connect='SET collation_connection = utf8_general_ci'
		skip-character-set-client-handshake
     
     
     
     
