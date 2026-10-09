package com.vehiclerental.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vehiclerental.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String phone;
    private final String profilePhoto;

    @JsonIgnore
    private final String password;

    private final Boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String fullName, String email, String phone, String profilePhoto,
                           String password, Boolean isActive, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.password = password;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfilePhoto(),
                user.getPassword(),
                user.getIsActive(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive != null && isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive != null && isActive;
    }
}
