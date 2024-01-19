package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.enums.RoomType;
import CollegeHelp.College_Mantra.enums.ServiceType;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.service.*;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DownloadImage downloadImage;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    // localhost:8080/user/id
    @GetMapping("/id")
    public ResponseEntity getUserById(@RequestParam("id") Long id) throws IOException {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(userService.findById(id));
    }

    // localhost:8080/user/profile/
    @GetMapping("/profile/{name}")
    public ResponseEntity getProfileImage(@PathVariable("name") String name) throws IOException {
        byte[] image = downloadImage.getImageByName(name);
        return ResponseEntity.status(HttpStatus.FOUND)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    // localhost:8080/user/room/
    @GetMapping("/room/{id}")
    public ResponseEntity getRoomById(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(roomService.findById(id));
    }

    // localhost:8080/user/change/password
    @PostMapping("/change/password")
    public boolean changePassword(@RequestParam("currentPassword") String currPassword,
                                  @RequestParam("newPassword") String newPassword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        return userService.changePassword(myUser,currPassword,newPassword);
    }

    // localhost:8080/user/get/student/list
    @GetMapping("/get/student/list")
    public ResponseEntity getStudentList(@RequestParam(value = "name",required = false) String name,
                                      @RequestParam(value = "college_id",required = false) Long collegeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserList(name,collegeId));
    }

    // localhost:8080/user/get/mess/list
    @GetMapping("/get/mess/list")
    public ResponseEntity getMessList(@RequestParam(value = "name",required = false) String name,
                                      @RequestParam(value = "college_id",required = false) Long collegeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getMessList(name,collegeId));
    }

    // localhost:8080/user/get/serviceProvider/list
    @GetMapping("/get/serviceProvider/list")
    public ResponseEntity getServiceProviderList(@RequestParam(value = "name",required = false) String name,
                                      @RequestParam(value = "college_id",required = false) Long collegeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getServicerProviderList(name,collegeId));
    }

    // localhost:8080/user/get/roomOwner/list
    @GetMapping("/get/roomOwner/list")
    public ResponseEntity getRoomOwnerList(@RequestParam(value = "name",required = false) String name,
                                                 @RequestParam(value = "college_id",required = false) Long collegeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getRoomOwnerList(name,collegeId));
    }

    // localhost:8080/user/get/college/list
    @GetMapping("get/college/list")
    public ResponseEntity getCollegeList(@RequestParam(value = "name",required = false) String name,
                                         @RequestParam(value = "city",required = false) City city){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCollegeList(name,city));
    }

    // localhost:8080/user/get/vacant/room/list
    @GetMapping("/get/vacant/room/list")
    public ResponseEntity getRooms(@RequestParam(value = "college_id",required = false) Long collegeId,
                                   @RequestParam(value = "room_type",required = false) RoomType roomType,
                                   @RequestParam(value = "range",required = false) Integer range){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getRooms(collegeId,roomType,range));
    }

    // localhost:8080/user/get/vacant/room/byOwner
    @GetMapping("/get/vacant/room/byOwner")
    public ResponseEntity getRooms(@RequestParam("user_id") Long userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getRooms(userId));
    }

    // localhost:8080/user/get/mess/product/list
    @GetMapping("/get/mess/product/list")
    public ResponseEntity getProducts(@RequestParam("user_id") Long userId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getProducts(userId));
    }

    // localhost:8080/user/get/college/byId
    @GetMapping("/get/college/byId")
    public ResponseEntity getCollegeById(@RequestParam("college_id") Long collegeId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCollegeById(collegeId));
    }

    // localhost:8080/user/get/messProduct/byId
    @GetMapping("/get/messProduct/byId")
    public ResponseEntity getMessProductById(@RequestParam("product_id") Long productId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getMessProductById(productId));
    }

    // localhost:8080/user/get/messProductCart/byId
    @GetMapping("/get/messProductCart/byId")
    public ResponseEntity getMessProductCartById(@RequestParam("cart_id") Long cartId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getMessProductCartById(cartId));
    }

    // localhost:8080/user/get/relation/byId
    @GetMapping("/get/relation/byId")
    public ResponseEntity getRelationById(@RequestParam("relation_id") Long relationId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getRelationById(relationId));
    }

    // localhost:8080/user/get/roomService/byId
    @GetMapping("/get/roomService/byId")
    public ResponseEntity getRoomServiceById(@RequestParam("service_id") Long serviceId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getRoomServiceById(serviceId));
    }

    // localhost:8080/user/get/serviceRequest/byId
    @GetMapping("/get/serviceRequest/byId")
    public ResponseEntity getServiceRequestById(@RequestParam("service_request_id") Long serviceRequestId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getServiceRequestById(serviceRequestId));
    }

    // localhost:8080/user/get/serviceTransaction/byId
    @GetMapping("/get/serviceTransaction/byId")
    public ResponseEntity getServiceTransactionById(@RequestParam("service_transaction_id") Long serviceTransactionId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getServiceTransactionById(serviceTransactionId));
    }

    // localhost:8080/user/get/transaction/byId
    @GetMapping("/get/transaction/byId")
    public ResponseEntity getTransactionById(@RequestParam("transaction_id") Long transactionId) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getTransactionbyId(transactionId));
    }

    // localhost:8080/user/get/service/list
    @GetMapping("/get/service/list")
    public ResponseEntity getServiceList(@RequestParam(value = "college_id",required = false) Long collegeId,
                                         @RequestParam(value = "city",required = false) City city,
                                         @RequestParam(value = "service_type",required = false) ServiceType serviceType){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getServiceList(collegeId,city,serviceType));
    }

}
