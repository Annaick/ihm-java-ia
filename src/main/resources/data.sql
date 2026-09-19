INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Villa avec jardin tropical', 'VENTE', 'MAISON', 450000000, 'Antananarivo', 'Ivandry', 5, 200, 'Villa moderne avec grand jardin fleuri et vue degagee, quartier residentiel prise d''Ivandry.', '/img/properties/villa-jardin-tropical.jpg'
WHERE NOT EXISTS (SELECT 1 FROM property);

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Maison coloniale de caractere', 'VENTE', 'MAISON', 280000000, 'Antananarivo', 'Analakely', 4, 150, 'Maison ancienne de style colonial en briques, proche du centre-ville et des commerces.', '/img/properties/maison-coloniale-analakely.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Appartement vue sur la Haute Ville', 'LOCATION', 'APPARTEMENT', 1200000, 'Antananarivo', 'Antaninarenina', 3, 85, 'Appartement lumineux avec vue panoramique sur la Haute Ville et son clocher, proche d''Analakely.', '/img/properties/vue-hauteville.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;

INSERT INTO property (title, transaction_type, property_type, price, city, zone, rooms, surface, description, photo_url)
SELECT 'Maison de ville a Toamasina', 'VENTE', 'MAISON', 180000000, 'Toamasina', 'Centre-ville', 4, 130, 'Maison en briques proche du port, a deux pas des commerces et de la gare.', '/img/properties/maison-toamasina.jpg'
WHERE (SELECT COUNT(*) FROM property) < 4;
