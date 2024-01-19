package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.*;
import CollegeHelp.College_Mantra.repository.*;
import CollegeHelp.College_Mantra.request.*;
import CollegeHelp.College_Mantra.response.*;
import com.sun.jdi.request.InvalidRequestStateException;
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
public class MessService {



    @Autowired
    private MessRepository messRepository;

    @Autowired
    private MyCacheRepository myCacheRepository;

    private String folderPath = "D:\\Java-Backend-Projects\\CollegeHelp\\College_Mantra\\College_Mantra\\src\\main\\resources\\profilePhotos\\";

    private static Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Value("${mail.prefix}")
    private String MAIL_PREFIX;

    @Value("${number.prefix}")
    private String NUMBER_PREFIX;

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private MessProductRepository messProductRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public void setInCache(MessCreateRequest messCreateRequest) {

        String tempId = UUID.randomUUID().toString();

        CreationRequestHolder creationRequestHolder = CreationRequestHolder.builder()
                .messCreateRequest(messCreateRequest)
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
                .number(messCreateRequest.getPhoneNumber())
                .mail(messCreateRequest.getGmail()).build();

        kafkaProducer.sendInBoth(message);

    }

    public void create(MessCreateRequest messCreateRequest) throws InvalidIdException {
        LOGGER.info("Username : {}",messCreateRequest.getUsername());
        Mess mess = messCreateRequest.to();
        mess.setCollege(collegeService.findById(messCreateRequest.getCollegeId()));
        UserCreateRequest userCreateRequest = messCreateRequest.toUser();
        User myUser = myUserDetailsService.create(userCreateRequest);
        mess.setUser(myUser);
        try {
            messRepository.save(mess);
        } catch (Exception ex){
            myUserRepository.delete(myUser);
            throw ex;
        }
    }

    public byte[] uploadPhoto(MultipartFile multipartFile, Long id) throws IOException {

        LOGGER.info("94");

        String imagePath = folderPath + multipartFile.getOriginalFilename();

        Mess mess = messRepository.findById(id).get();

        ProfileImage profileImage = ProfileImage.builder()
                .type(multipartFile.getContentType())
                .mess(mess)
                .filePath(imagePath)
                .name(multipartFile.getOriginalFilename())
                .build();

        ProfileImage existingImg = mess.getProfileImage();

        if(existingImg != null){
            mess.setProfileImage(null);
            mess = messRepository.save(mess);
            profileImageRepository.deleteById(existingImg.getId());
            File file = new File(existingImg.getFilePath());
            file.delete();
        }

        LOGGER.info("114");

        profileImage = profileImageRepository.save(profileImage);

        mess.setProfileImage(profileImage);

        messRepository.save(mess);

        LOGGER.info("120");

        multipartFile.transferTo(new File(profileImage.getFilePath()));

        LOGGER.info("124");

        return DownloadImage.downloadImage(profileImage.getFilePath());

    }

    public UserResponse findById(Long id) throws IOException {

        Optional<Mess> optionalStudent = messRepository.findById(id);

        return optionalStudent.get().to();

    }

    public MessProductResponse addProduct(Long id, MessProductCreateRequest messProductCreateRequest) throws IOException {

        Mess mess = messRepository.findById(id).get();

        MessProduct messProduct = messProductCreateRequest.to();
        messProduct.setMess(mess);

        return messProductRepository.save(messProduct).to();

    }

    public List<MessProductResponse> getProducts(Long id) {

        Mess mess = messRepository.findById(id).get();

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

    public String updateAddresss(Long id, String address) {
        Mess mess = messRepository.findById(id).get();
        mess.setAddress(address);
        mess = messRepository.save(mess);

        return mess.getAddress();
    }

    public void addPaymentMethod(Long id, String keyId, String keySecret) {

        Mess mess = messRepository.findById(id).get();

        mess.setKeyId(keyId);
        mess.setKeySecret(keySecret);

        mess = messRepository.save(mess);

    }

    public List<TransactionResponse> getMessTransactions(Long id) {
        Mess mess = messRepository.findById(id).get();
        return mess.getTransactionList().stream()
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
        Mess mess = messRepository.findById(id).get();

        return mess.getMessProductCarts()
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
        Mess mess = messRepository.findById(id).get();

        mess.setName(name);
        mess = messRepository.save(mess);

        return mess.getName();
    }

    public CollegeResponse updateCollege(Long id, Long collegeId) {
        Mess mess = messRepository.findById(id).get();

        College college = collegeRepository.findById(collegeId).get();

        mess.setCollege(college);
        mess = messRepository.save(mess);

        return mess.getCollege().to();
    }

    public MessProductResponse updateProduct(Long id, Long productId, MessProductCreateRequest messProductCreateRequest) throws IOException {

        Mess mess = messRepository.findById(id).get();

        MessProduct currentProduct = messProductRepository.findById(productId).get();

        if(currentProduct.getMess().equals(mess) == false){
            throw new InvalidRequestStateException("Mess Don't Have This Product");
        }

        MessProduct updatedProduct = messProductCreateRequest.to();

        updatedProduct.setId(currentProduct.getId());
        updatedProduct.setMess(mess);

        return messProductRepository.save(updatedProduct).to();

    }
}
