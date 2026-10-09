package com.vehiclerental.service;

import com.vehiclerental.dto.request.ForgotPasswordRequest;
import com.vehiclerental.dto.request.LoginRequest;
import com.vehiclerental.dto.request.RegisterRequest;
import com.vehiclerental.dto.request.ResetPasswordRequest;
import com.vehiclerental.dto.request.UpdateProfileRequest;
import com.vehiclerental.dto.response.JwtResponse;
import com.vehiclerental.dto.response.UserResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    UserResponse registerUser(RegisterRequest registerRequest);
    UserResponse getCurrentUserProfile(String email);
    UserResponse updateProfile(String email, UpdateProfileRequest request);
    void processForgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
