-- Voeg wat voorbeeldgebruikers toe
INSERT INTO users (username, password, role, created_at, updated_at)
VALUES
    ('user1', '$2a$10$VHpVf0UUGd4h7I9t7Ud5vOejBXhz.XgRgfMO2v60QSPZbAty5JjBa', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin1', '$2a$10$K7xEnp/hUKsw5lgRfZsXsXsIYwE4vBZsmfC0kwYCu/1xQ6A4HwM7S', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('user2', '$2a$10$y1mM5dyDccZ4t23m74jw/0es.FgxXbyMwrOgJ3nC7Hqrj1TdpURhWS', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Voeg tokens toe voor de gebruikers
INSERT INTO token (token, token_type, expired, revoked, user_id)
VALUES
    ('token_for_user1', 'BEARER', false, false, 35),
    ('token_for_admin1', 'BEARER', false, false, 36),
    ('token_for_user2', 'BEARER', false, false, 37);

-- Voeg voorbeeldafspraken toe aan de appointments tabel
INSERT INTO appointments (id, attachment, bicycle_name, date_time, description, user_id)
VALUES
    (1, 'Ketting', 'Mountain Bike', '2025-04-01 09:00:00', 'Onderhoud van de ketting van de mountainbike', 35),
    (2, 'Remmen', 'Racefiets', '2025-04-02 10:30:00', 'Vervangen van de remmen van de racefiets', 36),
    (3, 'Wiel', 'Stadsfiets', '2025-04-03 14:00:00', 'Controle en onderhoud van het wiel van de stadsfiets', 37);
