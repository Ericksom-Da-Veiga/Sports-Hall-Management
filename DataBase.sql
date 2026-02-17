create database salle_sports;

use salle_sports;

------------------------------------------------------ ADHERANT --------------------------------------------------
create table adherant(
	id bigint primary key auto_increment,
	cin varchar(15) unique,
    nom varchar(20),
    prenom varchar(20),
    mail varchar(50) unique,
    password varchar(100),
    date_naissance date,
    adress varchar(50),
    telephone varchar(15),
    ville varchar(30),
    nom_pere varchar(20),
    nom_mere varchar(20),
    tel_parant varchar(15),
    active bigint DEFAULT 1 -- 1 = active 0 = desactive
);


-- porcedures pour sasisir des valeur dans la table adherants
DELIMITER //
CREATE PROCEDURE add_adherant(
	in cin varchar(15),
    in mail varchar(50),
    in password varchar(100),
    in nom varchar(20),
    in prenom varchar(20),
    in date_naissance date,
    in adress varchar(50),
    in telephone varchar(15),
    in ville varchar(30),
    in nom_pere varchar(20),
    in nom_mere varchar(20),
    in tel_parant varchar(15)
    )
BEGIN 
	insert into adherants(cin, mail, password, nom, prenom, date_naissance, adress, telephone, ville, nom_pere, nom_mere, tel_parant) 
    values(cin, mail, password, nom, prenom, date_naissance, adress, telephone, ville, nom_pere, nom_mere, tel_parant );
END//
DELIMITER //

-- recuperer tous les adherants active
DELIMITER //
CREATE PROCEDURE findActiveAdherants()
BEGIN
	select id,cin, mail,password, nom, prenom, date_naissance, adress, telephone, ville, nom_pere, nom_mere, tel_parant, active 
    from adherant 
    where active = 1;
END//
DELIMITER //
-- Checher un adherant apartir d'un CIN ou d'un Nom 
DELIMITER //
CREATE PROCEDURE findAdherantByCinOrNom(
    in nom_adherant varchar(20)
    )
BEGIN
	select id,cin, mail,password, nom, prenom, date_naissance, adress, telephone, ville, nom_pere, nom_mere, tel_parant, active 
    from adherant 
    where cin LIKE CONCAT('%',nom_adherant, '%') OR nom LIKE CONCAT('%',nom_adherant, '%') OR prenom LIKE CONCAT('%',nom_adherant, '%');
END//
DELIMITER //



 --------------------------------------------------------------------COACH-------------------------------------------------------------

create table coach(
	id bigint auto_increment primary key, 
	cin varchar(15)unique,
    nom varchar(20),
    prenom varchar(20),
    date_entree date,
    telephone varchar(15),
    mail varchar(50) unique,
    adress varchar(100),
    idsport bigint,
    password varchar(100),
    constraint FK_coach_sport foreign key(idsport) references sports(id)
);

-- Chercher un coach apartir du CIN ou NOM
DELIMITER //
CREATE PROCEDURE findCoachByCinOrNom(
    in nom_coach varchar(20)
    )
BEGIN
	select id,cin, mail,password, nom, prenom, date_entree, adress, telephone, idsport
    from coach 
    where cin LIKE CONCAT('%',nom_coach, '%') OR nom LIKE CONCAT('%',nom_coach, '%') OR prenom LIKE CONCAT('%',nom_coach, '%');
END//
DELIMITER //

call findCoachByCinOrNom("a");

-- porcedures pour sasisir des valeur dans la table entraineur
DELIMITER //
CREATE PROCEDURE add_coach(
    cin varchar(15),
    nom varchar(20),
    prenom varchar(20),
    date_entree date,
    telephone varchar(15),
    mail varchar(50),
    adress varchar(100),
    idsport bigint,
    password varchar(100)
    )
BEGIN
	insert into coach (cin, nom, prenom, date_entree, telephone, mail, adress, password,idsport)
    values (cin, nom, prenom, date_entree, telephone, mail, adress, password, idsport);
END//
DELIMITER //



-- ----------------------------------------SPORTS----------------------------------------------------------------------------- 
create table sports(
	id bigint auto_increment,
    nom varchar(20),
    nmbr_max_seance_semaine bigint,
    prix double,
    primary key(id)
);
-- Quand on vas effacer un sport on doit effacer tous les registre avec cet sports dans la table entraineur_sports 
DELIMITER //
CREATE TRIGGER effacer_sports
BEFORE DELETE ON sports
FOR EACH ROW 
BEGIN
	-- delete from coach_sports where id_sports = OLD.id;
    delete from abonnement_sports where id_sports = OLD.id;
END//
DELIMITER //

-- Trouver un Sport apartir du nom
DELIMITER //
CREATE PROCEDURE findSportByNom(
    in nom_sport varchar(20)
    )
BEGIN
	select id,nom, nmbr_max_seance_semaine,prix 
    from sports
    where nom LIKE CONCAT('%',nom_sport, '%');
END//
DELIMITER //

-- procedure pour ajouter un sport 
DELIMITER //
CREATE PROCEDURE add_sports(
	in nom varchar(20),
    in nmbr_max_seance_semaine bigint,
    in prix double
    )
