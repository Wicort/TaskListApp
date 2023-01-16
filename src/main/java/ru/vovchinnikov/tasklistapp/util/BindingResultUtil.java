package ru.vovchinnikov.tasklistapp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovchinnikov Viktor
 */

public class BindingResultUtil {
    public static List<String> getBindingResultErrorsList(BindingResult result) {
        List<String> details = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                StringBuilder text = new StringBuilder();
                text.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
                details.add(text.toString());
            }

        }
        return details;
    }
}
