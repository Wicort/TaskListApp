package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.vovchinnikov.tasklistapp.dto.UserDTO;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.UsersRepository;
import ru.vovchinnikov.tasklistapp.util.exceptions.ServerError;
import ru.vovchinnikov.tasklistapp.util.exceptions.TaskListError;
import ru.vovchinnikov.tasklistapp.util.exceptions.UserNotFoundError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovchinnikov Viktor
 */

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO findByUUID(String id){
        try {
            Optional<User> user = usersRepository.findById(UUID.fromString(id));

            if (user.isPresent()) {
                return convertToDto(user.get());
            }
            throw new UserNotFoundError();
        } catch (IllegalArgumentException e) {
            throw new ServerError();
        }
    }

    public UserDTO findByName(String username){
        Optional<User> user = usersRepository.findUserByUsername(username);

        if (user.isPresent()){
            return convertToDto(user.get());
        }
        throw new UserNotFoundError();
    }

    public void createUser(UserDTO userDTO) {
        // todo проверка на существование пользователя
        usersRepository.save(convertAndEnrichUser(userDTO));
    }


    private UserDTO convertToDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private User convertAndEnrichUser(UserDTO userDTO){
        User user = convertToUser(userDTO);
        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setEmailConfirmed(false);
        return user;
    }
}
