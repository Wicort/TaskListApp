package ru.vovchinnikov.tasklistapp.util.errors;


import ru.vovchinnikov.tasklistapp.util.enums.ServerExceptions;

/**
 * @author Ovchinnikov Viktor
 */
public class ServerError extends TaskListError{
    public ServerError() {
        super(ServerExceptions.SERVER_EXCEPTION);
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
