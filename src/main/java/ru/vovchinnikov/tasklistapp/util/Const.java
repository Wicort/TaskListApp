package ru.vovchinnikov.tasklistapp.util;

/**
 * @author Ovchinnikov Viktor
 */

public class Const {
    public enum ServerExceptions implements TaskListException{
        SERVER_EXCEPTION("Ошибка в работе сервиса или сервис недоступен. Повторите попытку позже.", -1);

        private final String message;
        private final int code;

        ServerExceptions(String message, int code) {
            this.message = message;
            this.code = code;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        @Override
        public int getCode() {
            return this.code;
        }
    }

    public enum UserExceptions implements TaskListException {
        USER_NOT_FOUND("Пользователь не найден", -101),
        USER_NOT_CREATED("Не удалось создать пользователя", -102),
        USER_ALEREADY_EXISTS("Пользователь с таким именем уже зарегистрирован", -103),
        USER_EMAIL_ALEREADY_EXISTS("Указанный email уже использован другим пользователем при регистрации", -104);

        private final String message;
        private final int code;

        UserExceptions(String message, int code) {
            this.message = message;
            this.code = code;
        }

        @Override
        public String getMessage(){
            return this.message;
        }

        @Override
        public int getCode(){
            return this.code;
        }
    }
}
