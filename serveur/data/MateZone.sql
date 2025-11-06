-- MateZone
-- Programmeur  : MateGROUP
-- Date         : 05/10/2025
-- Version      : 1.1
-- Description  : Création de la BD pour MateZone

-- +-----------------------+
-- +  SUPPRESSION TABLES   +
-- +-----------------------+

-- Suppression des tables dans l'ordre inverse (à cause des clés étrangères)
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS membres_groupes;
DROP TABLE IF EXISTS groupes;
DROP TABLE IF EXISTS clients;

-- +-----------------------+
-- +  CREATION DES TABLES  +
-- +-----------------------+

-- CLIENTS
CREATE TABLE IF NOT EXISTS clients 
(
	id         INT AUTO_INCREMENT PRIMARY KEY,
	pseudo     VARCHAR(255) NOT NULL UNIQUE,
	mdp        VARCHAR(255) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--  GROUPES
CREATE TABLE IF NOT EXISTS groupes 
(
	id       INT AUTO_INCREMENT PRIMARY KEY,
	nom      VARCHAR(150) NOT NULL UNIQUE,
	type     VARCHAR(20) NOT NULL DEFAULT 'groupe', -- 'groupe' ou 'prive'
	cree_par INT,
	cree_le  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (cree_par) REFERENCES clients(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- MESSAGES
CREATE TABLE IF NOT EXISTS messages 
(
	id            INT AUTO_INCREMENT PRIMARY KEY,
	groupe_id     INT NOT NULL,
	expediteur_id INT NOT NULL,
	contenu       TEXT NOT NULL,
	envoye_le     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (groupe_id) REFERENCES groupes(id) ON DELETE CASCADE,
	FOREIGN KEY (expediteur_id) REFERENCES clients(id),
	INDEX idx_messages_groupe (groupe_id),
	INDEX idx_messages_expediteur (expediteur_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table d'association clients <-> groupes (membres)
CREATE TABLE IF NOT EXISTS membres_groupes 
(
	id            INT AUTO_INCREMENT PRIMARY KEY,
	groupe_id     INT NOT NULL,
	client_id     INT NOT NULL,
	role          VARCHAR(50) DEFAULT 'membre', -- ex: proprietaire, admin, membre
	date_adhesion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UNIQUE KEY unique_membre (groupe_id, client_id),
	FOREIGN KEY (groupe_id) REFERENCES groupes(id) ON DELETE CASCADE,
	FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- +-----------------------+
-- +  INSERTIONS           +
-- +-----------------------+

-- INSERT : 3 clients demandés (Yuriko, Admin, Dono0530)
INSERT INTO clients (pseudo, mdp) VALUES
	( 'Admin'   , 'pwdAdmin'  ),
	( 'Yuriko'  , 'pwdYuriko' ),
	( 'Dono0530', 'pwdDono'   );

-- CREATE 2 GROUPES :
-- 1 privé (Yuriko + Dono0530) et 1 public (tous les users)
INSERT INTO groupes (nom, type, cree_par) VALUES
	( 'public'           , 'groupe', (SELECT id FROM clients WHERE pseudo='Admin' ) ),
	( 'prive_yuriko_dono', 'prive' , (SELECT id FROM clients WHERE pseudo='Yuriko') );

-- Membres des groupes
INSERT INTO membres_groupes (groupe_id, client_id, role) VALUES
	( (SELECT id FROM groupes WHERE nom='public'           ), (SELECT id FROM clients WHERE pseudo='Admin'   ), 'proprietaire'),
	( (SELECT id FROM groupes WHERE nom='public'           ), (SELECT id FROM clients WHERE pseudo='Yuriko'  ), 'membre'      ),
	( (SELECT id FROM groupes WHERE nom='public'           ), (SELECT id FROM clients WHERE pseudo='Dono0530'), 'membre'      ),

	( (SELECT id FROM groupes WHERE nom='prive_yuriko_dono'), (SELECT id FROM clients WHERE pseudo='Yuriko'  ), 'proprietaire'),
	( (SELECT id FROM groupes WHERE nom='prive_yuriko_dono'), (SELECT id FROM clients WHERE pseudo='Dono0530'), 'membre'      );

-- Quelques messages dans chaque groupe (public + privé)
INSERT INTO messages (groupe_id, expediteur_id, contenu)	VALUES
	( (SELECT id FROM groupes WHERE nom='public'), (SELECT id FROM clients WHERE pseudo='Admin'   ), 'Bienvenue sur le groupe public de MateZone !'),
	( (SELECT id FROM groupes WHERE nom='public'), (SELECT id FROM clients WHERE pseudo='Yuriko'  ), 'Salut à tous, ravie d''être ici.'            ),
	( (SELECT id FROM groupes WHERE nom='public'), (SELECT id FROM clients WHERE pseudo='Dono0530'), 'Hello tout le monde !'                       );

INSERT INTO messages (groupe_id, expediteur_id, contenu) VALUES
	( (SELECT id FROM groupes WHERE nom='prive_yuriko_dono'), (SELECT id FROM clients WHERE pseudo='Yuriko'  ), 'Salut Dono, on discute en privé maintenant.'),
	( (SELECT id FROM groupes WHERE nom='prive_yuriko_dono'), (SELECT id FROM clients WHERE pseudo='Dono0530'), 'Ok Yuriko, à toi.'                          );
