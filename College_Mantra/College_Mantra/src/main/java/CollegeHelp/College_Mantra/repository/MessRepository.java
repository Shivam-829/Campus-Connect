package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.Mess;
import CollegeHelp.College_Mantra.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessRepository extends JpaRepository<Mess,Long> {
    List<Mess> findByCollege(College college);

    List<Mess> findByNameStartingWithAndCollege(String name, College college);

    List<Mess> findByNameStartingWith(String name);
}
