INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Villa normande avec vue mer', 'VENTE', 'MAISON', 890000, 'Deauville', 'Front de mer', 6, 180, 'Villa a colombages typique du bord de mer normand, terrasse et vue degagee.', '/img/properties/villa-normande.jpg'
WHERE NOT EXISTS (SELECT 1 FROM property);

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Maison de caractere en briques', 'VENTE', 'MAISON', 520000, 'Lyon', 'Croix-Rousse', 5, 140, 'Maison ancienne en briques pleine de cachet, proche des commerces et du tramway.', '/img/properties/maison-caractere.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Villa provencale fleurie', 'VENTE', 'MAISON', 610000, 'Aix-en-Provence', 'Centre historique', 4, 95, 'Villa lumineuse avec jardin et bougainvilliers, a deux pas du centre historique.', '/img/properties/villa-fleurie.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Appartement vue panoramique', 'LOCATION', 'APPARTEMENT', 2200, 'Paris', 'Montparnasse', 3, 72, 'Appartement en hauteur avec vue degagee sur la ville, immeuble recent et securise.', '/img/properties/tour-panoramique.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;
