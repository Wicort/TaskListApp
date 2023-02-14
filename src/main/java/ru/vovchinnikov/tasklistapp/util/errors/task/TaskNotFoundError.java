package ru.vovchinnikov.tasklistapp.util.errors.task;

import ru.vovchinnikov.tasklistapp.util.enums.TaskItemExceptions;
import ru.vovchinnikov.tasklistapp.util.errors.TaskListError;

/**
 * @author Ovchinnikov Viktor
 */
public class TaskNotFoundError extends TaskListError {

    public TaskNotFoundError() {
        super(TaskItemExceptions.TASK_NOT_FOUND);
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
