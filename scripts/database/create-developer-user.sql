-- TODO: CREATE USER FOR WEB APPLICATION

CREATE USER developer WITH ENCRYPTED PASSWORD 'pass123';
GRANT ALL PRIVILEGES ON DATABASE REAL_ESTATE_DB TO DEVELOPER;