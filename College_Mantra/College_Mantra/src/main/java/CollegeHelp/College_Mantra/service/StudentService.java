package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RelationStatus;
import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.*;
import CollegeHelp.College_Mantra.model.RoomService;
import CollegeHelp.College_Mantra.repository.*;
import CollegeHelp.College_Mantra.request.*;
import CollegeHelp.College_Mantra.response.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sun.jdi.request.InvalidRequestStateException;
import org.example.dto.MailMessage;
import org.example.dto.MessageMail_SMS;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.InvalidTransactionException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

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
    private StudentRepository studentRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RoomServiceRepository roomServiceRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private MessProductRepository messProductRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private MessProductCartRepository messProductCartRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ReputationRepository reputationRepository;
    @Autowired
    private RoomHonourRepository roomHonourRepository;
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public void setInCache(StudentCreateRequest studentCreateRequest) {

        String tempId = UUID.randomUUID().toString();

        CreationRequestHolder creationRequestHolder = CreationRequestHolder.builder()
                .studentCreateRequest(studentCreateRequest)
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
                .number(studentCreateRequest.getPhoneNumber())
                .mail(studentCreateRequest.getGmail()).build();

        kafkaProducer.sendInBoth(message);

    }

    public void create(StudentCreateRequest studentCreateRequest) throws InvalidIdException {
        LOGGER.info("Username : {}",studentCreateRequest.getUsername());
        Student student = studentCreateRequest.to();
        student.setCollege(collegeService.findById(studentCreateRequest.getCollegeId()));
        UserCreateRequest userCreateRequest = studentCreateRequest.toUser();
        User myUser = myUserDetailsService.create(userCreateRequest);
        student.setUser(myUser);

        try {
            studentRepository.save(student);
        }catch (Exception ex){
            myUserRepository.delete(myUser);
            throw ex;
        }
    }

    public byte[] uploadPhoto(MultipartFile multipartFile,Long id) throws IOException {

        LOGGER.info("94");

        String imagePath = folderPath + multipartFile.getOriginalFilename();

        Student student = studentRepository.findById(id).get();

        ProfileImage profileImage = ProfileImage.builder()
                .type(multipartFile.getContentType())
                .student(student)
                .filePath(imagePath)
                .name(multipartFile.getOriginalFilename())
                .build();

        ProfileImage existingImg = student.getProfileImage();

        if(existingImg != null){
            student.setProfileImage(null);
            student = studentRepository.save(student);
            profileImageRepository.deleteById(existingImg.getId());
            File file = new File(existingImg.getFilePath());
            file.delete();
        }

        LOGGER.info("114");

        profileImage = profileImageRepository.save(profileImage);

        student.setProfileImage(profileImage);

        studentRepository.save(student);

        LOGGER.info("120");

        multipartFile.transferTo(new File(profileImage.getFilePath()));

        LOGGER.info("124");

        return DownloadImage.downloadImage(profileImage.getFilePath());

    }

    public UserResponse findById(Long id) throws IOException {

        Optional<Student> optionalStudent = studentRepository.findById(id);

        return optionalStudent.get().to();

    }

    public String sendRequest(Long id, Long userId) throws InvalidIdException,InvalidRequestStateException {

        Optional<User> optionalUser = myUserRepository.findById(userId);
        Student student = studentRepository.findById(id).get();

        if(optionalUser.get().getAuthority() != Authority.ROOM_HONOUR){
            throw new InvalidIdException("Given User Is Not ROOM_HONOUR");
        }

        if(student.getRoomOwner() != null){
            throw new InvalidRequestStateException("You Have To Leave The Room First");
        }

        List<RoomOwner> requestSent = student.getRequestSent();

        requestSent.forEach(x -> {
            if(x.getId() == optionalUser.get().getRoomOwner().getId()){
                throw new InvalidRequestStateException("You Have Already Sent Request To This User");
            }
        });

        requestSent.add(optionalUser.get().getRoomOwner());

        student.setRequestSent(requestSent);

        student = studentRepository.save(student);

        MailMessage mailMessage = MailMessage.builder()
                .mailMessage("You have got a request from + " + student.getName())
                .mail(optionalUser.get().getRoomOwner().getGmail())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return "Successfully Sent";

    }

    public List<UserResponse> getSentRequest(Long id) {

        Student student = studentRepository.findById(id).get();
        List<RoomOwner> roomOwners = student.getRequestSent();

        return roomOwners.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

    }

    public List<UserResponse> getRequest(Long id) {

        Student student = studentRepository.findById(id).get();
        List<RoomOwner> roomOwners = student.getRequestGot();

        return roomOwners.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public List<RelationResponse> acceptRequest(Long id, Long userId, Long roomId) throws IOException {

        Student student = studentRepository.findById(id).get();
        RoomOwner roomOwner = myUserRepository.findById(userId).get().getRoomOwner();
        Room room = roomRepository.findById(roomId).get();

        if(room.getRoomOwner().getId() != roomOwner.getId()){
            throw new InvalidRequestStateException("Room Is Not Related To This Owner");
        }

        if(room.getAvailabilityType().equals(AvailabilityType.ACQUIRED)){
            throw new InvalidRequestStateException("Room Is Already Acquired");
        }

        List<RoomOwner> roomOwners = student.getRequestGot();

        if(roomOwners.remove(roomOwner)){
            student.setRequestSent(new ArrayList<>());
            student.setRequestGot(new ArrayList<>());
            student.setRoomOwner(roomOwner);

            room.setAvailabilityType(AvailabilityType.ACQUIRED);

            Relation relation = Relation.builder()
                    .rent(room.getCost())
                    .relationStatus(RelationStatus.ACTIVE)
                    .student(student)
                    .roomOwner(roomOwner)
                    .room(room)
                    .build();

            relation = relationRepository.save(relation);

            roomRepository.save(room);
        }

        student = studentRepository.save(student);

        MailMessage mailMessage = MailMessage.builder()
                .mail(roomOwner.getGmail())
                .mailMessage("Your request has been accepted by " + student.getName()).build();

        kafkaProducer.sendInMail(mailMessage);

        return student.getRelationList()
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

    public boolean deleteSentRequest(Long id, Long userId) {
        Student student = studentRepository.findById(id).get();
        RoomOwner roomOwner = myUserRepository.findById(userId).get().getRoomOwner();

        List<RoomOwner> sentRequest = student.getRequestSent();

        boolean res = sentRequest.remove(roomOwner);

        student.setRequestSent(sentRequest);

        studentRepository.save(student);

        return res;
    }

    public boolean deleteGetRequest(Long id, Long userId) {
        Student student = studentRepository.findById(id).get();
        RoomOwner roomOwner = myUserRepository.findById(userId).get().getRoomOwner();

        List<RoomOwner> getRequest = student.getRequestGot();

        boolean res = getRequest.remove(roomOwner);

        student.setRequestGot(getRequest);

        studentRepository.save(student);

        MailMessage mailMessage = MailMessage.builder()
                .mailMessage("Your request has been rejected by + " + student.getName())
                .mail(roomOwner.getGmail())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return res;
    }

    public RelationResponse leaveRoom(Long id, Long relationId) throws IOException {
        Student student = studentRepository.findById(id).get();
        Relation relation = relationRepository.findById(relationId).get();
        List<Relation> relationList = student.getRelationList();

        if(relation.getRelationStatus().equals(RelationStatus.ACTIVE) &&
                relationList.contains(relation)){

            Room room = relation.getRoom();
            room.setAvailabilityType(AvailabilityType.TOO_LATE);
            relation.setRelationStatus(RelationStatus.OVER);
            relation.setRoom(room);
            student.setRoomOwner(null);
            relationRepository.save(relation);
            studentRepository.save(student);

            MailMessage mailMessage = MailMessage.builder()
                    .mail(room.getRoomOwner().getGmail())
                    .mailMessage("Your room has been left by " + student.getName())
                    .build();

            kafkaProducer.sendInMail(mailMessage);

            return relation.to();
        }

        throw new InvalidRequestStateException("Invalid Action");
    }

    public List<RelationResponse> getRelations(Long id) {
        return studentRepository.findById(id).get().getRelationList()
                .stream()
                .map(x -> {
            try {
                return x.to();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public ServiceRequestResponse callService(Long id, Long serviceId) throws IOException {

        Student student = studentRepository.findById(id).get();
        RoomService roomService = roomServiceRepository.findById(serviceId).get();

        ServiceRequest serviceRequest = ServiceRequest.builder()
                .serviceProvider(roomService.getServiceProvider())
                .roomService(roomService)
                .student(student)
                .requestStatus(RequestStatus.PENDING)
                .build();

        serviceRequest = serviceRequestRepository.save(serviceRequest);

        MailMessage mailMessage = MailMessage.builder()
                .mailMessage("You have got a request by " + student.getName())
                .mail(roomService.getServiceProvider().getGmail())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return serviceRequest.to();

    }

    public boolean deleteServiceRequest(Long id, Long serviceRequestId) {
        Student student = studentRepository.findById(id).get();
        List<ServiceRequest> serviceRequestList = student.getServiceRequests();
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId).get();

        if(serviceRequestList.contains(serviceRequest)){
            serviceRequestRepository.deleteById(serviceRequestId);
            return true;
        }

        return false;
    }

    public List<ServiceRequestResponse> getServiceRequest(Long id, RequestStatus requestStatus) {
        Student student = studentRepository.findById(id).get();
        return serviceRequestRepository.findByStudentAndRequestStatus(student,requestStatus)
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

    public List<ServiceTransactionResponse> getTransactionList(Long id) {

        Student student = studentRepository.findById(id).get();

        return student.getServiceTransactions().stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public String createTransaction(Long id, MessProductCartCreateRequest cartCreateRequest) throws IOException, RazorpayException {

        List<MessProduct> messProductList = cartCreateRequest.getMessProductId()
                .stream()
                .map(x -> messProductRepository.findById(x).get())
                .collect(Collectors.toList());

        int amount = 0;
        Iterator<MessProduct> iterator = messProductList.listIterator();
        while(iterator.hasNext()){
            amount += iterator.next().getPrice();
        }

        Mess mess = messRepository.findById(cartCreateRequest.getMessId()).get();

        if(mess.getKeyId() == null || mess.getKeySecret() == null){
            throw new InvalidTransactionException("Mess Didn't Added RazorPay Account");
        }

        Student student = studentRepository.findById(id).get();
        String address = student.getAddress();
        if(address == null){
            throw new InvalidTransactionException("Please Set Address");
        }

        MessProductCart messProductCart = MessProductCart.builder()
                .mess(mess)
                .messProductList(messProductList)
                .student(student)
                .build();

        String order_id = UUID.randomUUID().toString();
        String description = createTransactionDescription(messProductCart,amount,order_id,address);

        TransactionCreateRequest transactionCreateRequest = TransactionCreateRequest.builder()
                .order_id(order_id)
                .amount(amount)
                .messId(id)
                .studentId(student.getId())
                .description(description)
                .messProductCart(messProductCart)
                .build();

        myCacheRepository.setTempIdValue(transactionCreateRequest.getOrder_id(),transactionCreateRequest);

        RazorpayClient razorpay = new RazorpayClient(mess.getKeyId(), mess.getKeySecret());

        JSONObject options = new JSONObject();
        options.put("amount", amount*100);
        options.put("currency", "INR");
        options.put("receipt", transactionCreateRequest.getOrder_id());

        JSONObject notes = new JSONObject();
        notes.put("key",mess.getKeyId());
        notes.put("name",mess.getName());
        notes.put("description",transactionCreateRequest.getDescription());
        notes.put("address",student.getAddress());
        options.put("notes",notes);

        Order order = razorpay.Orders.create(options);

        return order.toString();

    }

    private String createTransactionDescription(MessProductCart messProductCart,int amount,String order_id,String address) {
        String description = "Transaction Id : " + order_id + "\n"
                            + "Mess : " + messProductCart.getMess().getName() + "\n"
                            + "Student : " + messProductCart.getStudent().getName() + "\n"
                            + "Address : " + address + "\n";

        ListIterator<MessProduct> iterator = messProductCart.getMessProductList().listIterator();
        while(iterator.hasNext()){
            MessProduct messProduct = iterator.next();
            description += messProduct.getProductName() + " : " + messProduct.getPrice() + "\n";
        }

        description += "Total Amount Paid : " + amount;

        return description;
    }

    public String updateAddress(Long id,String address) {
        Student student = studentRepository.findById(id).get();
        student.setAddress(address);
        student = studentRepository.save(student);

        return student.getAddress();
    }

    public TransactionResponse sendOrder(String order_id) throws IOException {

        TransactionCreateRequest transactionCreateRequest = (TransactionCreateRequest) myCacheRepository.get(order_id);
        if(transactionCreateRequest == null) throw new InvalidTransactionException("No Order Found Now");
        myCacheRepository.delete(order_id);

        MessProductCart messProductCart = transactionCreateRequest.getMessProductCart();

        messProductCart = messProductCartRepository.save(messProductCart);

        Transaction transaction = transactionCreateRequest.to();
        transaction.setMess(messRepository.findById(transactionCreateRequest.getMessId()).get());
        transaction.setStudent(studentRepository.findById(transactionCreateRequest.getMessId()).get());

        transaction = transactionRepository.save(transaction);

        return transaction.to();

    }

    public List<TransactionResponse> getMessTransaction(Long id) {
        Student student = studentRepository.findById(id).get();
        return student.getTransactionList().stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<MessProductCartResponse> getCarts(Long id) {
        Student student = studentRepository.findById(id).get();

        return student.getMessProductCarts()
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

    public String updateName(Long id,String name) {
        Student student = studentRepository.findById(id).get();

        student.setName(name);
        student = studentRepository.save(student);

        return student.getName();
    }

    public UserResponse getRoomOwner(Long id) throws IOException {
        Student student = studentRepository.findById(id).get();
        RoomOwner roomOwner = student.getRoomOwner();

        if(roomOwner == null) throw new InvalidRequestStateException("User Don't Have A Room Owner");

        return roomOwner.to();
    }

    public ReputationResponse giveRatingToOwner(Long id,ReputationCreateRequest reputationCreateRequest) {
        Student student = studentRepository.findById(id).get();
        Relation relation = relationRepository.findById(reputationCreateRequest.getRelationId()).get();

        if(!relation.getStudent().equals(student)){
            throw new InvalidRequestStateException("Invalid Action");
        }

        Reputation reputation = reputationRepository.findByUser1AndUser2(student.getUser(),relation.getRoomOwner().getUser());

        if(reputation != null){
            throw new InvalidRequestStateException("Invalid Action");
        }

        User userStudent = student.getUser();
        RoomOwner roomOwner = relation.getRoomOwner();
        User userRoomOwner = roomOwner.getUser();

        reputation = reputationCreateRequest.to();
        reputation.setUser1(userStudent);
        reputation.setUser2(userRoomOwner);

        reputation = reputationRepository.save(reputation);

        long number = roomOwner.getNumberOfReputation();
        double currRating = roomOwner.getRating();

        double total = currRating * number;

        total += reputation.getRating();
        number++;

        currRating = total/number;

        roomOwner.setRating(currRating);
        roomOwner.setNumberOfReputation(number);

        roomHonourRepository.save(roomOwner);

        MailMessage mailMessage = MailMessage.builder()
                .mail(roomOwner.getGmail())
                .mailMessage("You have got a reputation by " + student.getName())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return reputation.to();
    }

    public ReputationResponse giveRatingToServiceProvider(Long id, ServiceProviderReputationCreateRequest serviceProviderReputationCreateRequest) {
        ServiceProvider serviceProvider = (ServiceProvider) myCacheRepository.get(serviceProviderReputationCreateRequest.getTransactionId());
        if(serviceProvider == null){
            throw new InvalidRequestStateException("Invalid Action");
        }

        Student student = studentRepository.findById(id).get();

        Reputation reputation = Reputation.builder()
                .user1(student.getUser())
                .user2(serviceProvider.getUser())
                .rating(serviceProviderReputationCreateRequest.getRating())
                .comment(serviceProviderReputationCreateRequest.getComment())
                .build();

        long number = serviceProvider.getNumberOfReputations();
        double currRating = serviceProvider.getRating();

        double total = currRating * number;

        total += reputation.getRating();
        number++;

        currRating = total/number;

        serviceProvider.setRating(currRating);
        serviceProvider.setNumberOfReputations(number);

        List<ServiceProvider> serviceProviders = Arrays.asList(serviceProvider);

        serviceProviderRepository.saveAll(serviceProviders);

        reputation = reputationRepository.save(reputation);

        myCacheRepository.delete(serviceProviderReputationCreateRequest.getTransactionId());

        MailMessage mailMessage = MailMessage.builder()
                .mailMessage("You have got a reputation by " + student.getName())
                .mail(serviceProvider.getGmail())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return reputation.to();
    }

    public ReputationResponse giveReputationToMess(Long id, MessReputationCreateRequest messReputationCreateRequest) {
        Student student = studentRepository.findById(id).get();
        Mess mess = myUserRepository.findById(messReputationCreateRequest.getMessUserId()).get().getMess();

        Reputation reputation = reputationRepository.findByUser1AndUser2(student.getUser(),mess.getUser());

        if(reputation != null){
            throw new InvalidRequestStateException("Invalid Action");
        }

        reputation = Reputation.builder()
                .user1(student.getUser())
                .user2(mess.getUser())
                .rating(messReputationCreateRequest.getRating())
                .comment(messReputationCreateRequest.getComment())
                .build();

        reputation = reputationRepository.save(reputation);

        long number = mess.getNumberOfReputations();
        double currRating = mess.getRating();

        double total = currRating * number;

        total += reputation.getRating();
        number++;

        currRating = total/number;

        mess.setRating(currRating);
        mess.setNumberOfReputations(number);

        messRepository.save(mess);

        MailMessage mailMessage = MailMessage.builder()
                .mailMessage("You have got a reputation by " + student.getName())
                .mail(mess.getGmail())
                .build();

        kafkaProducer.sendInMail(mailMessage);

        return reputation.to();
    }

}
