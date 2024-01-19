package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College,Long> {
    List<College> findByCity(City city);

    List<College> findByNameStartingWithAndCity(String name, City city);

    List<College> findByNameStartingWith(String name);

}
