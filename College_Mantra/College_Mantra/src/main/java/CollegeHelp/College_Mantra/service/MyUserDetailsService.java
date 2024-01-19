package CollegeHelp.College_Mantra.service;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.model.User;
import CollegeHelp.College_Mantra.repository.MyUserRepository;
import CollegeHelp.College_Mantra.request.UserCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return myUserRepository.findByUsername(username);
    }

    public User create(UserCreateRequest userCreateRequest){

        LOGGER.info("username is : {}",userCreateRequest.getUsername());

        User.UserBuilder myUserBuilder = User.builder()
                .username(userCreateRequest.getUsername())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()));

        if(userCreateRequest.getStudent() != null){
            myUserBuilder.authority(Authority.STUDENT);
        }
        else if(userCreateRequest.getServiceProvider() != null){
            myUserBuilder.authority(Authority.SERVICE_PROVIDER);
        }
        else if(userCreateRequest.getMess() != null){
            myUserBuilder.authority(Authority.MESS);
        }
        else{
            myUserBuilder.authority(Authority.ROOM_HONOUR);
        }
        return myUserRepository.save(myUserBuilder.build());
    }

}
