package finance_dashboard.repository;

import finance_dashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;  // ADD THIS import

public interface UserRepository extends JpaRepository<User, Long> {

    // ADD THIS method - it finds a user by their email (used for login)
    Optional<User> findByEmail(String email);
}