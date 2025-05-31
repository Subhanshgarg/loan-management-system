    package com.fintech.loan_management_system.controller;

    import com.fintech.loan_management_system.dto.ApiResponse;
    import com.fintech.loan_management_system.dto.LoginDto;
    import com.fintech.loan_management_system.security.JwtUtil;
    import com.fintech.loan_management_system.service.UserService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    @Slf4j
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserService userService;
        private final JwtUtil jwtUtil;

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginDto loginDto) {
            log.info("Login attempt for username: {}", loginDto.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

            log.info("Login successful for username: {}", loginDto.getUsername());
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        }
    }

