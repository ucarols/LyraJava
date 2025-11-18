package com.example.lyra.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Size(min = 6, max = 40)
    private String password;

    private Set<String> roles;
    
    private Integer humor;
    
    @Size(max = 500)
    private String humorDescricao;
}
