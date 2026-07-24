package dev.aj.bank_user.model.dtos;

import dev.aj.bank_commons.types.Email;
import dev.aj.bank_user.model.types.Password;

public record CreateUser(Email email, Password password) {

}
