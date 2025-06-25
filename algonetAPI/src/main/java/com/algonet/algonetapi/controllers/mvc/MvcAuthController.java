package com.algonet.algonetapi.controllers.mvc;

import com.algonet.algonetapi.models.dto.userDTOs.UserCreationDTO;
import com.algonet.algonetapi.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Controller("mvcAuthController")
@AllArgsConstructor
@Slf4j
public class MvcAuthController {
    
    private final AuthService authService;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupForm {
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;
        
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        private String email;
        
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
        
        @NotBlank(message = "Please confirm your password")
        private String confirmPassword;
        
        public UserCreationDTO toUserCreationDTO() {
            UserCreationDTO dto = new UserCreationDTO();
            dto.setUsername(this.username);
            dto.setEmail(this.email);
            dto.setPassword(this.password);
            return dto;
        }
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        log.info("Displaying signup form");
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupForm signupForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        log.info("Processing signup request for username: {}", signupForm.getUsername());
        
        if (bindingResult.hasErrors()) {
            log.warn("Signup form validation failed for username: {}", signupForm.getUsername());
            return "auth/signup";
        }
        
        // Check if passwords match
        if (!signupForm.getPassword().equals(signupForm.getConfirmPassword())) {
            log.warn("Password confirmation failed for username: {}", signupForm.getUsername());
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
            return "auth/signup";
        }
          try {
            authService.register(signupForm.toUserCreationDTO(), Instant.now());
            log.info("User registration successful for username: {}", signupForm.getUsername());
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("User registration failed for username: {}, error: {}", signupForm.getUsername(), e.getMessage());
            bindingResult.rejectValue("username", "error.username", "Username or email already exists");
            return "auth/signup";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        log.info("Displaying login form");
        return "auth/login";
    }
}
