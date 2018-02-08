package rmb.foundery.replica.integration.identity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rmb.foundery.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name = "uaa")
public interface UserServiceClient {

    @GetMapping(value = "/api/users/{login}")
    UserDTO getLoggedInUserByUsername(@PathVariable("login") String username);

}
