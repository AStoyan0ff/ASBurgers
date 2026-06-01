package START.Services;

import START.Models.User;
import START.Repositories.UserRepository;
import START.Web.DTOs.LoginRequest;
import START.Web.DTOs.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .build();

        userRepository.save(user);

    }

    public User login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new IllegalStateException("Invalid username or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Invalid username or password.");
        }

        return user;
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("User not found."));
    }
}
