package com.example.DocumentManagement.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final UsersEntity user;
    private final List<RolesEntity> listRoleEntity;

    public MyUserDetails(UsersEntity user, List<RolesEntity> listRoleEntity) {
        this.user = user;
        this.listRoleEntity = listRoleEntity;

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (RolesEntity role : listRoleEntity) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPass();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
