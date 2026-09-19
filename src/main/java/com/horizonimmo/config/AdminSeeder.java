package com.horizonimmo.config;

import com.horizonimmo.model.AdminUser;
import com.horizonimmo.repository.AdminUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cree le compte agent par defaut au premier demarrage, si la table est vide.
 *
 * Identifiants : admin@immobilier.anaick.com / 1234567890
 * TODO ecole : mot de passe volontairement simple pour la demo, a changer
 * avant tout usage reel (pas de politique de mot de passe ici).
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final String DEFAULT_EMAIL = "admin@immobilier.anaick.com";
    private static final String DEFAULT_PASSWORD = "1234567890";

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() == 0) {
            AdminUser admin = new AdminUser();
            admin.setEmail(DEFAULT_EMAIL);
            admin.setPasswordHash(passwordEncoder.encode(DEFAULT_PASSWORD));
            adminUserRepository.save(admin);
        }
    }
}
