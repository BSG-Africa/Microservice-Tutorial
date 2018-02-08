package za.co.bsg.feign;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class UserServiceStub implements UserService {

    public UserDTO getLoggedInUserByUsername(String username) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("stubbed-login");
        return userDTO;
    }
}
