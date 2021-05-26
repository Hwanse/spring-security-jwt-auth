INSERT INTO USERS (NAME, PASSWORD, NICKNAME, EMAIL, CREATE_AT, MODIFIED_AT, DELETED_AT, BE_DELETED)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin','admin@gmail.com', NOW(), null, null , false);

INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USERS_ID, AUTHORITY_ID) values (1, 1);
INSERT INTO USER_AUTHORITY (USERS_ID, AUTHORITY_ID) values (1, 2);