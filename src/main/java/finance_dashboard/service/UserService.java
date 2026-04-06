package finance_dashboard.service;

import finance_dashboard.model.User;
import finance_dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // ADD THIS - for encrypting passwords

    // Create user - always set ACTIVE by default and ENCRYPT password
    public User createUser(User user) {
        user.setStatus("ACTIVE");

        // ENCRYPT the password before saving to database
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return repository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // Get one user by ID
    public User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));
    }

    // Deactivate a user
    public User deactivateUser(Long id) {
        User user = getUserById(id);
        user.setStatus("INACTIVE");
        return repository.save(user);
    }

    // Update user role
    public User updateRole(Long id, String role) {
        User user = getUserById(id);
        user.setRole(role);
        return repository.save(user);
    }
}