INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Appartement lumineux avec balcon', 'VENTE', 'APPARTEMENT', 245000, 'Lyon', 'Croix-Rousse', 3, 65, 'Bel appartement traversant au 3eme etage, balcon expose sud, proche des commerces.', 'https://picsum.photos/seed/horizon1/480/320'
WHERE NOT EXISTS (SELECT 1 FROM property);

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Maison avec jardin', 'VENTE', 'MAISON', 380000, 'Lyon', 'Monplaisir', 5, 110, 'Maison familiale avec jardin clos, garage, a deux pas du tramway.', 'https://picsum.photos/seed/horizon2/480/320'
WHERE (SELECT COUNT(*) FROM property) < 5;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Studio etudiant renove', 'LOCATION', 'STUDIO', 650, 'Lyon', 'Part-Dieu', 1, 22, 'Studio entierement renove, proche des transports et de la gare.', 'https://picsum.photos/seed/horizon3/480/320'
WHERE (SELECT COUNT(*) FROM property) < 5;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Appartement 2 pieces avec vue', 'LOCATION', 'APPARTEMENT', 890, 'Lyon', 'Confluence', 2, 45, 'Appartement moderne dans une residence recente, vue degagee.', 'https://picsum.photos/seed/horizon4/480/320'
WHERE (SELECT COUNT(*) FROM property) < 5;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Duplex avec terrasse', 'VENTE', 'APPARTEMENT', 420000, 'Lyon', 'Croix-Rousse', 4, 95, 'Duplex avec grande terrasse plein sud et vue sur les toits.', 'https://picsum.photos/seed/horizon5/480/320'
WHERE (SELECT COUNT(*) FROM property) < 5;
