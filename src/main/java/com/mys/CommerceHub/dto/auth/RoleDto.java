package com.mys.CommerceHub.dto.auth;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private Set<Long> permissionIds;

    public RoleDto() {

    }

    @Builder
    public RoleDto(Long id, String name, Set<Long> permissionIds) {
        this.id = id;
        this.name = name;
        this.permissionIds = permissionIds;
    }


}
