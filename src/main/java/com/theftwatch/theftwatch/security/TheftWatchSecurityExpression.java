package com.theftwatch.theftwatch.security;

import com.theftwatch.theftwatch.domain.enums.Role;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class TheftWatchSecurityExpression extends SecurityExpressionRoot {

    public TheftWatchSecurityExpression(Authentication authentication) {
        super(authentication);
    }

    public boolean hasRole(Role role) {
        return hasAnyRole(role);
    }

    public boolean hasAnyRole(Role... roles) {
        for (Role role : roles) {
            if (getAuthentication().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()))) {
                return true;
            }
        }
        return false;
    }
}
