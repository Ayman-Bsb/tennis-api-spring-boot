-- Player
CREATE SEQUENCE player_id_seq;

CREATE TABLE player
(
    id integer NOT NULL DEFAULT nextval('player_id_seq'),
    last_name character varying(50) NOT NULL,
    first_name character varying(50) NOT NULL,
    birth_date date NOT NULL,
    position integer NOT NULL,
    points integer NOT NULL,
    PRIMARY KEY(id)
);

ALTER SEQUENCE player_id_seq OWNED BY player.id;

ALTER TABLE IF EXISTS public.player OWNER to postgres;


INSERT INTO public.player(last_name, first_name, birth_date, position, points)
        VALUES ('Nadal', 'Rafael', '1986-06-03', 1, 5000);

INSERT INTO public.player(last_name, first_name, birth_date, position, points)
    	VALUES ('Djokovic', 'Novak', '1987-05-22', 2, 4000);

INSERT INTO public.player(last_name, first_name, birth_date, position, points)
    	VALUES ('Federer', 'Roger', '1981-08-08', 3, 3000);

INSERT INTO public.player(last_name, first_name, birth_date, position, points)
    	VALUES ('Murray', 'Andy', '1987-05-15', 4, 2000);
    	
-- User
CREATE SEQUENCE user_id_seq;

CREATE TABLE app_user (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    login character varying(50) NOT NULL,
    password character varying(60) NOT NULL,
    last_name character varying(50) NOT NULL,
    first_name character varying(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER SEQUENCE user_id_seq OWNED BY player.id;

ALTER TABLE IF EXISTS public.app_user OWNER to postgres;

INSERT INTO public.app_user(login, password, last_name, first_name)
	VALUES
	('admin', '$2a$12$RkcdJn2kLrAS9fmvDv/CWehqID8nB3XBWXOtazhQ2PY1ZFwDB3L76', 'app', 'Admin'),
	('user', '$2a$12$VRnUGZfeEsWHG9jb7NyvQuhpISK65N2LtWyqXAi5t1CBWIQ34uRNa', 'Doe', 'John');

-- Role
CREATE TABLE app_role
(
    name character varying(50) NOT NULL,
    PRIMARY KEY (name)
);

INSERT INTO public.app_role(name)
	VALUES
	('ROLE_ADMIN'),
	('ROLE_USER');

ALTER TABLE IF EXISTS public.app_role OWNER to postgres;

-- user_role (associative table)
CREATE TABLE app_user_role
(
    user_id bigint NOT NULL,
    role_name character varying(50) NOT NULL,
    CONSTRAINT app_user_role_pkey PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_role_name FOREIGN KEY (role_name)
        REFERENCES public.app_role (name),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES public.app_user (id)
);

ALTER TABLE IF EXISTS public.app_user_role OWNER to postgres;

INSERT INTO public.app_user_role(user_id, role_name)
	VALUES
	(1, 'ROLE_ADMIN'),
	(1, 'ROLE_USER'),
	(2, 'ROLE_USER');