package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.exceptions.WrongOTP;
import CollegeHelp.College_Mantra.repository.MyCacheRepository;
import CollegeHelp.College_Mantra.request.CreationRequestHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    @Value("${mail.prefix}")
    private String MAIL_PREFIX;

    @Value("${number.prefix}")
    private String NUMBER_PREFIX;

    private static Logger LOGGER = LoggerFactory.getLogger(VerificationService.class);

    @Autowired
    private MyCacheRepository myCacheRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoomOwnerService roomOwnerService;

    @Autowired
    private MessService messService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    public boolean mailVerification(String otp) throws WrongOTP, InvalidIdException {

        CreationRequestHolder createRequest = (CreationRequestHolder) myCacheRepository.get(MAIL_PREFIX + otp);

        if(createRequest == null) throw new WrongOTP("Incorrect OTP");

        CreationRequestHolder createRequest1 = (CreationRequestHolder) myCacheRepository.get(createRequest.getTempId());

        if(createRequest1 == null) throw new WrongOTP("Incorrect OTP");

        createRequest1.setIsMailVerified(true);

        myCacheRepository.delete(createRequest.getTempId());
        myCacheRepository.setTempIdValue(createRequest.getTempId(),createRequest1);

        if(createRequest1.getIsMailVerified() && createRequest1.getIsNumberVerified()) {
            // Create User
            //LOGGER.info("Name : {} is verified",createRequest1.getStudentCreateRequest().getName());

            sendCreationRequest(createRequest1);

            myCacheRepository.delete(createRequest.getTempId());
            return true;
        }

        // LOGGER.info("Name : {}, Email Verified",createRequest1.getStudentCreateRequest().getName());

        return false;

    }

    public boolean numberVerification(String otp) throws WrongOTP, InvalidIdException {

        CreationRequestHolder createRequest = (CreationRequestHolder) myCacheRepository.get(NUMBER_PREFIX + otp);

        if(createRequest == null) throw new WrongOTP("Incorrect OTP");

        CreationRequestHolder createRequest1 = (CreationRequestHolder) myCacheRepository.get(createRequest.getTempId());

        if(createRequest1 == null) throw new WrongOTP("Incorrect OTP");

        createRequest1.setIsNumberVerified(true);

        myCacheRepository.delete(createRequest.getTempId());
        myCacheRepository.setTempIdValue(createRequest.getTempId(),createRequest1);

        if(createRequest1.getIsMailVerified() && createRequest1.getIsNumberVerified()) {
            // Create User
            //LOGGER.info("Name : {} is verified",createRequest1.getStudentCreateRequest().getName());

            sendCreationRequest(createRequest1);

            myCacheRepository.delete(createRequest.getTempId());
            return true;
        }

        // LOGGER.info("Name : {}, Number Verified",createRequest1.getStudentCreateRequest().getName());

        return false;

    }

    private void sendCreationRequest(CreationRequestHolder creationRequestHolder) throws InvalidIdException {

        if(creationRequestHolder.getStudentCreateRequest() != null){
            LOGGER.info("Creating Student");
            studentService.create(creationRequestHolder.getStudentCreateRequest());
        }
        else if(creationRequestHolder.getMessCreateRequest() != null){
            LOGGER.info("Creating Mess");
            messService.create(creationRequestHolder.getMessCreateRequest());
        }
        else if(creationRequestHolder.getServiceProviderCreateRequest() != null){
            LOGGER.info("Creating ServiceProvider");
            serviceProviderService.create(creationRequestHolder.getServiceProviderCreateRequest());
        }
        else if(creationRequestHolder.getRoomHonourCreateRequest() != null){
            LOGGER.info("Creating RoomOwner");
            roomOwnerService.create(creationRequestHolder.getRoomHonourCreateRequest());
        }

    }

}
