package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation,Long> {
}
