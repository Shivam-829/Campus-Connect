package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.RoomOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomHonourRepository extends JpaRepository<RoomOwner,Long> {
    List<RoomOwner> findByNameStartingWithAndCollege(String name, College college);

    List<RoomOwner> findByNameStartingWith(String name);

    List<RoomOwner> findByCollege(College college);
}