BEGIN
	insert into sports(nom, nmbr_max_seance_semaine, prix)
    values(nom, nmbr_max_seance_semaine, prix);
END//
DELIMITER //


-- Lister les sports pour chaque coach
DELIMITER //

CREATE PROCEDURE lister_sports_pour_coach(
	in id bigint
    )
BEGIN
	SELECT s.nom 
	FROM sports s 
	JOIN coach_sports c ON s.id = c.id_sports
	WHERE c.id_coach = id;
END //

DELIMITER //
-- pour iniciatilizer la procedure dans sql: CALL nom_procedure(valeur);

call lister_sports_pour_coach(1);



-- ----------------------------------------------------------ABONNEMENT-----------------------------------------------------------------------
create table abonnement(
	id bigint auto_increment,
    id_adherant bigint,
    date_debut date,
    date_fin date,
    prix_totale double,
    active bigint DEFAULT 1, -- 1 = active 0 = desactive
    primary key(id),
    constraint FK_abonnement_adherant foreign key(id_adherant) references adherant(id)
);

-- Apres avoir desactive un adherant on vas desactiver aussi l'abonnement dasn l'aquel il apartien 
DELIMITER //
CREATE PROCEDURE delete_abonnement(
	in id_adherant bigint
)
BEGIN
	update abonnement set active = 0 where id = id_adherant;
END //
DELIMITER //

-- porcedures pour ajouter un nouveau abonnement

DELIMITER //
CREATE PROCEDURE add_abonnement(
	in id bigint,
    in id_adherant bigint,
    in date_debut date,
    in date_fin date,
    in prix_totale double
    )
BEGIN
	insert into abonnement (id, id_adherant, date_debut, date_fin, prix_totale)
    values (id, id_adherant, date_debut, date_fin, prix_totale);
END//
DELIMITER //

DELIMITER //
CREATE PROCEDURE findActiveAbonnements()
BEGIN
	select abonnement.id, abonnement.id_adherant, abonnement.date_debut, abonnement.date_fin, abonnement.prix_totale, abonnement.active
    from abonnement 
    where abonnement.active =1;
END//
DELIMITER //


-- ----Trouver un abonnement atraver sont id 
DELIMITER //
CREATE PROCEDURE findAbonnementById(
	in id_abonnement bigint
)
BEGIN
	select abonnement.id, abonnement.id_adherant, abonnement.date_debut, abonnement.date_fin, abonnement.prix_totale, abonnement.active
    from abonnement 
    where abonnement.id = id_abonnement;
END//
DELIMITER //

 -- trouver un abonnement aprtir du nom ou cin du adherant
 DELIMITER // 
 create Procedure findAbonnementByCinOrNom(
	in data varchar(50)
 )
 BEGIN
	select abonnement.id, abonnement.id_adherant, abonnement.date_debut, abonnement.date_fin, abonnement.prix_totale, abonnement.active
    from abonnement join adherant on abonnement.id_adherant = adherant.id
    where adherant.cin LIKE CONCAT('%',data, '%') or adherant.nom LIKE concat("%",data,"%") or  adherant.prenom LIKE concat("%",data,"%");
 END//
 DELIMITER //
 
 -- trouver un abonnement avec le CIN seulement
 DELIMITER // 
 create Procedure findAbonnementByCinOnly(
	in data varchar(50)
 )
 BEGIN
	select abonnement.id, abonnement.id_adherant, abonnement.date_debut, abonnement.date_fin, abonnement.prix_totale, abonnement.active
    from abonnement join adherant on abonnement.id_adherant = adherant.id
    where adherant.cin LIKE data;
 END//
 DELIMITER //

-- Recuperer la liste des abonnements que votn terminer dans cet mois
 DELIMITER //
 create procedure findAbonnementPresqueFini()
 BEGIN
	 SELECT *
	FROM abonnement
	WHERE active = 1 AND date_fin BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY);
 END //
 DELIMITER //
 
 
 -- Recuperer le nombres d'abonnements active
DELIMITER //
CREATE PROCEDURE CountAbonnements()
BEGIN    
    SELECT COUNT(*)
    FROM abonnement a
    WHERE active = 1;
END //
DELIMITER ;

call CountAbonnements();
 -- --------------------------------------------------PAYEMENT-----------------------------------------------------------------------------
create table payement(
	id bigint auto_increment,
    date_payement date,
    id_abonnement bigint,
    quant_recu double,  -- argent reçu
    rendu double, -- argent reçu - prix total
	cin_adherant varchar(15),
    primary key(id),
    constraint FK_payement_abonnement foreign key(id_abonnement) references abonnement(id),
    constraint FK_payement_adherant foreign key(cin_adherant) references adherant(cin)
);

-- procedure pour ajouter un payement 
DELIMITER //
Create procedure add_payement(
	in data_payement date,
    in id_abonnement bigint,
    in espece double,
    in rendu double
    )
BEGIN
	insert into payement(date_payement, id_abonnement,spece,rendu)
    values (date_payement, id_abonnement, espece, rendu);
END//
DELIMITER //

