package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.exceptions.InvalidIdException;
import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.repository.CollegeRepository;
import CollegeHelp.College_Mantra.request.CollegeCreateRequest;
import CollegeHelp.College_Mantra.response.CollegeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CollegeService {

    private static Logger LOGGER = LoggerFactory.getLogger(CollegeService.class);

    @Autowired
    private CollegeRepository collegeRepository;

    public CollegeResponse addCollege(CollegeCreateRequest collegeCreateRequest) {

        College college = collegeCreateRequest.to();

        LOGGER.info("Name : {}, City : ",collegeCreateRequest.getName(),collegeCreateRequest.getCity());

        college = collegeRepository.save(college);

        return college.to();

    }

    public College findById(long id) throws InvalidIdException {

        Optional<College> optionalCollege = collegeRepository.findById(id);

        if(optionalCollege.isEmpty()){
            throw new InvalidIdException("Given College Id Is Invalid");
        }

        return optionalCollege.get();

    }
}
