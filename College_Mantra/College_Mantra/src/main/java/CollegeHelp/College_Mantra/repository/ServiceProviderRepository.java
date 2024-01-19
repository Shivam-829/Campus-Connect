package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,Long> {
    List<ServiceProvider> findByCollege(College college);

    List<ServiceProvider> findByNameStartingWithAndCollege(String name, College college);

    List<ServiceProvider> findByNameStartingWith(String name);
}
