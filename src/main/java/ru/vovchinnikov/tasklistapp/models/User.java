package ru.vovchinnikov.tasklistapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="username")
    @NotEmpty(message="Имя пользователя не может быть пустым")
    @Size(min=3, max=20, message = "Длина имени пользователя должна быть в диапазоне от 3 до 20 символов")
    private String username;

    @Column(name="password")
    @NotEmpty(message="Не заполнен пароль")
    @Size(min=8, message = "Длина пароля должна быть не меньше 8 символов")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*]).{8,}$",
            message="Пароль должен быть не менее 8 символов в длину и содержать как минимум 1 заглавную букву, 1 прописную букву, 1 цифру и один спецсимвол")
    private String password;

    @Column(name="email")
    @Email(message = "Email не соответствует формату адреса электронной почты")
    private String email;

    @Column(name="email_confirmed")
    private boolean emailConfirmed;

    @Column(name="created_at")
    private LocalDateTime createdAt;

}
