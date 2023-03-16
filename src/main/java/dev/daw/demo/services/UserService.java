package dev.daw.demo.services;

import dev.daw.demo.models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    StackExchangeService stackExchangeService;
    @Cacheable(cacheNames = {"usersCache"}, key = "#userId")
    public UserDTO getUser(int userId) throws IOException {
        UserDTO user = stackExchangeService.getUser(userId);
        return user;
    }
}
