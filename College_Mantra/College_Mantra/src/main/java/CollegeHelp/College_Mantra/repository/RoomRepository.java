package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RoomType;
import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findByCostLessThanEqualAndAvailabilityType(Integer range, AvailabilityType tooLate);

    List<Room> findByCollegeAndCostLessThanEqualAndAvailabilityType(College college, Integer range, AvailabilityType tooLate);

    List<Room> findByRoomTypeAndCostLessThanEqualAndAvailabilityType(RoomType roomType, Integer range, AvailabilityType tooLate);

    List<Room> findByCollegeAndAvailabilityType(College college, AvailabilityType tooLate);

    List<Room> findByRoomTypeAndAvailabilityType(RoomType roomType, AvailabilityType tooLate);

    List<Room> findByAvailabilityType(AvailabilityType tooLate);

    List<Room> findByCollegeAndRoomTypeAndCostLessThanEqualAndAvailabilityType(College college, RoomType roomType, Integer range, AvailabilityType tooLate);

    List<Room> findByCollegeAndRoomTypeAndAvailabilityType(College college, RoomType roomType, AvailabilityType tooLate);
}
