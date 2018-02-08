package za.co.bsg.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.bsg.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "uaa")
public interface UserServiceClient {

    @GetMapping(value = "/api/users/{login}")
    UserDTO getLoggedInUserByUsername(@PathVariable("login") String username);

}
