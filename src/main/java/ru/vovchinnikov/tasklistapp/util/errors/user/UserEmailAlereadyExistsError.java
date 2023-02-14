package ru.vovchinnikov.tasklistapp.util.errors.user;

import ru.vovchinnikov.tasklistapp.util.enums.UserExceptions;
import ru.vovchinnikov.tasklistapp.util.errors.TaskListError;

/**
 * @author Ovchinnikov Viktor
 */

public class UserEmailAlereadyExistsError extends TaskListError {

    public UserEmailAlereadyExistsError() {
        super(UserExceptions.USER_EMAIL_ALREADY_EXISTS);
    }

    @Override
    public String getMessage() {
        return getException().getMessage();
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}
