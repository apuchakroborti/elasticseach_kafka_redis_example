DROP TABLE IF EXISTS customers_info;
CREATE TABLE customers_info (
  customer_id int NOT NULL,
  contact varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  dob varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  gender varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (customer_id)
);