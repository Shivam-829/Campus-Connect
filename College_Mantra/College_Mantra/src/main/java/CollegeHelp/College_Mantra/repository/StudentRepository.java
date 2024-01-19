package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.Student;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByCollege(College college);

    List<Student> findByNameStartingWithAndCollege(String name, College college);

    List<Student> findByNameStartingWith(String name);
}