-- procedure pour trouver un Payements en utilisant le CIN adherant
create procedure findPayement(
	in cin varchar(20)
	)
BEGIN
	
-- ----------------------------------------ABONNEMENT_SPORTS---------------------------------------------------------------------   
create table abonnement_sports(
	id bigint auto_increment,
	id_sports bigint,
    id_abonnement bigint,
    primary key(id),
	constraint FK_AS_abonnement foreign key(id_abonnement) references abonnement(id),
	constraint FK_AS_sports foreign key(id_sports) references sports(id)
);

-- Procedure pour supprimer un un eregistrement dans abonnement_sports
DELIMITER //
CREATE PROCEDURE delete_aboonement_sports(
	in id bigint
    )
BEGIN
	delete from abonnement_sports where id_abonnement = id;
END //
DELIMITER //

-- porcedures pour ajouter un nouveau abonnement_sports
DELIMITER //
CREATE PROCEDURE add_abonnement_sports(
    in id_sports bigint,
    in id_abonnement bigint
    )
BEGIN
	insert into abonnement_sports (id_sports, id_abonnement)
    values (id_sports, id_abonnement);
END//
DELIMITER //


-----------------------------------------USER-------------------------------------------------------------------------------
create table users(
	id bigint auto_increment primary key,
	cin varchar(15) unique,
    mail varchar(50),
    password varchar(100),
    nom varchar(20),
    prenom varchar(20),
    telephone varchar(15),
    role varchar(10)  -- ADMIN OR SUPER ADMIN
);
-- procedure pour ajouter un USER 
DELIMITER //
CREATE PROCEDURE add_user(
	in cin varchar(15),
    in mail varchar(50),
    in password varchar(100),
    in nom varchar(20),
    in prenom varchar(20),
    in telephone varchar(15),
    in role varchar(10)  -- ADMIN OR SUPER ADMIN
    )
BEGIN
	insert into users(cin, mail, password, nom, prenom, telephone, role)
    values (cin, mail, password, nom, prenom, telephone, role);
END //
DELIMITER //

-- procedure pour effacer un user 
DELIMITER //
CREATE PROCEDURE delete_user(
	in id bigint
    )
BEGIN
	delete from users where id = id;
END //
DELIMITER //

call add_user("A07219L","super.admin@admin.com","$2a$12$m1PmgeH/hsejhcPuCZonKO8DaR4SNngLzBjBTLEURQforNL9T.b0u","Erickson", "Da Veiga","07281932","ROLE_ADMIN");

-- ------------------------------------------------------------------------------------------------------------------
DELIMITER //
CREATE PROCEDURE ShowStatsForCurrentMonth()
BEGIN
    -- Número de abonnements ativos
		SELECT COUNT(*) AS ActiveAbonnements
		FROM abonnement
		WHERE active = 1;

		-- Número de adherants ativos
		SELECT COUNT(*) AS ActiveAdherants
		FROM adherant
		WHERE active = 1;

		-- Total de dinheiro arrecadado este mês
		SELECT SUM(quant_recu) AS TotalMoneyCollected
		FROM payement
		WHERE MONTH(date_payement) = MONTH(CURDATE())
		  AND YEAR(date_payement) = YEAR(CURDATE());
END //
DELIMITER ;

call ShowStatsForCurrentmonth()
-- -----------------------------LES TRIGGERS ----------------------------------------------------------------------------------------------
-- Trigger pour effacer les registre de la table entraineur_sports quand on vas effacer l'entraineur
-- DELIMITER //
-- CREATE TRIGGER effacer_coach 
-- BEFORE DELETE ON coach 
-- FOR EACH ROW
-- BEGIN
-- 	delete from coach_sports where id =  OLD.id;
-- END//
-- DELIMITER //

-- avant de effacer un abonnement on doit effacer tous les registre dans la table abonnement_sports avec le meme id_abonnement -

-- porcedures pour sasisir des valeur dans la table entraineur_sports 
-- DELIMITER //
-- CREATE PROCEDURE add_coach_sports(
--     in id_sports bigint,
--     in id_coach bigint
--     )
-- BEGIN
-- 	insert into coach_sports (id_sports, id_coach)
--     values (id_sports, id_coach);
-- END//
-- DELIMITER //


-- -------------------------TESTES ----------------------------------------------------------------------------------------------------------------------------------------------------
-- pour afficher tous les triggers
SELECT TRIGGER_NAME, EVENT_MANIPULATION, EVENT_OBJECT_TABLE, ACTION_STATEMENT, ACTION_TIMING 
FROM information_schema.TRIGGERS;

-- pour effacer um trigger
DROP TRIGGER IF EXISTS effacer_sports;



CALL findActiveAdherants()

SELECT * FROM coach

SELECT a.id, a.id_adherant, a.date_debut, a.date_fin, a.prix_totale FROM abonnement a WHERE a.active = 1

ALTER TABLE abonnement
DROP COLUMN id_sports,
DROP COLUMN id_abonnement;

select * from abonnement_sports

ALTER TABLE payement
ADD COLUMN quant_recu double;

DELETE FROM `salle_sports`.`abonnement` WHERE `active` = 0;

rename table user to users;