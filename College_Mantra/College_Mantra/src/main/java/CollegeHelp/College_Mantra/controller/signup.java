package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.exceptions.WrongOTP;
import CollegeHelp.College_Mantra.request.MessCreateRequest;
import CollegeHelp.College_Mantra.request.RoomHonourCreateRequest;
import CollegeHelp.College_Mantra.request.ServiceProviderCreateRequest;
import CollegeHelp.College_Mantra.request.StudentCreateRequest;
import CollegeHelp.College_Mantra.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class signup {

    private static Logger LOGGER = LoggerFactory.getLogger(signup.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private RoomOwnerService roomOwnerService;

    @Autowired
    private MessService messService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private UserService userService;

    // localhost:8080/signup/student
    @PostMapping("/student")
    public void student(@RequestBody @Valid StudentCreateRequest studentCreateRequest){
        LOGGER.info("username Controller : {}",studentCreateRequest.getUsername());
        studentService.setInCache(studentCreateRequest);
    }

    // localhost:8080/signup/roomOwner
    @PostMapping("/roomOwner")
    public void roomHonour(@RequestBody @Valid RoomHonourCreateRequest roomHonourCreateRequest){
        roomOwnerService.setInCache(roomHonourCreateRequest);
    }

    // localhost:8080/signup/mess
    @PostMapping("/mess")
    public void mess(@RequestBody @Valid MessCreateRequest messCreateRequest){
        messService.setInCache(messCreateRequest);
    }

    // localhost:8080/signup/serviceProvider
    @PostMapping("/serviceProvider")
    public void serviceProvider(@RequestBody @Valid ServiceProviderCreateRequest serviceProviderCreateRequest){
        serviceProviderService.setInCache(serviceProviderCreateRequest);
    }

    // localhost:8080/signup/otp/mail
    @GetMapping("/otp/mail/{otp}")
    public ResponseEntity mailOTP(@PathVariable("otp") String otp) throws WrongOTP, InvalidIdException {
        boolean isCreated = verificationService.mailVerification(otp);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.OK).body("User is Created");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Please give OTP from number");
    }

    // localhost:8080/signup/otp/number
    @GetMapping("/otp/number/{otp}")
    public ResponseEntity numberOTP(@PathVariable("otp") String otp) throws WrongOTP, InvalidIdException {
        boolean isCreated = verificationService.numberVerification(otp);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.OK).body("User is Created");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Please give OTP from mail");
    }

    // localhost:8080/signup/forgot/password
    @PostMapping("/forgot/password")
    public boolean forgotPassword(@RequestParam("username") String username){
        return userService.forgotPassword(username);
    }

    // localhost:8080/signup/change/password
    @PostMapping("/change/password")
    public boolean changePassword(@RequestParam("otp") String otp,@RequestParam("newPassword") String newPassword){
        return userService.changePassword(otp,newPassword);
    }

}
