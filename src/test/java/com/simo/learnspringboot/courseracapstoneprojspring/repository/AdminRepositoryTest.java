package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void shouldCreateAdmin() {
        // Test logic to create an admin
        String username = "simo";
        String fullName = "mohamed khalis";
        String password = "somepassword";
        Admin admin = new Admin(username, password, fullName);

        Admin result = adminRepository.save(admin);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getFullName()).isEqualTo(fullName);
        assertThat(result.getPassword()).isEqualTo(password);
    }
}
