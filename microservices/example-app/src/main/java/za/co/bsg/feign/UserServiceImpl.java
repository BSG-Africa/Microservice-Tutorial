package za.co.bsg.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class UserServiceImpl implements UserService {

    private UserServiceClient userServiceClient;

    @Autowired
    public UserServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public UserDTO getLoggedInUserByUsername(String username) {
        return userServiceClient.getLoggedInUserByUsername(username);
    }
}
