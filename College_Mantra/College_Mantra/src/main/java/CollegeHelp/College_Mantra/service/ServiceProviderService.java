package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.*;
import CollegeHelp.College_Mantra.model.RoomService;
import CollegeHelp.College_Mantra.repository.*;
import CollegeHelp.College_Mantra.request.*;
import CollegeHelp.College_Mantra.response.*;
import com.sun.jdi.request.InvalidRequestStateException;
import org.example.dto.MailMessage;
import org.example.dto.MessageMail_SMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private String folderPath = "D:\\Java-Backend-Projects\\CollegeHelp\\College_Mantra\\College_Mantra\\src\\main\\resources\\profilePhotos\\";

    private static Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Value("${mail.prefix}")
    private String MAIL_PREFIX;

    @Value("${number.prefix}")
    private String NUMBER_PREFIX;

    @Autowired
    private MyCacheRepository myCacheRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private RoomServiceRepository roomServiceRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceTransactionRepository serviceTransactionRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public void setInCache(ServiceProviderCreateRequest serviceProviderCreateRequest) {

        String tempId = UUID.randomUUID().toString();

        CreationRequestHolder creationRequestHolder = CreationRequestHolder.builder()
                .serviceProviderCreateRequest(serviceProviderCreateRequest)
                .isMailVerified(false)
                .isNumberVerified(false)
                .tempId(tempId)
                .build();

        String mailOTP = OTPService.generateOTP();
        String numberOTP = OTPService.generateOTP();

        myCacheRepository.setOTPValue(MAIL_PREFIX+mailOTP,creationRequestHolder);
        myCacheRepository.setOTPValue(NUMBER_PREFIX+numberOTP,creationRequestHolder);

        myCacheRepository.setTempIdValue(tempId,creationRequestHolder);

        // add messaging method here
        String mailOTPLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/signup/otp/mail/")
                .path(mailOTP)
                .toUriString();

        String numberOTPLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/signup/otp/number/")
                .path(numberOTP)
                .toUriString();

        MessageMail_SMS message = MessageMail_SMS.builder()
                .smsMessage("Hello User Click on the link " + numberOTPLink + " to verify your number")
                .mailMessage("Hello User Click on the link " + mailOTPLink + " to verify your gmail")
                .number(serviceProviderCreateRequest.getPhoneNumber())
                .mail(serviceProviderCreateRequest.getGmail()).build();

        kafkaProducer.sendInBoth(message);

    }

    public void create(ServiceProviderCreateRequest serviceProviderCreateRequest) throws InvalidIdException {
        LOGGER.info("Username : {}",serviceProviderCreateRequest.getUsername());
        ServiceProvider serviceProvider = serviceProviderCreateRequest.to();
        serviceProvider.setCollege(collegeService.findById(serviceProviderCreateRequest.getCollegeId()));
        UserCreateRequest userCreateRequest = serviceProviderCreateRequest.toUser();
        User myUser = myUserDetailsService.create(userCreateRequest);
        serviceProvider.setUser(myUser);
        try {
            serviceProviderRepository.save(serviceProvider);
        } catch (Exception ex){
            myUserRepository.delete(myUser);
            throw ex;
        }
    }

    public byte[] uploadPhoto(MultipartFile multipartFile, Long id) throws IOException {

        LOGGER.info("94");

        String imagePath = folderPath + multipartFile.getOriginalFilename();

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();

        ProfileImage profileImage = ProfileImage.builder()
                .type(multipartFile.getContentType())
                .serviceProvider(serviceProvider)
                .filePath(imagePath)
                .name(multipartFile.getOriginalFilename())
                .build();

        ProfileImage existingImg = serviceProvider.getProfileImage();

        if(existingImg != null){
            serviceProvider.setProfileImage(null);
            serviceProvider = serviceProviderRepository.save(serviceProvider);
            profileImageRepository.deleteById(existingImg.getId());
            File file = new File(existingImg.getFilePath());
            file.delete();
        }

        LOGGER.info("114");

        profileImage = profileImageRepository.save(profileImage);

        serviceProvider.setProfileImage(profileImage);

        serviceProviderRepository.save(serviceProvider);

        LOGGER.info("120");

        multipartFile.transferTo(new File(profileImage.getFilePath()));

        LOGGER.info("124");

        return DownloadImage.downloadImage(profileImage.getFilePath());

    }

    public UserResponse findById(Long id) throws IOException {

        Optional<ServiceProvider> optionalStudent = serviceProviderRepository.findById(id);

        return optionalStudent.get().to();

    }

    public RoomServiceResponse addService(Long id, RoomServiceRequest roomServiceRequest) throws IOException {

        RoomService roomService = roomServiceRequest.to();

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();

        roomService.setServiceProvider(serviceProvider);
        roomService.setCity(serviceProvider.getCollege().getCity());
        roomService.setCollege(serviceProvider.getCollege());

        return roomServiceRepository.save(roomService).to();

    }

    public List<RoomServiceResponse> getServicesList(Long id) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();

        return serviceProvider.getRoomServices().stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<ServiceRequestResponse> getServiceRequest(Long id, RequestStatus requestStatus) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();
        return serviceRequestRepository.findByServiceProviderAndRequestStatus(serviceProvider, requestStatus)
                .stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }


    public boolean deleteServiceRequest(Long id, Long serviceRequestId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();
        List<ServiceRequest> serviceRequestList = serviceProvider.getServiceRequests();
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId).get();

        if(serviceRequestList.contains(serviceRequest)){
            serviceRequestRepository.deleteById(serviceRequestId);
            return true;
        }

        return false;

    }

    public ServiceTransactionResponse acceptRequest(Long id, Long serviceRequestId) throws IOException {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId).get();

        List<ServiceRequest> serviceRequestList = serviceProvider.getServiceRequests();

        if(serviceRequestList.contains(serviceRequest)){

            ServiceTransaction serviceTransaction = ServiceTransaction.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .student(serviceRequest.getStudent())
                    .serviceProvider(serviceProvider)
                    .roomService(serviceRequest.getRoomService())
                    .build();

            serviceRequest.setRequestStatus(RequestStatus.ACCEPTED);

            serviceRequestRepository.save(serviceRequest);

            myCacheRepository.setTransactionValue(serviceTransaction.getTransactionId(),serviceProvider);

            MailMessage mailMessage = MailMessage.builder()
                    .mail(serviceRequest.getStudent().getGmail())
                    .mailMessage("Your Service Request has been accepted by " + serviceProvider.getName()).build();

            kafkaProducer.sendInMail(mailMessage);

            return serviceTransactionRepository.save(serviceTransaction).to();
        }

        throw new InvalidRequestStateException("User Don't Have This Request");

    }

    public List<ServiceTransactionResponse> getTransactionList(Long id) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();

        return serviceProvider.getServiceTransactions().stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public String updateName(Long id, String name) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();

        serviceProvider.setName(name);
        serviceProvider = serviceProviderRepository.save(serviceProvider);

        return serviceProvider.getName();
    }

    public CollegeResponse updateCollege(Long id, Long collegeId) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).get();
        College college = collegeRepository.findById(collegeId).get();

        serviceProvider.setCollege(college);
        serviceProvider = serviceProviderRepository.save(serviceProvider);

        return serviceProvider.getCollege().to();
    }
}
