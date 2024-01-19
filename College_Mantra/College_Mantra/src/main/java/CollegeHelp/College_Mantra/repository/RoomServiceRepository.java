package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.enums.ServiceType;
import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.RoomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomServiceRepository extends JpaRepository<RoomService,Long> {

    List<RoomService> findByCityAndServiceType(City city, ServiceType serviceType);

    List<RoomService> findByCollege(College college);

    List<RoomService> findByCity(City city);

    List<RoomService> findByServiceType(ServiceType serviceType);

    List<RoomService> findByCityAndCollege(City city, College college);

    List<RoomService> findByServiceTypeAndCollege(ServiceType serviceType, College college);

    List<RoomService> findByCityAndServiceTypeAndCollege(City city, ServiceType serviceType, College college);
}
