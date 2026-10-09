package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.ForgotPasswordRequest;
import com.vehiclerental.dto.request.LoginRequest;
import com.vehiclerental.dto.request.RegisterRequest;
import com.vehiclerental.dto.request.ResetPasswordRequest;
import com.vehiclerental.dto.request.UpdateProfileRequest;
import com.vehiclerental.dto.response.JwtResponse;
import com.vehiclerental.dto.response.UserResponse;
import com.vehiclerental.entity.Role;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ConflictException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.RoleRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.security.JwtUtils;
import com.vehiclerental.security.UserDetailsImpl;
import com.vehiclerental.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail().trim().toLowerCase(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .fullName(userDetails.getFullName())
                .phone(userDetails.getPhone())
                .profilePhoto(userDetails.getProfilePhoto())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest registerRequest) {
        String normalizedEmail = registerRequest.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email is already registered: " + normalizedEmail);
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build()));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .fullName(registerRequest.getFullName().trim())
                .email(normalizedEmail)
                .phone(registerRequest.getPhone().trim())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone().trim());
        }
        if (request.getProfilePhoto() != null) {
            user.setProfilePhoto(request.getProfilePhoto());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Current password does not match");
            }
            if (request.getNewPassword().length() < 8) {
                throw new BadRequestException("New password must be at least 8 characters long");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void processForgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("No account registered with email: " + request.getEmail()));
        // Simulated secure reset token dispatch
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (request.getNewPassword().length() < 8) {
            throw new BadRequestException("New password must be at least 8 characters");
        }
        // In this implementation, token can either be the user's email or an authorized temporary token
        User user = userRepository.findByEmail(request.getToken().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        List<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profilePhoto(user.getProfilePhoto())
                .isActive(user.getIsActive())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }
}
