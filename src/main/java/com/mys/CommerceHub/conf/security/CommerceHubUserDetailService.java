package com.mys.CommerceHub.conf.security;

import com.mys.CommerceHub.models.auth.Permission;
import com.mys.CommerceHub.models.auth.Role;
import com.mys.CommerceHub.models.auth.User;
import com.mys.CommerceHub.services.auth.UserService;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public class CommerceHubUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CommerceHubUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s was not found", username));
        }

        Set<GrantedAuthority> _perms = new HashSet<>();

        Hibernate.initialize(user.getRoles());

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            for (Role role : user.getRoles()) {

                Hibernate.initialize(role.getPermissions());

                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    for (Permission perm :  role.getPermissions()) {
                        _perms.add(new SimpleGrantedAuthority(perm.getName()));
                    }
                }
            }
        }

        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                !user.isAccountExpired(),
                !user.isCredentialsExpired(),
                !user.isAccountLocked(),
                _perms
        );

        return springUser;
    }
}
