package ru.vovchinnikov.tasklistapp.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vovchinnikov.tasklistapp.dto.UserDTO;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.UsersRepository;
import ru.vovchinnikov.tasklistapp.util.errors.user.UserEmailAlereadyExistsError;
import ru.vovchinnikov.tasklistapp.util.errors.user.UserNotFoundError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovchinnikov Viktor
 */

@Service
@Slf4j
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> findAll() {
        log.info("Getting all users");
        return usersRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO findById(String id){
        log.info("Start finding user by id {}", id);
        try {
            Optional<User> user = usersRepository.findById(UUID.fromString(id));

            if (user.isPresent()) {
                log.info("Found user for id {}: {}", id, user.get());
                return convertToDto(user.get());
            }
            log.error("Can not found user by id {}", id);
            throw new UserNotFoundError();
        } catch (IllegalArgumentException e) {
            log.error("userId {} is not valid UUID", id);
            throw new UserNotFoundError();
        }
    }

    public User findUserById(UUID id){
        log.info("Start finding user by UUID {}", id);
        Optional<User> user = usersRepository.findById(id);
        if (user.isEmpty()){
            log.error("Can not found user by UUID {}", id);
            throw new UserNotFoundError();
        }
        log.info("Found user by id {}: {}", id, user.get());
        return user.get();
    }

    @Transactional(readOnly = false)
    public void createUser(UserDTO userDTO) {
        if (usersRepository.findUserByEmail(userDTO.getEmail()).isPresent()){
            log.error("Error for creating new user: user with email {} alrady exists", userDTO.getEmail());
            throw new UserEmailAlereadyExistsError();
        }
        log.info("creating user {}", userDTO);
        usersRepository.save(convertToUser(userDTO));
    }

    private UserDTO convertToDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO){
        if (userDTO.getId() != null) {
            Optional<User> user = usersRepository.findById(userDTO.getId());

            if (user.isPresent()) {
                return user.get();
            }
        }

        return enrichUser(modelMapper.map(userDTO, User.class));
    }

    private User enrichUser(User user){
        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setEmailConfirmed(false);
        return user;
    }

    public User findUserByStringId(String strId){
        log.info("Start finding user by id {}", strId);
        User user;
        try {
            user = findUserById(UUID.fromString(strId));
            log.info("Found user by id {}: {}", strId, user);
        } catch (IllegalArgumentException e) {
            log.error("userId {} is not valid UUID", strId);
            throw new UserNotFoundError();
        }
        return user;
    }

}
