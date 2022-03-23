package com.project.alarcha.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleSecurityModel implements GrantedAuthority {

    private String roleName;
    private UserSecurityModel userSecurityModel;

    @Override
    public String getAuthority() {
        return getRoleName();
    }
}