package CollegeHelp.College_Mantra.controller;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.request.CollegeCreateRequest;
import CollegeHelp.College_Mantra.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    // localhost:8080/college/add
    @PostMapping("/add")
    public ResponseEntity addCollege(@RequestBody @Valid CollegeCreateRequest collegeCreateRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collegeService.addCollege(collegeCreateRequest));
    }

}
