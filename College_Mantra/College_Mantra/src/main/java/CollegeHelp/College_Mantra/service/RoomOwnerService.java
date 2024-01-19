package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RelationStatus;
import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.*;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomOwnerService implements Serializable {

    private String folderPath = "D:\\Java-Backend-Projects\\CollegeHelp\\College_Mantra\\College_Mantra\\src\\main\\resources\\profilePhotos\\";

    private static Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Value("${mail.prefix}")
    private String MAIL_PREFIX;

    @Value("${number.prefix}")
    private String NUMBER_PREFIX;

    @Autowired
    private MyCacheRepository myCacheRepository;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private RoomHonourRepository roomHonourRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private MessRepository messRepository;

    @Autowired
    private ReputationRepository reputationRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public void setInCache(RoomHonourCreateRequest roomHonourCreateRequest) {

        String tempId = UUID.randomUUID().toString();

        CreationRequestHolder creationRequestHolder = CreationRequestHolder.builder()
                .roomHonourCreateRequest(roomHonourCreateRequest)
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
                .number(roomHonourCreateRequest.getPhoneNumber())
                .mail(roomHonourCreateRequest.getGmail()).build();

        kafkaProducer.sendInBoth(message);

    }

    public void create(RoomHonourCreateRequest roomHonourCreateRequest) throws InvalidIdException {
        LOGGER.info("Username : {}",roomHonourCreateRequest.getUsername());
        RoomOwner roomOwner = roomHonourCreateRequest.to();
        roomOwner.setCollege(collegeService.findById(roomHonourCreateRequest.getCollegeId()));
        UserCreateRequest userCreateRequest = roomHonourCreateRequest.toUser();
        User myUser = myUserDetailsService.create(userCreateRequest);
        roomOwner.setUser(myUser);
        try {
            roomHonourRepository.save(roomOwner);
        } catch (Exception ex){
            myUserRepository.delete(myUser);
            throw ex;
        }
    }

    public UserResponse findById(Long id) throws IOException {

        Optional<RoomOwner> optionalStudent = roomHonourRepository.findById(id);

        return optionalStudent.get().to();

    }

    public byte[] uploadPhoto(MultipartFile multipartFile, Long id) throws IOException {

        LOGGER.info("94");

        String imagePath = folderPath + multipartFile.getOriginalFilename();

        RoomOwner roomOwner = roomHonourRepository.findById(id).get();

        ProfileImage profileImage = ProfileImage.builder()
                .type(multipartFile.getContentType())
                .roomOwner(roomOwner)
                .filePath(imagePath)
                .name(multipartFile.getOriginalFilename())
                .build();

        ProfileImage existingImg = roomOwner.getProfileImage();

        if(existingImg != null){
            roomOwner.setProfileImage(null);
            roomOwner = roomHonourRepository.save(roomOwner);
            profileImageRepository.deleteById(existingImg.getId());
            File file = new File(existingImg.getFilePath());
            file.delete();
        }

        LOGGER.info("114");

        profileImage = profileImageRepository.save(profileImage);

        roomOwner.setProfileImage(profileImage);

        roomHonourRepository.save(roomOwner);

        LOGGER.info("120");

        multipartFile.transferTo(new File(profileImage.getFilePath()));

        LOGGER.info("124");

        return DownloadImage.downloadImage(profileImage.getFilePath());

    }

    public List<UserResponse> getSentRequest(Long id) {

        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        List<Student> studentList = roomOwner.getRequestSent();

        return studentList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

    }

    public List<UserResponse> getRequest(Long id) {

        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        List<Student> students = roomOwner.getRequestGot();

        return students.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    public String sendRequest(Long id, Long userId) throws InvalidIdException {

        Optional<User> optionalUser = myUserRepository.findById(userId);
        Student student = optionalUser.get().getStudent();

        if(optionalUser.get().getAuthority() != Authority.STUDENT){
            throw new InvalidIdException("Given User Is Not ROOM_HONOUR");
        }

        if(student.getRoomOwner() != null){
            throw new InvalidRequestStateException("Student Already Have A Room");
        }

        RoomOwner roomOwner = roomHonourRepository.findById(id).get();

        List<RoomOwner> roomOwners = student.getRequestGot();

        roomOwners.forEach(x -> {
            if(x.getId() == roomOwner.getId()){
                throw new InvalidRequestStateException("You Have Already Sent Request To This User");
            }
        });

        roomOwners.add(roomOwner);

        MailMessage message = MailMessage.builder()
                .mailMessage("Hey User You Got Request From Room Owner "
                        + roomOwner.getName() + ". " + roomOwner.getName()
                        +" is waiting for your acceptance.")
                .mail(student.getGmail()).build();

        kafkaProducer.sendInMail(message);

        student.setRequestGot(roomOwners);
        student = studentRepository.save(student);

        return "Successfully Sent";

    }

    public List<RelationResponse> acceptRequest(Long id, Long userId, Long roomId) throws IOException {

        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Student student = myUserRepository.findById(userId).get().getStudent();
        Room room = roomRepository.findById(roomId).get();

        if(room.getRoomOwner().getId() != roomOwner.getId()){
            throw new InvalidRequestStateException("Room Is Not Related To This Owner");
        }

        if(room.getAvailabilityType().equals(AvailabilityType.ACQUIRED)){
            throw new InvalidRequestStateException("Room Is Already Acquired");
        }

        if(student.getRequestSent().remove(roomOwner)){
            student.setRequestSent(new ArrayList<>());
            student.setRequestGot(new ArrayList<>());
            student.setRoomOwner(roomOwner);

            room.setAvailabilityType(AvailabilityType.ACQUIRED);

            Relation relation = Relation.builder()
                    .roomOwner(roomOwner)
                    .rent(room.getCost())
                    .relationStatus(RelationStatus.ACTIVE)
                    .student(student)
                    .room(room)
                    .build();

            relation = relationRepository.save(relation);
        }

        student = studentRepository.save(student);

        return student.getRelationList().stream()
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
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Student student = myUserRepository.findById(userId).get().getStudent();

        List<RoomOwner> getRequest = student.getRequestGot();

        boolean res = getRequest.remove(roomOwner);

        student.setRequestGot(getRequest);

        studentRepository.save(student);

        return res;
    }

    public boolean deleteGetRequest(Long id, Long userId) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Student student = myUserRepository.findById(userId).get().getStudent();

        List<RoomOwner> sentRequest = student.getRequestSent();

        boolean res = sentRequest.remove(roomOwner);

        student.setRequestSent(sentRequest);

        studentRepository.save(student);

        return res;
    }

    public RelationResponse removeStudent(Long id, Long relationId) throws IOException {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Relation relation = relationRepository.findById(relationId).get();
        List<Relation> relationList = roomOwner.getRelationList();

        if(relation.getRelationStatus().equals(RelationStatus.ACTIVE) &&
                relationList.contains(relation)){

            Room room = relation.getRoom();
            room.setAvailabilityType(AvailabilityType.TOO_LATE);
            relation.setRelationStatus(RelationStatus.OVER);
            Student student = relation.getStudent();
            student.setRoomOwner(null);
            relationRepository.save(relation);
            studentRepository.save(student);

            MailMessage message = MailMessage.builder()
                    .mail(student.getGmail())
                    .mailMessage("Hello Student You have been removed by you room owner "
                                    + roomOwner.getName()).build();

            kafkaProducer.sendInMail(message);

            return relation.to();
        }

        throw new InvalidRequestStateException("Invalid Action");
    }

    public List<RelationResponse> getRelations(Long id) {
        return roomHonourRepository.findById(id).get().getRelationList()
                .stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public String updateName(Long id, String name) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();

        roomOwner.setName(name);
        roomOwner = roomHonourRepository.save(roomOwner);

        return roomOwner.getName();
    }

    public CollegeResponse updateCollege(Long id, Long collegeId) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        College college = collegeRepository.findById(collegeId).get();

        roomOwner.setCollege(college);
        roomOwner = roomHonourRepository.save(roomOwner);

        return roomOwner.getCollege().to();
    }

    public List<UserResponse> getStudents(Long id) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();

        return roomOwner.getStudentList()
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

    public List<RoomResponse> getPostedRoom(Long id) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();

        return roomOwner.getRoomList()
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

    public String updateRoom(Long id, Long roomId, RoomCreateRequest roomCreateRequest, List<MultipartFile> images) throws IOException {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Room room = roomRepository.findById(roomId).get();

        if(room.getRoomOwner().equals(roomOwner) == false){
            throw new InvalidRequestStateException("This Room Doesn't Belongs To This User");
        }

        Room updatedRoom = roomCreateRequest.to();
        updatedRoom.setAvailabilityType(room.getAvailabilityType());
        updatedRoom.setId(room.getId());
        updatedRoom.setRoomOwner(roomOwner);

        List<ProfileImage> existingImg = room.getImages();

        existingImg.stream()
                .forEach(x -> {
                    profileImageRepository.deleteById(x.getId());
                    File file = new File(x.getFilePath());
                    file.delete();
                });

        List<ProfileImage> uploadImages = RoomService.uploadImages(images,updatedRoom);
        updatedRoom = roomRepository.save(updatedRoom);
        profileImageRepository.saveAll(uploadImages);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/room/")
                .path(String.valueOf(updatedRoom.getId()))
                .toUriString();
    }

    public ReputationResponse giveReputationToStudent(Long id, ReputationCreateRequest reputationCreateRequest) {
        RoomOwner roomOwner = roomHonourRepository.findById(id).get();
        Relation relation = relationRepository.findById(reputationCreateRequest.getRelationId()).get();

        if(!relation.getRoomOwner().equals(roomOwner)){
            throw new InvalidRequestStateException("Invalid Action");
        }

        Reputation reputation = reputationRepository.findByUser1AndUser2(roomOwner.getUser(),relation.getStudent().getUser());

        if(reputation != null){
            throw new InvalidRequestStateException("Invalid Action");
        }

        User userRoomOwner = roomOwner.getUser();
        Student student = relation.getStudent();
        User userStudent = student.getUser();

        reputation = reputationCreateRequest.to();
        reputation.setUser1(userRoomOwner);
        reputation.setUser2(userStudent);

        reputation = reputationRepository.save(reputation);

        long number = student.getNumberOfReputations();
        double currRating = student.getRating();

        double total = currRating * number;

        total += reputation.getRating();
        number++;

        currRating = total/number;

        student.setRating(currRating);
        student.setNumberOfReputations(number);

        roomHonourRepository.save(roomOwner);

        MailMessage message = MailMessage.builder()
                .mailMessage("Hello Student you got a reputation by "
                                + roomOwner.getName())
                .mail(student.getGmail()).build();

        kafkaProducer.sendInMail(message);

        return reputation.to();
    }
}
