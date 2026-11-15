package com.alianca.sistema.service;

import com.alianca.sistema.dto.LoginRequestDTO;
import com.alianca.sistema.dto.UserRequestDTO;
import com.alianca.sistema.dto.UserResponseDTO;
import com.alianca.sistema.exception.EmailAlreadyExistsException;
import com.alianca.sistema.exception.InvalidCredentialsException;
import com.alianca.sistema.exception.UserNotFoundException;
import com.alianca.sistema.model.User;
import com.alianca.sistema.repository.UserRepository;
import com.alianca.sistema.dto.UserUpdateDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);

        return toResponseDTO(savedUser);
    }

    public void authenticate(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponseDTO(user);
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return toResponseDTO(user);
    }
    public UserResponseDTO updateName(
            String email,
            UserUpdateDTO request
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.setName(request.name());

        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }
}