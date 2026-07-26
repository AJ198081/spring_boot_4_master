package dev.aj.bank_user.model.dtos;

import dev.aj.commons.types.Email;
import dev.aj.commons.types.Password;

public record CreateUser(Email email, Password password) {

}
