package ru.vovchinnikov.tasklistapp.util.errors.category;

import ru.vovchinnikov.tasklistapp.util.enums.CategoryExceptions;
import ru.vovchinnikov.tasklistapp.util.errors.TaskListErrorWithDetail;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
public class CategoryNotCreatedError extends TaskListErrorWithDetail {

    public CategoryNotCreatedError(List<String> detail) {
        super(CategoryExceptions.CATEGORY_NOT_CREATED, detail);
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}
