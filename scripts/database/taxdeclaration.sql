CREATE TABLE public.users (
	id bigserial NOT NULL,
	"password" varchar(120) NULL,
	username varchar(20) NULL,
	tin varchar(9) NULL,
	CONSTRAINT uk61pawc0o3qllm6qvjegj55kvs UNIQUE (tin),
	CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

INSERT INTO public.users ("password",username,tin) VALUES
	 ('$2a$10$T0FcEsja4sgTIDJT4w/lfeqlPKw8AB.YrN6KYoMk3Ri6IohGk2LWi','admin',NULL),
	 ('$2a$10$wuHE1efvJW3th3xn87/vV.B/nrHYfs5umiy.VhV/timWm/7H0gebu','a12','111111112'),
	 ('$2a$10$aPw604g8uBRPw8zTFAj5Eub68U5efIjrAO7J8yDMxQqYlfZjwjz0u','a14','111111114'),
	 ('$2a$10$s9f940ajYRXVE/buU5/vxeRDVxiyb.gzyTxAnR/f5cQtPFQ.dfOcG','a14ff','311111114');
	 
CREATE TABLE public.roles (
	id serial4 NOT NULL,
	"name" varchar(20) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

INSERT INTO public.roles ("name") VALUES
	 ('ROLE_ADMIN'),
	 ('ROLE_CITIZEN'),
	 ('ROLE_NOTARY');
	 

CREATE TABLE public.user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id),
	CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id)
);

INSERT INTO public.user_roles (user_id,role_id) VALUES
	 (1,3),
	 (20,1),
	 (22,1),
	 (21,2);
	 
	 
CREATE TABLE public.persons (
	address varchar(50) NULL,
	doy varchar(30) NULL,
	firstname varchar(30) NULL,
	lastname varchar(30) NULL,
	tin varchar(9) NOT NULL,
	CONSTRAINT persons_pk PRIMARY KEY (tin),
	CONSTRAINT ukgdbbv2j9cliy5wlmnv5070yvm UNIQUE (tin)
);


INSERT INTO public.persons (address,doy,firstname,lastname,tin) VALUES
	 ('fsdf','k','k','k','111111112'),
	 ('ss ss 44','lamias','a123456','aaasdd','111111111');
	 
CREATE TABLE public.declarations (
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
	CONSTRAINT declarations_fk_n FOREIGN KEY (notarytin) REFERENCES public.persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT declarations_fk_p FOREIGN KEY (purchasertin) REFERENCES public.persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT declarations_fk_s FOREIGN KEY (sellertin) REFERENCES public.persons(tin) ON DELETE RESTRICT ON UPDATE CASCADE
);


INSERT INTO public.declarations (contractdetails,notarytin,paymentmethod,propertycategory,propertydescription,propertynumber,purchaseracceptance,purchasertin,selleracceptance,sellertin,status,tax) VALUES
	 ('','111111112','','a111','ssas111','111',false,'111111111',false,'111111112',0,11.12),
	 ('','111111112','','a111','ssas111','111',false,'111111111',false,'111111112',0,11.12),
	 ('','111111112','','a111','ssas111','111',false,'111111111',false,'111111112',0,11.12),
	 ('','111111112','','a111','ssas111','111',false,'111111111',false,'111111112',0,11.12),
	 ('','111111112','','a111','ssas111','111',false,'111111111',false,'111111112',0,11.12),
	 ('','111111112','','a111','ssas111','111',false,'111111112',false,'111111111',0,11.12),
	 ('111/11-11-1111','111111112','a111','a111','ssas111','111',true,'111111112',true,'111111112',1,11.12);
	 
	 

