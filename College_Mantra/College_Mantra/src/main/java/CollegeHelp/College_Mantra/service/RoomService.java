package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.Room;
import CollegeHelp.College_Mantra.model.RoomOwner;
import CollegeHelp.College_Mantra.repository.CollegeRepository;
import CollegeHelp.College_Mantra.repository.ProfileImageRepository;
import CollegeHelp.College_Mantra.repository.RoomHonourRepository;
import CollegeHelp.College_Mantra.repository.RoomRepository;
import CollegeHelp.College_Mantra.request.RoomCreateRequest;
import CollegeHelp.College_Mantra.response.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private static final String FOLDER_PATH = "D:\\Java-Backend-Projects\\CollegeHelp\\College_Mantra\\College_Mantra\\src\\main\\resources\\profilePhotos\\";

    @Autowired
    private RoomHonourRepository roomHonourRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private RoomRepository roomRepository;


    public String addRoom(RoomCreateRequest roomCreateRequest, List<MultipartFile> multipartFileList, Long id) throws IOException {

        Optional<RoomOwner> optionalRoomHonour = roomHonourRepository.findById(id);

        Room room = roomCreateRequest.to();
        room.setRoomOwner(optionalRoomHonour.get());
        room.setCollege(optionalRoomHonour.get().getCollege());

        List<ProfileImage> images = uploadImages(multipartFileList,room);
        room = roomRepository.save(room);
        profileImageRepository.saveAll(images);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/room/")
                .path(String.valueOf(room.getId()))
                .toUriString();

    }

    public static List<ProfileImage> uploadImages(List<MultipartFile> multipartFileList,Room room) {

        return (List<ProfileImage>) multipartFileList.stream()
                .map(x -> {
                    try {
                        x.transferTo(new File(FOLDER_PATH + x.getOriginalFilename()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return ProfileImage.builder()
                            .room(room)
                            .type(x.getContentType())
                            .name(x.getOriginalFilename())
                            .filePath(FOLDER_PATH + x.getOriginalFilename())
                            .build();
                }).collect(Collectors.toList());

    }

    public RoomResponse findById(Long id) throws IOException {

        return roomRepository.findById(id).get().to();

    }
}

//    String imagePath = folderPath + multipartFile.getOriginalFilename();
//
//    Student student = studentRepository.findById(id).get();
//
//    ProfileImage profileImage = ProfileImage.builder()
//            .type(multipartFile.getContentType())
//            .student(student)
//            .filePath(imagePath)
//            .name(multipartFile.getOriginalFilename())
//            .build();
//
//                profileImage = profileImageRepository.save(profileImage);
//
//                student.setProfileImage(profileImage);
//
//                studentRepository.save(student);
//
//                LOGGER.info("120");
//
//                multipartFile.transferTo(new File(profileImage.getFilePath()));
