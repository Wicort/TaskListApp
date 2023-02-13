package ru.vovchinnikov.tasklistapp.util.errors;

import ru.vovchinnikov.tasklistapp.util.enums.CategoryExceptions;

import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */
public class CategoryNotCreatedError extends TaskListErrorWithDetail{

    public CategoryNotCreatedError(List<String> detail) {
        super(CategoryExceptions.CATEGORY_NOT_CREATED, detail);
    }

    @Override
    public int getCode() {
        return getException().getCode();
    }
}
