package com.example.backend.Controller.Controllers;

import com.example.backend.Controller.Services.AuthService;
import com.example.backend.Controller.Services.EmailService;
import com.example.backend.Controller.Services.UserService;
import com.example.backend.Model.Entities.LoginRequest;
import com.example.backend.Model.Entities.LoginResponse;
import com.example.backend.Model.Entities.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;

    public AuthController(AuthService authService, UserService userService, EmailService emailService) {
        this.authService = authService;
        this.userService = userService;
        this.emailService = emailService;
    }

    // Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        // Autenticar al usuario
        User user = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword(), session);

        if (user != null) {
            return new LoginResponse(user.getId(), user.getFullName(), user.getLastName(), user.getRole());
        } else {
            return null;
        }
    }

    // Logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out successfully";
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        Optional<User> userOptional = userService.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Email not found.");
        }

        String resetLink = "https://proyect-frontend-deploymen.vercel.app/update-password?email=" + email;

        emailService.sendPasswordResetEmail(email, resetLink);

        return ResponseEntity.ok("Password reset link sent.");
    }


    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        Optional<User> userOptional = userService.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Invalid email.");
        }

        User user = userOptional.get();
        user.setPassword(newPassword);
        userService.createUser(user); // Guardar los cambios

        return ResponseEntity.ok("Password updated successfully.");
    }
}
