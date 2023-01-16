package ru.vovchinnikov.tasklistapp.util.exceptions;

import ru.vovchinnikov.tasklistapp.util.TaskListException;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */

public abstract class TaskListErrorWithDetail extends TaskListError {

    private final List<String> detail;

    public TaskListErrorWithDetail(TaskListException exc, List<String> detail) {
        super(exc);
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append(getException().getMessage());

        if (detail.size() > 0){
            msg.append(": ");
            detail.stream()
                    .forEach(str->msg.append(str).append("; "));
        }
        return msg.toString();
    }
}
