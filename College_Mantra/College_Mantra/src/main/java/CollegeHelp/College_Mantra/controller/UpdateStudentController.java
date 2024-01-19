package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.request.MessProductCartCreateRequest;
import CollegeHelp.College_Mantra.request.MessReputationCreateRequest;
import CollegeHelp.College_Mantra.request.ReputationCreateRequest;
import CollegeHelp.College_Mantra.request.ServiceProviderReputationCreateRequest;
import CollegeHelp.College_Mantra.service.StudentService;
import com.razorpay.RazorpayException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/student")
public class UpdateStudentController {

    @Autowired
    private StudentService studentService;

    // localhost:8080/student/upload/photo
    @PostMapping("/upload/photo")
    public ResponseEntity uploadPhoto(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        byte[] image = studentService.uploadPhoto(multipartFile,id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(multipartFile.getContentType()))
                .body(image);

    }

    // localhost:8080/student/update/address
    @PostMapping("/update/address")
    public ResponseEntity updateAddress(@RequestParam("address")
                                            @NotBlank(message = "Please Give Some Value")
                                            @NotNull(message = "Please Give Some Value")
                                            String address){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.updateAddress(id,address));
    }

    // localhost:8080/student/send/request
    @PostMapping("/send/request")
    public ResponseEntity sendRequest(@RequestParam("id") Long userId) throws InvalidIdException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.sendRequest(id,userId));

    }

    // localhost:8080/student/getSentRequest
    @GetMapping("/getSentRequest")
    public ResponseEntity getSentRequest(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getSentRequest(id));

    }

    // localhost:8080/student/getRequest
    @GetMapping("/getRequest")
    public ResponseEntity getRequest(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getRequest(id));

    }

    // localhost:8080/student/acceptRequest
    @GetMapping("/acceptRequest")
    public ResponseEntity acceptRequest(@RequestParam("id") Long userId,@RequestParam("roomId") Long roomId) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.acceptRequest(id,userId,roomId));

    }

    // localhost:8080/student/delete/sent/request
    @GetMapping("/delete/sent/request")
    public ResponseEntity deleteSentRequest(@RequestParam("id") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.deleteSentRequest(id,userId));
    }

    // localhost:8080/student/delete/get/request
    @GetMapping("/delete/get/request")
    public ResponseEntity deleteGetRequest(@RequestParam("id") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.deleteGetRequest(id,userId));
    }

    // localhost:8080/student/leave/room
    @GetMapping("/leave/room")
    public ResponseEntity leaveRoom(@RequestParam("id") Long relationId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.leaveRoom(id,relationId));
    }

    // localhost:8080/student/getRelations
    @GetMapping("/getRelations")
    public ResponseEntity getRelations(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getRelations(id));
    }

    // localhost:8080/student/callService
    @PostMapping("/callService")
    public ResponseEntity callService(@RequestParam("serviceId") Long serviceId) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.callService(id,serviceId));

    }

    // localhost:8080/student/deleteServiceRequest
    @PostMapping("/deleteServiceRequest")
    public ResponseEntity deleteServiceRequest(@RequestParam("serviceRequestId") Long serviceRequestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.deleteServiceRequest(id,serviceRequestId));
    }

    // localhost:8080/student/getServiceRequest
    @GetMapping("/getServiceRequest")
    public ResponseEntity getServiceRequest(@RequestParam("request_status")RequestStatus requestStatus){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getServiceRequest(id,requestStatus));

    }

    // localhost:8080/student/getTransactionList
    @GetMapping("/getTransactionList")
    public ResponseEntity getTransactionList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getTransactionList(id));
    }

    // localhost:8080/student/createTransaction
    @PostMapping("/createTransaction")
    public ResponseEntity createTransaction(@RequestBody @Valid MessProductCartCreateRequest cartCreateRequest) throws IOException, RazorpayException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.createTransaction(id,cartCreateRequest));
    }

    // localhost:8080/student/sendOrder
    @PostMapping("/sendOrder")
    public ResponseEntity sendOrder(@RequestParam("order_id") String order_id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.sendOrder(order_id));
    }

    // localhost:8080/student/getMessTransactions
    @GetMapping("/getMessTransactions")
    public ResponseEntity getTransactions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getMessTransaction(id));
    }

    // localhost:8080/student/getCarts
    @GetMapping("/getCarts")
    public ResponseEntity getCarts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(studentService.getCarts(id));
    }

    // localhost:8080/student/update/name
    @PostMapping("/update/name")
    public ResponseEntity updateName(@RequestParam("name") String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.updateName(id,name));
    }

    // localhost:8080/student/get/roomOwner
    @GetMapping("/get/roomOwner")
    public ResponseEntity getRoomOwner() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.getRoomOwner(id));
    }

    // localhost:8080/student/give/reputation/owner
    @PostMapping("/give/reputation/owner")
    public ResponseEntity giveRatingToOwner(@RequestBody @Valid ReputationCreateRequest reputationCreateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.giveRatingToOwner(id,reputationCreateRequest));
    }

    // localhost:8080/student/give/reputation/serviceProvider
    @PostMapping("/give/reputation/serviceProvider")
    public ResponseEntity giveRatingToServiceProvider(@RequestBody @Valid ServiceProviderReputationCreateRequest serviceProviderReputationCreateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.giveRatingToServiceProvider(id,serviceProviderReputationCreateRequest));
    }

    // localhost:8080/student/give/reputation/mess
    @PostMapping("/give/reputation/mess")
    public ResponseEntity giveReputationToMess(@RequestBody @Valid MessReputationCreateRequest messReputationCreateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getStudent().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentService.giveReputationToMess(id,messReputationCreateRequest));
    }

}
