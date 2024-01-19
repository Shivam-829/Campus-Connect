package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.Reputation;
import CollegeHelp.College_Mantra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReputationRepository extends JpaRepository<Reputation, Long> {
    Reputation findByUser1AndUser2(User user, User user1);
}