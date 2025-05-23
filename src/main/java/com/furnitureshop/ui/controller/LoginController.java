package com.furnitureshop.ui.controller;

import com.furnitureshop.model.entity.User;
import com.furnitureshop.repository.UserRepository;
import com.furnitureshop.security.JwtTokenProvider;
import com.furnitureshop.ui.StageManager;
import com.furnitureshop.ui.view.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button bypassButton;

    @FXML
    private Label errorLabel;

    private final StageManager stageManager;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(StageManager stageManager,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.stageManager = stageManager;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);

        // Debug: Check if admin user exists
        Optional<User> adminUser = userRepository.findByUsername("admin");
        if (adminUser.isPresent()) {
            System.out.println("Admin user found in database");
            User admin = adminUser.get();
            System.out.println("Admin password hash: " + admin.getPassword());
            System.out.println("Would 'admin123' match? " + passwordEncoder.matches("admin123", admin.getPassword()));
        } else {
            System.out.println("Admin user NOT found in database!");
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Login attempt: username=" + username);

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty");
            return;
        }

        try {
            // Direct check first
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
                System.out.println("User found. Password match: " + passwordMatches);

                if (passwordMatches) {
                    System.out.println("Password matches! Proceeding with authentication...");
                } else {
                    System.out.println("Password doesn't match. Expected hash: " + user.getPassword());
                }
            } else {
                System.out.println("User not found in database");
                showError("Invalid username or password");
                return;
            }

            // Proceed with Spring Security authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            System.out.println("Authentication successful! JWT token generated.");

            // Navigate to dashboard
            stageManager.switchScene(FXMLView.DASHBOARD);

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
            showError("Invalid username or password");
        } catch (Exception e) {
            System.err.println("Unexpected error during login: " + e.getMessage());
            e.printStackTrace();
            showError("An unexpected error occurred. Please try again.");
        }
    }

    @FXML
    private void handleBypassButtonAction(ActionEvent event) {
        System.out.println("Login bypass activated - proceeding as admin");

        try {
            // Create a manual authentication token with admin privileges
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "admin",
                    "BYPASSED_PASSWORD",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );

            // Set it in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Navigate to dashboard
            stageManager.switchScene(FXMLView.DASHBOARD);

        } catch (Exception e) {
            System.err.println("Error during login bypass: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to bypass login. Please try again.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
