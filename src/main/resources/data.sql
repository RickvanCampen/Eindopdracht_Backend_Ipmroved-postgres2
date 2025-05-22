INSERT INTO users (username, password, role, created_at, updated_at) VALUES
                                                                         ('Admin1', '$2a$10$6/SD91Nv/LRte7wphk2RS.CBMFCGiw5mAg9kY0iLIPC8q5zOf1Fda', 'ADMIN', NOW(), NOW()),
                                                                         ('Admin2', '$2a$10$6/SD91Nv/LRte7wphk2RS.CBMFCGiw5mAg9kY0iLIPC8q5zOf1Fda', 'ADMIN', NOW(), NOW()),
                                                                         ('User1',  '$2a$10$brh1Au48wdMO4X8MiF6QWuV3Nr9kKagTRpfOi.1H6i2Y2aAfztOpC', 'USER', NOW(), NOW()),
                                                                         ('User2',  '$2a$10$brh1Au48wdMO4X8MiF6QWuV3Nr9kKagTRpfOi.1H6i2Y2aAfztOpC', 'USER', NOW(), NOW()),
                                                                         ('User3',  '$2a$10$brh1Au48wdMO4X8MiF6QWuV3Nr9kKagTRpfOi.1H6i2Y2aAfztOpC', 'USER', NOW(), NOW()),
                                                                         ('User4',  '$2a$10$brh1Au48wdMO4X8MiF6QWuV3Nr9kKagTRpfOi.1H6i2Y2aAfztOpC', 'USER', NOW(), NOW());
INSERT INTO appointments (bicycle_name, description, attachment, date_time, user_id) VALUES
                                                                                        ('Stadsfiets', 'Remmen vervangen', NULL, '2025-05-01 14:00', 3),
                                                                                        ('Bakfiets', 'Achterband vervangen', NULL, '2025-05-02 10:30', 4),
                                                                                        ('Racefiets', 'Jaarlijkse onderhoudsbeurt', NULL, '2025-05-03 09:00', 5),
                                                                                        ('Elektrische fiets', 'Accu controleren', NULL, '2025-05-04 15:00', 6),
                                                                                        ('Vouwfiets', 'Versnellingen afstellen', NULL, '2025-05-05 11:00', 3),
                                                                                        ('Omafiets', 'Frame inspecteren', NULL, '2025-05-06 13:30', 4);
