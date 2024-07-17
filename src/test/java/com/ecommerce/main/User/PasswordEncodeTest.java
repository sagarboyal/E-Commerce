package com.ecommerce.main.User;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncodeTest {
    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "12345";
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        boolean flag = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        assertThat(flag).isTrue();
    }
}
