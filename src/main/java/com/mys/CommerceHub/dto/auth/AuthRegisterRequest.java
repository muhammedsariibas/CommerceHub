package com.mys.CommerceHub.dto.auth;


import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
public class AuthRegisterRequest {
    private Long id;
    private String name;
    private String password;
    private Set<Long> roleIds;
    private String email;
    private String phone;
    private String surname;
    private boolean enabled = false;


    public AuthRegisterRequest() {

    }


    @Builder
    public AuthRegisterRequest(Long id, String name, String password, Set<Long> roleIds, String email, String phone,
                               boolean enabled,String surname) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.roleIds = roleIds;
        this.email = email;
        this.phone = phone;
        this.enabled = enabled;
        this.surname = surname;
    }




}

