package com.mys.CommerceHub.services.securityService;

import com.mys.CommerceHub.dao.auth.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserDao userDao;
    public boolean hasAuthority(String perm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {

            return ((UserDetails)principal).getAuthorities().stream().anyMatch(t->t.getAuthority().equals(perm));

        } else {

            return false;
        }
    }
}
