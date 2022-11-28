This is a simple project to store data in the postgres database primarily,  
then sync those data from pg to elasticsearch through kafka broker for faster search. 


Techs: SpringBoot, Elasticsearch, Kafka, Redis, Hibernate, Spring Data JPA, Flyway, and PostgreSQL 

Steps to run this project: \
Step 1. create a database with a user using password \
$ DROP DATABASE if exists houseprices; \
$ CREATE DATABASE houseprices; \
$ CREATE USER apu WITH ENCRYPTED PASSWORD 'tigerit'; \
$ GRANT ALL PRIVILEGES ON DATABASE houseprices to apu; \
$ ALTER USER apu WITH SUPERUSER; 

Or shortcut way to create a user \
$ CREATE ROLE apu WITH LOGIN SUPERUSER PASSWORD 'tigerit';

Build project: $ mvn clean package
Run: mvn spring-boot: run 

#Download and run elasticsearch
https://www.elastic.co/downloads/past-releases/elasticsearch-7-13-1

Go to the bin folder, for windows, execute elasticsearch.bat
The node will be started, and the node details will be shown as host:port, for example 127.0.0.1:9200

#Download and run kafka
https://kafka.apache.org/downloads
Version: 2.12-3.3.1.tgz
Extract and do copy all files into short path directory like D:\kafka 

Start the zookeeper first: \
$ .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

Then start the kafka server: \
.\bin\windows\kafka-server-start.bat .\config\server.properties


#Download and start redis server
Url: https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100
Download the Redis-x64-3.2.100.zip file
Run the redis-server.exe file

To get the elasticsearch node info:
Hit the url: http://127.0.0.1:9200/

To get all of the info about indices:
http://127.0.0.1:9200/_cat/indices

To get the info for a specific index:
http://127.0.0.1:9200/_cat/indices/<<index name>>

like : http://127.0.0.1:9200/_cat/indices/house_prices_textual_info

To get the data from an index use _search:
http://127.0.0.1:9200/<<index name>>/_search

Like http://127.0.0.1:9200/house_prices_textual_info/_search


Insert data API:
POST http://127.0.0.1:8080/service-api/api/house-prices \
BODY: filePath

Insert data through multi-threading based API:
POST http://127.0.0.1:8080/service-api/api/house-prices/save-by-multi-threading \
POST http://127.0.0.1:8080/service-api/api/house-prices \
BODY: filePath

Get data from pg API: GET http://127.0.0.1:8080/service-api/api/house-prices/search \
Get data from ES API: GET http://127.0.0.1:8080/service-api/api/house-prices/adv-search \
Get data from ES through multi-threading based API: http://127.0.0.1:8080/service-api/api/house-prices/get-all-by-multi-threading 


Note: here I have used he HousePrices Dataset from kaggle.
Url: https://www.kaggle.com/datasets/takahashiy/houseprice


Common Error: \
Caused by: org.postgresql.util.PSQLException:  
The authentication type 10 is not supported. Check that you have configured the pg_hba.conf file to include the client's IP address or subnet, and that it is using an authentication scheme supported by the driver.

Probable Solution: \
Open pg_hba.conf file location in windows: C:\Program Files\PostgreSQL\15\data

Update IPv4 local connections: \
host    all             all             127.0.0.1/32            scram-sha-256 \
to \
host    all             all             127.0.0.1/32            trust

Then restart the system

#Spring Batch Processing
for testing spring batch processing need to create all the tables related to it you can create it through flyway by defining schemas by yourselves
or setting ddl-auto: update

Spring Job creates transaction manager itself: collision between PostgressConfig and built in Simple Spring Job transactionManager needed to resolved it

