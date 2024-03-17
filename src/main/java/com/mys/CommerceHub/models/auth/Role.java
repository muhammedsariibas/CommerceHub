package com.mys.CommerceHub.models.auth;

import com.mys.CommerceHub.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity(name = "role")
@Table(name = "auth_role")
@Getter
@Setter
public class Role extends BaseEntity {
    @Column(name = "name",unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_role_permissions")
    private Set<Permission> permissions;


}
