package com.mys.CommerceHub.models.auth;

import com.mys.CommerceHub.models.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "permission")
@Table(name = "auth_permission")
@Data
public class Permission extends BaseEntity {
    @Column(name = "name",unique = true)
    private String name;
}
