package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.request.RoomServiceRequest;
import CollegeHelp.College_Mantra.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/serviceProvider")
public class UpdateServiceProviderController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    // localhost:8080/serviceProvider/upload/photo
    @PostMapping("/upload/photo")
    public ResponseEntity uploadPhoto(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        byte[] image = serviceProviderService.uploadPhoto(multipartFile,id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(multipartFile.getContentType()))
                .body(image);

    }

    // localhost:8080/serviceProvider/addService
    @PostMapping("/addService")
    public ResponseEntity addService(@RequestBody @Valid RoomServiceRequest roomServiceRequest) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.addService(id,roomServiceRequest));

    }

    // localhost:8080/serviceProvider/getPostedServices
    @GetMapping("/getPostedServices")
    public ResponseEntity getServicesList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.getServicesList(id));
    }

    // localhost:8080/serviceProvider/getServiceRequest
    @GetMapping("/getServiceRequest")
    public ResponseEntity getServiceRequest(@RequestParam("request_status")RequestStatus requestStatus){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.getServiceRequest(id,requestStatus));

    }

    // localhost:8080/serviceProvider/deleteServiceRequest
    @PostMapping("/deleteServiceRequest")
    public ResponseEntity deleteServiceRequest(@RequestParam("serviceRequestId") Long serviceRequestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.deleteServiceRequest(id,serviceRequestId));
    }

    // localhost:8080/serviceProvider/acceptRequest
    @PostMapping("/acceptRequest")
    public ResponseEntity acceptRequest(@RequestParam("serviceRequestId") Long serviceRequestId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.acceptRequest(id,serviceRequestId));
    }

    // localhost:8080/serviceProvider/getTransactionList
    @GetMapping("/getTransactionList")
    public ResponseEntity getTransactionList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceProviderService.getTransactionList(id));
    }

    // localhost:8080/serviceProvider/update/name
    @PostMapping("/update/name")
    public ResponseEntity updateName(@RequestParam("name") String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serviceProviderService.updateName(id,name));
    }

    // localhost:8080/serviceProvider/update/college
    @PostMapping("/update/college")
    public ResponseEntity updateCollege(@RequestParam("college_id") Long collegeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getServiceProvider().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serviceProviderService.updateCollege(id,collegeId));
    }

}
