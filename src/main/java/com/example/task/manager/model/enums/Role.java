package com.example.task.manager.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    Developer, QA, BA, Admin;

    @Override
    public String getAuthority() {
        return name();
    }
}
