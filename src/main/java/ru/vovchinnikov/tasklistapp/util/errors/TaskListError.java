package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

/**
 * @author Ovchinnikov Viktor
 */

public abstract class TaskListError extends RuntimeException{
    private final TaskListException exception;

    public TaskListError(TaskListException exception) {
        super();
        this.exception = exception;
    }

    public abstract String getMessage();

    public abstract int getCode();

    public TaskListException getException(){
        return this.exception;
    }
}
