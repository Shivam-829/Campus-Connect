package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
