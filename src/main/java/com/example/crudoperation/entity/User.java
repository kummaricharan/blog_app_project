package com.example.crudoperation.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="username")
    private String username;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="cmp_password")
    private String cmp_password;
    @Column(name = "enabled")
    private int enabled;
    @Column(name="role")
    private String role;
    public User(){

    }


    public User(Long id, String username, String email, String password,String cmp_password, int enabled,String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cmp_password = cmp_password;
        this.enabled = enabled;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCmp_password() {
        return cmp_password;
    }

    public void setCmp_password(String cmp_password) {
        this.cmp_password = cmp_password;
    }

    public int isEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getEnabled() {
        return enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cmp_password='" + cmp_password + '\'' +
                ", enabled=" + enabled +
                ", role='" + role + '\'' +
                '}';
    }
}
