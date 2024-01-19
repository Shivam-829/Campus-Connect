package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.enums.*;
import CollegeHelp.College_Mantra.model.*;
import CollegeHelp.College_Mantra.model.RoomService;
import CollegeHelp.College_Mantra.repository.*;
import CollegeHelp.College_Mantra.response.*;
import com.sun.jdi.request.InvalidRequestStateException;
import io.netty.util.internal.StringUtil;
import org.example.dto.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoomOwnerService roomOwnerService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyCacheRepository myCacheRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MessProductRepository messProductRepository;
    @Autowired
    private MessProductCartRepository messProductCartRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private RoomServiceRepository roomServiceRepository;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    @Autowired
    private ServiceTransactionRepository serviceTransactionRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RoomHonourRepository roomHonourRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public Object findById(Long id) throws IOException {
        Optional<User> optionalUser = myUserRepository.findById(id);

        if(optionalUser.get().getAuthority() == Authority.STUDENT){
            return optionalUser.get().getStudent().to();
        }
        else if(optionalUser.get().getAuthority() == Authority.ROOM_HONOUR){
            return optionalUser.get().getRoomOwner().to();
        }
        else if(optionalUser.get().getAuthority() == Authority.SERVICE_PROVIDER){
            return optionalUser.get().getServiceProvider().to();
        }
        else {
            return optionalUser.get().getMess().to();
        }
    }

    public boolean changePassword(User myUser, String currPassword, String newPassword) {
        if(passwordEncoder.matches(currPassword,myUser.getPassword())){
            myUser.setPassword(passwordEncoder.encode(newPassword));
            myUserRepository.save(myUser);
            return true;
        }
        return false;
    }

    public boolean forgotPassword(String username) {
        User user = myUserRepository.findByUsername(username);
        if(user != null){
            String mailOTP = OTPService.generateTransactionId();

            myCacheRepository.setOTPValue(mailOTP,user);

            String mail = null;

            Authority authority = user.getAuthority();
            switch (authority){
                case ROOM_HONOUR:
                    mail = user.getRoomOwner().getGmail();
                    break;
                case STUDENT:
                    mail = user.getStudent().getGmail();
                    break;
                case SERVICE_PROVIDER:
                    mail = user.getServiceProvider().getGmail();
                    break;
                case MESS:
                    mail = user.getMess().getGmail();
                    break;
                default:
                    LOGGER.info("Impossible");
            }

            MailMessage message = MailMessage.builder()
                    .mail(mail)
                    .mailMessage("OTP to forgot the password : " + mailOTP).build();

            kafkaProducer.sendInMail(message);

            return true;
        }
        return false;
    }

    public boolean changePassword(String otp, String newPassword) {
        User user = (User) myCacheRepository.get(otp);
        if(user != null){
            user.setPassword(passwordEncoder.encode(newPassword));
            myUserRepository.save(user);
            return true;
        }
        return false;
    }

    public List<UserResponse> getUserList(String name, Long collegeId) {

        List<Student> userList = new ArrayList<>();

        if(StringUtils.hasText(name) && collegeId != null){
            userList = studentRepository.findByNameStartingWithAndCollege(name,collegeRepository.findById(collegeId).get());
        } else if (StringUtils.hasText(name)) {
            userList = studentRepository.findByNameStartingWith(name);
        } else if (collegeId != null) {
            userList = studentRepository.findByCollege(collegeRepository.findById(collegeId).get());
        }
        else userList = studentRepository.findAll();

        return userList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<UserResponse> getMessList(String name, Long collegeId) {

        List<Mess> userList = new ArrayList<>();

        if(StringUtils.hasText(name) && collegeId != null){
            userList = messRepository.findByNameStartingWithAndCollege(name,collegeRepository.findById(collegeId).get());
        } else if (StringUtils.hasText(name)) {
            userList = messRepository.findByNameStartingWith(name);
        } else if (collegeId != null) {
            userList = messRepository.findByCollege(collegeRepository.findById(collegeId).get());
        }
        else userList = messRepository.findAll();

        return userList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<UserResponse> getServicerProviderList(String name, Long collegeId) {

        List<ServiceProvider> userList = new ArrayList<>();

        if(StringUtils.hasText(name) && collegeId != null){
            userList = serviceProviderRepository.findByNameStartingWithAndCollege(name,collegeRepository.findById(collegeId).get());
        } else if (StringUtils.hasText(name)) {
            userList = serviceProviderRepository.findByNameStartingWith(name);
        } else if (collegeId != null) {
            userList = serviceProviderRepository.findByCollege(collegeRepository.findById(collegeId).get());
        }
        else userList = serviceProviderRepository.findAll();

        return userList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<CollegeResponse> getCollegeList(String name, City city) {

        List<College> collegeList = new ArrayList<>();

        if(StringUtils.hasText(name) && city != null){
            collegeList = collegeRepository.findByNameStartingWithAndCity(name,city);
        } else if (StringUtils.hasText(name)) {
            collegeList = collegeRepository.findByNameStartingWith(name);
        } else if (city != null) {
            collegeList = collegeRepository.findByCity(city);
        }
        else {
            collegeList = collegeRepository.findAll();
        }

        return collegeList.stream()
                .map(x -> x.to())
                .collect(Collectors.toList());

    }

    public List<RoomResponse> getRooms(Long collegeId, RoomType roomType, Integer range) {

        List<Room> roomList = new ArrayList<>();

        if(collegeId != null && roomType != null && range != null){

            roomList = roomRepository.findByCollegeAndRoomTypeAndCostLessThanEqualAndAvailabilityType(collegeRepository.findById(collegeId).get(),roomType, range,AvailabilityType.TOO_LATE);

        } else if (collegeId != null && roomType != null) {

            roomList = roomRepository.findByCollegeAndRoomTypeAndAvailabilityType(collegeRepository.findById(collegeId).get(),roomType,AvailabilityType.TOO_LATE);

        } else if (collegeId != null && range != null) {

            roomList = roomRepository.findByCollegeAndCostLessThanEqualAndAvailabilityType(collegeRepository.findById(collegeId).get(),range,AvailabilityType.TOO_LATE);

        } else if (roomType != null && range != null) {

            roomList = roomRepository.findByRoomTypeAndCostLessThanEqualAndAvailabilityType(roomType,range,AvailabilityType.TOO_LATE);

        } else if (collegeId != null) {

            roomList = roomRepository.findByCollegeAndAvailabilityType(collegeRepository.findById(collegeId).get(),AvailabilityType.TOO_LATE);

        } else if (roomType != null) {

            roomList = roomRepository.findByRoomTypeAndAvailabilityType(roomType,AvailabilityType.TOO_LATE);

        } else if (range != null) {

            roomList = roomRepository.findByCostLessThanEqualAndAvailabilityType(range, AvailabilityType.TOO_LATE);

        }
        else{

            roomList = roomRepository.findByAvailabilityType(AvailabilityType.TOO_LATE);

        }

        return roomList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<RoomResponse> getRooms(Long userId) {
        RoomOwner roomOwner = myUserRepository.findById(userId).get().getRoomOwner();
        if(roomOwner == null){
            throw new InvalidRequestStateException("Given User Is Not A Room Owner");
        }
        return roomOwner.getRoomList().stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<MessProductResponse> getProducts(Long userId) {
        Mess mess = myUserRepository.findById(userId).get().getMess();
        if(mess == null){
            throw new InvalidRequestStateException("Given User Is Not A Mess");
        }

        return mess.getMessProducts()
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

    public CollegeResponse getCollegeById(Long collegeId) {
        return collegeRepository.findById(collegeId).get().to();
    }

    public MessProductResponse getMessProductById(Long productId) throws IOException {
        MessProduct messProduct = messProductRepository.findById(productId).get();

        return messProduct.to();
    }

    public MessProductCartResponse getMessProductCartById(Long cartId) throws IOException {
        return messProductCartRepository.findById(cartId).get().to();
    }

    public RelationResponse getRelationById(Long relationId) throws IOException {
        return relationRepository.findById(relationId).get().to();
    }

    public RoomServiceResponse getRoomServiceById(Long serviceId) throws IOException {
        return roomServiceRepository.findById(serviceId).get().to();
    }

    public ServiceRequestResponse getServiceRequestById(Long serviceRequestId) throws IOException {
        return serviceRequestRepository.findById(serviceRequestId).get().to();
    }

    public ServiceTransactionResponse getServiceTransactionById(Long serviceTransactionId) throws IOException {
        return serviceTransactionRepository.findById(serviceTransactionId).get().to();
    }

    public TransactionResponse getTransactionbyId(Long transactionId) throws IOException {
        return transactionRepository.findById(transactionId).get().to();
    }

    public List<UserResponse> getRoomOwnerList(String name, Long collegeId) {
        List<RoomOwner> userList = new ArrayList<>();

        if(StringUtils.hasText(name) && collegeId != null){
            userList = roomHonourRepository.findByNameStartingWithAndCollege(name,collegeRepository.findById(collegeId).get());
        } else if (StringUtils.hasText(name)) {
            userList = roomHonourRepository.findByNameStartingWith(name);
        } else if (collegeId != null) {
            userList = roomHonourRepository.findByCollege(collegeRepository.findById(collegeId).get());
        }
        else userList = roomHonourRepository.findAll();

        return userList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<RoomServiceResponse> getServiceList(Long collegeId, City city, ServiceType serviceType) {
        List<RoomService> roomServiceList = new ArrayList<>();
        College college = null;

        if(collegeId != null) college = collegeRepository.findById(collegeId).get();

        if(collegeId != null && city != null && serviceType != null){
            roomServiceList = roomServiceRepository.findByCityAndServiceTypeAndCollege(city,serviceType,college);
        } else if (collegeId != null && city != null) {
            roomServiceList = roomServiceRepository.findByCityAndCollege(city,college);
        } else if (collegeId != null && serviceType != null) {
            roomServiceList = roomServiceRepository.findByServiceTypeAndCollege(serviceType,college);
        } else if (city != null && serviceType != null) {
            roomServiceList = roomServiceRepository.findByCityAndServiceType(city,serviceType);
        } else if (collegeId != null) {
            roomServiceList = roomServiceRepository.findByCollege(college);
        } else if (city != null) {
            roomServiceList = roomServiceRepository.findByCity(city);
        } else if (serviceType != null) {
            roomServiceList = roomServiceRepository.findByServiceType(serviceType);
        }
        else roomServiceList = roomServiceRepository.findAll();

        return roomServiceList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }
}
