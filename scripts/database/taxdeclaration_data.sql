INSERT INTO public.users ("password",username,tin) VALUES
 ('$2a$10$WZ25rth1lA5QCIYn6.58XuGTbQvpB/JNUVC2BSADTTlxF/t1NXgJ2','user1','111111111'),
 ('$2a$10$Z27gBZTeJvmOEeMaqU99/uPZcIipwcXuKMxgq1LSdsDn8yGGXKPj.','user2','111111112'),
 ('$2a$10$xJno7e12ORPLZWYVIRVyn.2qXZGtlfLVCMBWhbLtvy4nZAYjDOl46','user4','111111114'),
 ('$2a$10$LMahJ.rH2rOz8otIy0/KeuUjQykRawzkWl/Q3u.tYBy92wDfiqzHq','user6','111111116'),
 ('$2a$10$mgd3o05Q4gLwepzoExp/iOZ4pYy2oQZuA3EZWKh89Ik7XCWsssCJq','user3','111111113'),
 ('$2a$10$4IL5HIe79Jj2/3EPCVBg/..kDMPJ3FS5EM/neTkF6kTujmOxA/hKm','user5','111111115');

INSERT INTO public.user_roles (user_id,role_id) VALUES (2,2), (3,2), (4,2), (5,2), (7,3), (6,3);

INSERT INTO public.persons (address,doy,firstname,lastname,tin) VALUES
 ('2st Street 1','1H Athinon ','Jane','Do','111111112'),
 ('3st Street 1','2H Athinon','John','John','111111113'),
 ('4st Street 1','Larisas','Nick','Papas','111111114'),
 ('5st Street 1','Volou','Tom','Lee','111111115'),
 ('6st Street 1','Patras','Sue','You','111111116'),
 ('1st Street 1','Lamias','Jo','Do','111111111');

INSERT INTO public.declarations (contractdetails,notarytin,paymentmethod,propertycategory,propertydescription,propertynumber,purchaseracceptance,purchasertin,selleracceptance,sellertin,status,tax) VALUES
 ('','111111116','','House','Detashed House','111112',false,'111111115',false,'111111114',0,1000.5),
 ('','111111115','','House','Detashed House','1111111',false,'111111113',false,'111111112',0,1000.5),
 ('1234/11-11-2022','111111116','Cash','Shop','Shop with Basement','1111124',true,'111111112',true,'111111116',1,2000.5),
 ('','111111116','','Shop','Shop with Basement','1111124',true,'111111116',false,'111111112',0,2000.5);