package com.mys.CommerceHub.dao.auth;

import com.mys.CommerceHub.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role,Long> {

}
