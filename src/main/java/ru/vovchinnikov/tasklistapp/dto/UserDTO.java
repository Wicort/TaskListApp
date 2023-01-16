package ru.vovchinnikov.tasklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message="Имя пользователя не может быть пустым")
    @Size(min=3, max=20, message = "Длина имени пользователя должна быть в диапазоне от 3 до 20 символов")
    private String username;

    @NotEmpty(message="Не заполнен пароль")
    @Size(min=8, message = "Длина пароля должна быть не меньше 8 символов")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}$",
            message="Пароль должен быть не менее 8 символов в длину и содержать как минимум 1 заглавную букву, 1 прописную букву, 1 цифру и один спецсимвол")
    private String password;

    @Email(message = "Email не соответствует формату адреса электронной почты")
    private String email;
}
