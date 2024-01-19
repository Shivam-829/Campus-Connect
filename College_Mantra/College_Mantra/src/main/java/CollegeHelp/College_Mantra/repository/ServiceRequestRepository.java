package CollegeHelp.College_Mantra.repository;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.model.ServiceProvider;
import CollegeHelp.College_Mantra.model.ServiceRequest;
import CollegeHelp.College_Mantra.model.Student;
import CollegeHelp.College_Mantra.response.ServiceRequestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest,Long> {
    List<ServiceRequest> findByStudentAndRequestStatus(Student student, RequestStatus requestStatus);

    List<ServiceRequest> findByServiceProviderAndRequestStatus(ServiceProvider serviceProvider, RequestStatus requestStatus);
}
