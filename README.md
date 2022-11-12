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

Go to the bin folder, for windows execute elasticsearch.bat
the node start like 127.0.0.1:9200

To get the node info:
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

Get data from pg API: GET http://127.0.0.1:8080/service-api/api/house-prices/search \
Get data from ES API: GET http://127.0.0.1:8080/service-api/api/house-prices/adv-search


Note: here I have used he HousePrices Dataset from kaggle.
Url: https://www.kaggle.com/datasets/takahashiy/houseprice


Common Error: \
Caused by: org.postgresql.util.PSQLException:  
The authentication type 10 is not supported. Check that you have configured the pg_hba.conf file to include the client's IP address or subnet, and that it is using an authentication scheme supported by the driver.

Probable Solution: \
Open postgresql.conf file location in windows: C:\Program Files\PostgreSQL\15\data

Update: \
host    all             all             127.0.0.1/32            scram-sha-256 \
to \
host    all             all             127.0.0.1/32            trust

Then restart the system
