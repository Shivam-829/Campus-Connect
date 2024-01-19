package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.files.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
}
