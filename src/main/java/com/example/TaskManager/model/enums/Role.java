package com.example.TaskManager.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    Developer, QA, BA;

    @Override
    public String getAuthority() {
        return name();
    }
}
