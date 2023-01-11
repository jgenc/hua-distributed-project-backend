/* database taxdeclaration*/

CREATE TABLE users (
	id bigserial NOT NULL,
	"password" varchar(120) NULL,
	username varchar(20) NULL,
	tin varchar(9) NULL,
	CONSTRAINT tin_key UNIQUE (tin),
	CONSTRAINT username_key UNIQUE (username),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

INSERT INTO users ("password",username,tin) VALUES
	 ('$2a$10$fCKXivkCv1YL8YR9FvD37eVBlL2YYAHx9I.CHCc/6JKQoh0RJpOGC','admin','admin'); /* password admin */

CREATE TABLE roles (
	id serial4 NOT NULL,
	"name" varchar(20) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

INSERT INTO roles ("name") VALUES
	 ('ROLE_ADMIN'),
	 ('ROLE_CITIZEN'),
	 ('ROLE_NOTARY');
	 

CREATE TABLE user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT roles_fk FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO public.user_roles (user_id,role_id) VALUES (1,1);

CREATE TABLE persons (
	address varchar(50) NULL,
	doy varchar(30) NULL,
	firstname varchar(30) NULL,
	lastname varchar(30) NULL,
	tin varchar(9) NOT NULL,
	CONSTRAINT persons_pk PRIMARY KEY (tin),
	CONSTRAINT tin_key UNIQUE (tin)
);

CREATE TABLE declarations (
	id bigserial NOT NULL,
	contractdetails varchar(25) NULL,
	notarytin varchar(9) NULL,
	paymentmethod varchar(30) NULL,
	propertycategory varchar(15) NULL,
	propertydescription varchar(100) NULL,
	propertynumber varchar(15) NULL,
	purchaseracceptance bool NULL,
	purchasertin varchar(9) NULL,
	selleracceptance bool NULL,
	sellertin varchar(9) NULL,
	status int4 NULL,
	tax float4 NULL,
	CONSTRAINT declarations_pkey PRIMARY KEY (id),
	CONSTRAINT declarations_fk_n FOREIGN KEY (notarytin) REFERENCES persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT declarations_fk_p FOREIGN KEY (purchasertin) REFERENCES persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT declarations_fk_s FOREIGN KEY (sellertin) REFERENCES persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE
);

