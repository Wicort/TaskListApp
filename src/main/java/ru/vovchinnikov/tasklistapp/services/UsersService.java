package ru.vovchinnikov.tasklistapp.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vovchinnikov.tasklistapp.dto.UserDTO;
import ru.vovchinnikov.tasklistapp.models.User;
import ru.vovchinnikov.tasklistapp.repositories.UsersRepository;
import ru.vovchinnikov.tasklistapp.util.errors.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovchinnikov Viktor
 */

@Service
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
            throw new UserNotFoundError();
        }
    }

    public User findUserById(UUID id){
        Optional<User> user = usersRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundError();
        }
        return user.get();
    }

    public UserDTO findByName(String username){
        Optional<User> user = usersRepository.findUserByUsername(username);

        if (user.isPresent()){
            return convertToDto(user.get());
        }
        throw new UserNotFoundError();
    }

    @Transactional(readOnly = false)
    public void createUser(UserDTO userDTO) {
        if (usersRepository.findUserByEmail(userDTO.getEmail()).isPresent()){
            throw new UserEmailAlereadyExistsError();
        }
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

    public User findUserByStringId(String srtId){
        User user;
        try {
            user = findUserById(UUID.fromString(srtId));
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundError();
        }
        return user;
    }

}
