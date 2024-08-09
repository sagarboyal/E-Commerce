package com.ecommerce.main.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name ="user")
public class User {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String email;
    @Column(length = 64, nullable = false)
    private String password;
    @Column(name="first_name", length = 45, nullable = false)
    private String firstName;
    @Column(name="last_name", length = 45, nullable = false)
    private String lastName;
    private String photos;
    private boolean enabled;
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, String firstName, String lastName, boolean enabled) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    public void addRoles(Role role){
        this.roles.add(role);
    }
    @Transient
    public String getPhotosImagePath(){
        if(id == null || photos == null) return "/images/default-user.png";
        return "/user-photos/" + this.id + "/" + this.photos;
    }
}
