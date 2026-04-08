package com.sms.studentmanagement.serviceimpl;

import com.sms.studentmanagement.config.JwtUtils;
import com.sms.studentmanagement.config.UserDetailsImpl;
import com.sms.studentmanagement.dto.AuthDto;
import com.sms.studentmanagement.entity.Role;
import com.sms.studentmanagement.entity.User;
import com.sms.studentmanagement.exception.BadRequestException;
import com.sms.studentmanagement.exception.DuplicateResourceException;
import com.sms.studentmanagement.exception.ResourceNotFoundException;
import com.sms.studentmanagement.repository.RoleRepository;
import com.sms.studentmanagement.repository.UserRepository;
import com.sms.studentmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthDto.JwtResponse login(AuthDto.LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        log.info("User '{}' logged in successfully", userDetails.getUsername());

        return AuthDto.JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public AuthDto.JwtResponse register(AuthDto.RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("User", "username", registerRequest.getUsername());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", registerRequest.getEmail());
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .enabled(true)
                .build();

        Set<Role> roles = new HashSet<>();
        Set<String> requestedRoles = registerRequest.getRoles();

        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_STUDENT"));
            roles.add(studentRole);
        } else {
            requestedRoles.forEach(roleName -> {
                String fullRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName.toUpperCase();
                Role role = roleRepository.findByName(fullRoleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", fullRoleName));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        log.info("New user '{}' registered successfully", user.getUsername());

        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roleNames = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return AuthDto.JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    public AuthDto.JwtResponse refreshToken(AuthDto.RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new BadRequestException("Invalid or expired refresh token");
        }
        String username = jwtUtils.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtUtils.generateTokenFromUsername(username,
                (int) (24 * 60 * 60 * 1000));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return AuthDto.JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
