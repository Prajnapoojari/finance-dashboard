package finance_dashboard.controller;

import finance_dashboard.model.User;
import finance_dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    // POST /api/users — create user (anyone can register)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(service.createUser(user));
    }

    // GET /api/users — get all users (Only ADMIN)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestAttribute String userRole) {  // ADDED role check
        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can view all users");
        }
        return ResponseEntity.ok(service.getAllUsers());
    }

    // GET /api/users/1 — get one user (ADMIN or the user themselves)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id,
            @RequestAttribute String userRole,
            @RequestAttribute String userEmail) {  // ADDED role and email from token

        User user = service.getUserById(id);

        // Allow if ADMIN or if the user is requesting their own data
        if (!userRole.equals("ADMIN") && !user.getEmail().equals(userEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: You can only view your own profile");
        }
        return ResponseEntity.ok(user);
    }

    // PATCH /api/users/1/deactivate — deactivate user (Only ADMIN)
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(
            @PathVariable Long id,
            @RequestAttribute String userRole) {  // ADDED role check

        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can deactivate users");
        }
        return ResponseEntity.ok(service.deactivateUser(id));
    }

    // PATCH /api/users/1/role?role=ANALYST — update role (Only ADMIN)
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestAttribute String userRole) {  // ADDED role check from token

        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can change roles");
        }
        return ResponseEntity.ok(service.updateRole(id, role));
    }
}