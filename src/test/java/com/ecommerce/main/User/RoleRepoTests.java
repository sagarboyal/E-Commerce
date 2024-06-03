package com.ecommerce.main.User;

import com.ecommerce.main.entity.Role;
import com.ecommerce.main.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepoTests {
    @Autowired
    private RoleRepository roleRepository;
    @Test
    public void createRole(){
        Role role = new Role("Admin", "Manage everything.");
        Role data = roleRepository.save(role);
        assertThat(data.getId()).isGreaterThan(0);
    }
    @Test
    public void createRoles(){
        List<Role> list = new ArrayList<>();
        list.add(new Role("Admin", "Manage everything."));
        list.add(new Role("Salesperson", "Manage product price, customers, shipping, orders and sales reports."));
        list.add(new Role("Editor", "Manage categories, brands, products,articles and menus."));
        list.add(new Role("Shipper", "View products, orders and update order status."));
        list.add(new Role("Assistant", "Manage questions and reviews."));

        List<Role> data = roleRepository.saveAll(list);
    }
}
