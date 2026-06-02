package START.Services;

import START.Exception.EmailAlreadyExistsException;
import START.Exception.InvalidCredentialsException;
import START.Exception.UserNotFoundException;
import START.Exception.UsernameAlreadyExistsException;
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
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
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

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
