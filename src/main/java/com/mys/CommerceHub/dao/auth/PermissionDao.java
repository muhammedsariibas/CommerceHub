package com.mys.CommerceHub.dao.auth;

import com.mys.CommerceHub.models.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends JpaRepository<Permission,Long> {
}

