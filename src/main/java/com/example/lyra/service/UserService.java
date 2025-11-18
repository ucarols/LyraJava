package com.example.lyra.service;

import com.example.lyra.dto.request.UserRequest;
import com.example.lyra.dto.response.UserResponse;
import com.example.lyra.exception.ResourceNotFoundException;
import com.example.lyra.model.EHumor;
import com.example.lyra.model.ERole;
import com.example.lyra.model.Role;
import com.example.lyra.dto.HumorMessage;
import com.example.lyra.model.User;
import com.example.lyra.repository.RoleRepository;
import com.example.lyra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange}")
    private String exchange;
    
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        // Check if email is already in use
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        
        // Set humor if provided
        if (userRequest.getHumor() != null) {
            user.setHumor(EHumor.fromCodigo(userRequest.getHumor()));
        }
        
        // Set humor description if provided
        if (userRequest.getHumorDescricao() != null) {
            user.setHumorDescricao(userRequest.getHumorDescricao());
        }
        
        // Set roles
        Set<Role> roles = new HashSet<>();
        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            userRequest.getRoles().forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Update user fields
        if (userRequest.getFirstName() != null) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            user.setLastName(userRequest.getLastName());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new RuntimeException("Error: Email is already in use!");
            }
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        
        // Update humor if provided
        // Update humor if provided
        if (userRequest.getHumor() != null) {
            EHumor novoHumor = EHumor.fromCodigo(userRequest.getHumor());
            user.setHumor(novoHumor);
            
            // Envia mensagem para a fila quando o humor é atualizado
            if (userRequest.getHumorDescricao() != null) {
                sendHumorUpdate(user, novoHumor, userRequest.getHumorDescricao());
            } else {
                sendHumorUpdate(user, novoHumor, "");
            }
        } else if (userRequest.getHumorDescricao() != null) {
            // Se apenas a descrição for fornecida, mantém o humor atual ou usa um padrão
            if (user.getHumor() == null) {
                user.setHumor(EHumor.NEUTRO);
            }
            sendHumorUpdate(user, user.getHumor(), userRequest.getHumorDescricao());
        }
        
        // Update roles if provided
        if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            userRequest.getRoles().forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
            user.setRoles(roles);
        }
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private void sendHumorUpdate(User user, EHumor humor, String descricao) {
        HumorMessage message = new HumorMessage(
            user.getId(),
            user.getFirstName() + " " + user.getLastName(),
            humor,
            descricao,
            java.time.LocalDateTime.now()
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
                
        // Set humor and humor description
        if (user.getHumor() != null) {
            response.setHumor(user.getHumor().name());
            response.setHumorDescricao(user.getHumorDescricao());
        }
        return response;
    }
}
