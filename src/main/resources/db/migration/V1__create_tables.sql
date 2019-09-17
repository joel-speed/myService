SET SCHEMA 'myservice';

BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS todo
(
    id                  SERIAL PRIMARY KEY,
    title               TEXT,
    Contract_date       DATE,
    Org_Num             TEXT,
    email               TEXT,
    Locality            TEXT,
    Priceplan           TEXT,
    Contract_Period     TEXT,
    Sales_Source        TEXT,
    Payment_Method      TEXT,
    Cust_Id             TEXT,
    Swb_DataorgId       TEXT,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO todo

VALUES
(1,
'SpeedLedger',
'2019-09-17',
'1234567890',
'speedledger@visma.com',
'Göteborg',
'SWB20160101',
'TWELVE MONTHS',
'SWEDBANK',
'INVOICE',
'12345',
'54321');

COMMIT TRANSACTION;
