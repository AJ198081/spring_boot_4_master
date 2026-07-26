package dev.aj.bank_user.services;


import dev.aj.bank_user.model.dtos.CreateUser;
import dev.aj.bank_user.model.dtos.UserCreated;

public interface Auth0UserService {


    UserCreated createNewUser(CreateUser createUserRequest);
}
