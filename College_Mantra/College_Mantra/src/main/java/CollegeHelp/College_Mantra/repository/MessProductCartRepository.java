package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.MessProduct;
import CollegeHelp.College_Mantra.model.MessProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessProductCartRepository extends JpaRepository<MessProductCart,Long> {
}
