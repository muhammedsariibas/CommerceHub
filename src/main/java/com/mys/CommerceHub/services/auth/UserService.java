package com.mys.CommerceHub.services.auth;


import com.mys.CommerceHub.dao.auth.PermissionDao;
import com.mys.CommerceHub.dao.auth.RoleDao;
import com.mys.CommerceHub.dao.auth.UserDao;
import com.mys.CommerceHub.dto.auth.AuthRegisterRequest;
import com.mys.CommerceHub.dto.auth.RoleDto;
import com.mys.CommerceHub.models.auth.Permission;
import com.mys.CommerceHub.models.auth.Role;
import com.mys.CommerceHub.models.auth.User;
import com.mys.CommerceHub.services.securityService.SecurityService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final PermissionDao permissionDao;
    private final RoleDao roleDao;
    private final SecurityService securityService;
    @Transactional
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);

        if (user != null) {
            Hibernate.initialize(user.getRoles());

            for (Role role : user.getRoles()) {
                Hibernate.initialize(role.getPermissions());
            }
        }

        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createUser(AuthRegisterRequest request) {
        if(securityService.hasAuthority("PERM_AUTH_REGİSTER")){
            if (request.getId() == 0) {
                User user = new User();
                user.setAccountExpired(false);
                user.setAccountLocked(false);
                user.setEmail(request.getEmail());
                user.setPhone(request.getPhone());
                user.setEnabled(request.isEnabled());
                user.setSurname(request.getSurname());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setUsername(request.getName());
                user.setCredentialsExpired(false);
                user.setRoles(new HashSet<>());

                for (Long roleId : request.getRoleIds()) {
                    Optional<Role> roleOptional = roleDao.findById(roleId);

                    roleOptional.ifPresent(roles -> user.getRoles().add(roles));
                }

                userDao.save(user);
            } else {
                Optional<User> gettedUser = userDao.findById(request.getId());
                if (gettedUser.isPresent()) {
                    User user = gettedUser.get();
                    user.setUsername(request.getName());

                    if(request.getPassword() != null){
                        user.setPassword(passwordEncoder.encode(request.getPassword()));}
                    user.setEmail(request.getEmail());
                    user.setPhone(request.getPhone());
                    user.setEnabled(request.isEnabled());
                    user.setRoles(new HashSet<>());
                    for (Long roleId : request.getRoleIds()) {
                        Optional<Role> roleOptional = roleDao.findById(roleId);

                        roleOptional.ifPresent(roles -> user.getRoles().add(roles));
                    }

                    userDao.save(user);
                }
            }
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleDto roleDto) {
        if(securityService.hasAuthority("PERM_CREATE_ROLE")) {
            if (roleDto.getId() == 0) {
                Role newRole = new Role();
                newRole.setPermissions(new HashSet<>());

                newRole.setName(roleDto.getName());
                for (Long permId : roleDto.getPermissionIds()) {

                    Optional<Permission> permsOptional = permissionDao.findById(permId);

                    permsOptional.ifPresent(perms -> newRole.getPermissions().add(perms));
                }
                roleDao.save(newRole);
            } else {
                Optional<Role> getRoleById = roleDao.findById(roleDto.getId());
                if (getRoleById.isPresent()) {
                    Role role = getRoleById.get();
                    role.setName(roleDto.getName());
                    role.setPermissions(new HashSet<>());
                    for (Long permId : roleDto.getPermissionIds()) {
                        Optional<Permission> permOptional = permissionDao.findById(permId);
                        permOptional.ifPresent(perm -> role.getPermissions().add(perm));
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AuthRegisterRequest> getUsers() {
        List<User> users = new ArrayList<>();
        List<AuthRegisterRequest> returnList = new ArrayList<>();
        if(securityService.hasAuthority("PERM_AUTH_SEE_ALL")){

            users = userDao.findAll();

            for (User user : users) {

                Set<Long> ids = new HashSet<>();
                for (Role role : user.getRoles()) {
                    ids.add(role.getId());
                }

                AuthRegisterRequest authRegisterRequest = AuthRegisterRequest.builder()
                        .id(user.getId())
                        .name(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .roleIds(ids)
                        .email(user.getEmail())
                        .enabled(user.isEnabled())
                        .phone(user.getPhone())
                        .build();
                returnList.add(authRegisterRequest);
            }


        }
        return returnList;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<RoleDto> getRoles() {
        List<RoleDto> returnList = new ArrayList<>();
        List<Role> roles = roleDao.findAll();
        if(securityService.hasAuthority("PERM_AUTH_SEE_ALL")){


            for (Role role : roles) {

                Set<Long> ids = new HashSet<>();

                for (Permission perm : role.getPermissions()) {
                    ids.add(perm.getId());
                }

                RoleDto dto = RoleDto.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .permissionIds(ids)
                        .build();
                returnList.add(dto);
            }


        }
        return returnList;
    }

    @Transactional
    public List<Permission> getPerm(){
        List<Permission> returnList = new ArrayList<>();
        if(securityService.hasAuthority("PERM_AUTH_SEE_ALL")){
            returnList = permissionDao.findAll();
        }
        return returnList;
    }

    @Transactional
    public void deleteUser(Long id){
        if(securityService.hasAuthority("PERM_AUTH_DELETE")){
            userDao.deleteById(id);
        }

    }
    @Transactional
    public void deleteRole(Long id){
        if(securityService.hasAuthority("PERM_AUTH_DELETE")) {
            roleDao.deleteById(id);
        }
    }

}

