package com.techyatra.blog_api.dto;


import lombok.*;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private UUID id;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Invalid email pattern")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(admin|user)$", message = "Role must be 'admin' or 'user'")
    private String role;
    
    private Boolean isVerify;
    private String errorMessage;
    private String otpReference;
    private Long otpExpirationTime;
}
