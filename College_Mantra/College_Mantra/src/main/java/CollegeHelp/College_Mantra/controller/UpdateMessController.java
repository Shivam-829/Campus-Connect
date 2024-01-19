package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.request.MessProductCreateRequest;
import CollegeHelp.College_Mantra.service.MessService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/mess")
public class UpdateMessController {

    @Autowired
    private MessService messService;

    // localhost:8080/mess/upload/photo

    @PostMapping("/upload/photo")
    public ResponseEntity uploadPhoto(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        byte[] image = messService.uploadPhoto(multipartFile,id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(multipartFile.getContentType()))
                .body(image);

    }

    // localhost:8080/mess/update/address
    @PostMapping("/update/address")
    public ResponseEntity updateAddress(@RequestParam("address")
                                        @NotNull(message = "Please Give Some Value")
                                        @NotBlank(message = "Please Give Some Value")
                                        String address){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.updateAddresss(id,address));
    }

    // localhost:8080/mess/add/payment/method
    @PostMapping("/add/payment/method")
    public void addPaymentMethod(@RequestParam("keyId")
                                           @NotNull(message = "Please Give Some Value")
                                           @NotBlank(message = "Please Give Some Value")
                                           String keyId,
                                           @RequestParam("keySecret")
                                           @NotNull(message = "Please Give Some Value")
                                           @NotBlank(message = "Please Give Some Value")
                                           String keySecret){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        messService.addPaymentMethod(id,keyId,keySecret);

    }

    // localhost:8080/mess/addProduct
    @PostMapping("/addProduct")
    public ResponseEntity addProduct(@RequestBody @Valid MessProductCreateRequest messProductCreateRequest) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.addProduct(id,messProductCreateRequest));

    }

    // localhost:8080/mess/getProducts
    @GetMapping("/getProducts")
    public ResponseEntity getProducts(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.getProducts(id));

    }

    // localhost:8080/mess/getMessTransactions
    @GetMapping("/getMessTransactions")
    public ResponseEntity getMessTransactions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.getMessTransactions(id));
    }

    // localhost:8080/mess/getCarts
    @GetMapping("/getCarts")
    public ResponseEntity getCarts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.getCarts(id));
    }

    // localhost:8080/mess/update/name
    @PostMapping("/update/name")
    public ResponseEntity updateName(@RequestParam("name") String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(messService.updateName(id,name));
    }

    // localhost:8080/mess/update/college
    @PostMapping("/update/college")
    public ResponseEntity updateCollege(@RequestParam("college_id") Long collegeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messService.updateCollege(id,collegeId));
    }

    // localhost:8080/mess/update/product
    @PostMapping("/update/product")
    public ResponseEntity updateProduct(@RequestParam("product_id") Long productId,@ModelAttribute @Valid MessProductCreateRequest messProductCreateRequest) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUser = (User) authentication.getPrincipal();

        Long id = myUser.getMess().getId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messService.updateProduct(id,productId,messProductCreateRequest));
    }

}
