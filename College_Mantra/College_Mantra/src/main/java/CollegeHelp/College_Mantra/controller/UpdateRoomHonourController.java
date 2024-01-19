package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.request.ReputationCreateRequest;
import CollegeHelp.College_Mantra.request.RoomCreateRequest;
import CollegeHelp.College_Mantra.service.RoomOwnerService;
import CollegeHelp.College_Mantra.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/roomOwner")
public class UpdateRoomHonourController {

    @Autowired
    private RoomOwnerService roomOwnerService;

    @Autowired
    private RoomService roomService;

    // localhost:8080/roomOwner/upload/photo
    @PostMapping("/upload/photo")
    public ResponseEntity uploadPhoto(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        byte[] image = roomOwnerService.uploadPhoto(multipartFile,id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(multipartFile.getContentType()))
                .body(image);

    }

    // localhost:8080/roomOwner/addRoom
    @PostMapping("/addRoom")
    public ResponseEntity addRoom(@ModelAttribute RoomCreateRequest roomCreateRequest,
                                  @RequestParam("images") @NotNull(message = "Please Upload a photo") @NotEmpty(message = "Please Upload a Photo") List<MultipartFile> multipartFileList) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.addRoom(roomCreateRequest,multipartFileList,id));

    }

    // localhost:8080/roomOwner/getSentRequest
    @GetMapping("/getSentRequest")
    public ResponseEntity getSentRequest(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.getSentRequest(id));

    }

    // localhost:8080/roomOwner/getRequest
    @GetMapping("/getRequest")
    public ResponseEntity getRequest(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.getRequest(id));

    }

    // localhost:8080/roomOwner/send/request
    @PostMapping("/send/request")
    public ResponseEntity sendRequest(@RequestParam("id") Long userId) throws InvalidIdException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.sendRequest(id,userId));

    }

    // localhost:8080/roomOwner/acceptRequest
    @GetMapping("/acceptRequest")
    public ResponseEntity acceptRequest(@RequestParam("id") Long userId,@RequestParam("roomId") Long roomId) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.acceptRequest(id,userId,roomId));

    }

    // localhost:8080/roomOwner/delete/sent/request
    @GetMapping("/delete/sent/request")
    public ResponseEntity deleteSentRequest(@RequestParam("id") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.deleteSentRequest(id,userId));
    }

    // localhost:8080/roomOwner/delete/get/request
    @GetMapping("/delete/get/request")
    public ResponseEntity deleteGetRequest(@RequestParam("id") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.deleteGetRequest(id,userId));
    }

    // localhost:8080/roomOwner/remove/student
    @GetMapping("/remove/student")
    public ResponseEntity removeStudent(@RequestParam("id") Long relationId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.removeStudent(id,relationId));
    }

    // localhost:8080/roomOwner/getRelations
    @GetMapping("/getRelations")
    public ResponseEntity getRelations(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(roomOwnerService.getRelations(id));
    }

    // localhost:8080/roomOwner/update/name
    @PostMapping("/update/name")
    public ResponseEntity updateName(@RequestParam("name") String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.updateName(id,name));
    }

    // localhost:8080/roomOwner/update/college
    @PostMapping("/update/college")
    public ResponseEntity updateCollege(@RequestParam("college_id") Long collegeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.updateCollege(id,collegeId));
    }

    // localhost:8080/roomOwner/get/students
    @GetMapping("/get/students")
    public ResponseEntity getStudents(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.getStudents(id));
    }

    // localhost:8080/roomOwner/get/posted/room
    @GetMapping("/get/posted/room")
    public ResponseEntity getPostedRoom(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.getPostedRoom(id));
    }

    // localhost:8080/roomOwner/update/room
    @PostMapping("/update/room")
    public ResponseEntity updateRoom(@RequestParam("room_id") Long roomId, @ModelAttribute @Valid RoomCreateRequest roomCreateRequest, @RequestParam("images") List<MultipartFile> images) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.updateRoom(id,roomId,roomCreateRequest,images));
    }

    // localhost:8080/roomOwner/give/reputation/student
    @PostMapping("/give/reputation/student")
    public ResponseEntity giveReputationToStudent(@RequestBody @Valid ReputationCreateRequest reputationCreateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getRoomOwner().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomOwnerService.giveReputationToStudent(id,reputationCreateRequest));
    }

}
