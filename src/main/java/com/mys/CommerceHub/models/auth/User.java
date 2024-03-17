package com.mys.CommerceHub.models.auth;

import com.mys.CommerceHub.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "user")
@Table(name = "auth_user")
public class User extends BaseEntity {

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email",unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_account_locked")
    private boolean isAccountLocked;

    @Column(name = "is_account_expired")
    private boolean isAccountExpired;

    @Column(name = "is_credentials_expired")
    private boolean isCredentialsExpired;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_roles")
    private Set<Role> roles = new java.util.LinkedHashSet<>();
}
