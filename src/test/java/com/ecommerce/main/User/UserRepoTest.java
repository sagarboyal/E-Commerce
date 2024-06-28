package com.ecommerce.main.User;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.entity.User;
import com.ecommerce.main.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepoTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        Role roleAdmin = entityManager.find(Role.class, 1); // that will find the role from role_db and assign to the user to map it
        User user = new User(
                "demo1@gmail.com",
                "12345",
                "demo",
                "demo",
                true
        );
        user.addRoles(roleAdmin);
        User data = userRepository.save(user);
        assertThat(data.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateUserWithMultipleRoles(){
        User user = new User(
                "demo3@gmail.com",
                "12345",
                "demo",
                "demo",
                true
        );
        user.addRoles(
                entityManager.find(Role.class, 1)
        );
        user.addRoles(
                new Role(3) //-------> Another Way using equals and hashcode
        );
        User data = userRepository.save(user);
        assertThat(data.getId()).isGreaterThan(0);
    }
    @Test
    public void testListOfUsers(){
        List<User> list = userRepository.findAll();
        list.forEach(user -> System.out.println(user));
        assertThat(list.stream().toList());
    }
}
