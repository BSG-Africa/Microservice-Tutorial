package za.co.bsg.feign;

public interface UserService {
    UserDTO getLoggedInUserByUsername(String username);
}
