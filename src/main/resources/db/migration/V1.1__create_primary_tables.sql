DROP TABLE IF EXISTS LOOKUP_CANCELLATION_TYPE;
CREATE TABLE LOOKUP_CANCELLATION_TYPE (
    ID              serial not null,
    type	        varchar(32) not null,
    primary key (ID)
);

DROP TABLE IF EXISTS LOOKUP_ROOM_TYPE;
CREATE TABLE LOOKUP_ROOM_TYPE (
    ID              serial not null,
    type	        varchar(32) not null,
    primary key (ID)
);


DROP TABLE IF EXISTS LOOKUP_BED_TYPE;
CREATE TABLE LOOKUP_BED_TYPE (
    ID              serial not null,
    type	        varchar(32) not null,
    primary key (ID)
);


DROP TABLE IF EXISTS LOOKUP_PROPERTY_TYPE;
CREATE TABLE LOOKUP_PROPERTY_TYPE (
    ID              serial not null,
    NAME	        varchar(64) not null,
    primary key (ID)
);

DROP TABLE IF EXISTS LOOKUP_CITY_CODE;
CREATE TABLE LOOKUP_CITY_CODE (
    ID              serial not null,
    CODE	        varchar(16) not null,
    primary key (ID)
);

DROP TABLE IF EXISTS LOOKUP_NEIGHBOURHOOD_CITY;
CREATE TABLE LOOKUP_NEIGHBOURHOOD_CITY (
    ID              serial not null,
    NAME	        varchar(255) not null,
    primary key (ID)
);

DROP TABLE IF EXISTS LOOKUP_ZIPCODE;
CREATE TABLE LOOKUP_ZIPCODE (
    ID              serial not null,
    CODE	        varchar(32) not null,
    primary key (ID)
);

DROP TABLE IF EXISTS HOUSE_PRICES;
CREATE TABLE HOUSE_PRICES (
    id                      bigserial not null,
    amenities               text,
    accommodates            int,
    bathrooms               float(8),
    bed_type_id             smallint,
    bedrooms                int,
    beds                    int,
    cancellation_policy_id  smallint,
    city_id                 int,
    cleaning_fee            smallint,
    description             text,
    first_review            date,
    host_has_profile_pic    smallint,
    host_identity_verified  smallint,
    host_response_rate_percent  float(8),
    host_since              date,
    instant_bookable        smallint,
    last_review             date,
    latitude                float(8),
    longitude               float(8),
    name                    varchar(255),
    neighbourhood_id        int,
    number_of_review        int,
    property_type_id           smallint,
    review_scores_rating    int,
    room_type_id               smallint,
    thumbnail_url           varchar(255),
    zipcode_id              int,
    y_number_of_persons_want int,
    primary key (id)
);


DROP TABLE IF EXISTS PG_ES_TEMP_TABLE;
CREATE TABLE PG_ES_TEMP_TABLE (
    ID                      serial not null,
    HOUSE_PRICE_TABLE_ID    bigint not null,
    AMENITIES               text,
    DESCRIPTION             text,
    NAME	                varchar(255),
    primary key (ID)
);
