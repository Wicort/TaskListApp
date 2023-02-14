package ru.vovchinnikov.tasklistapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vovchinnikov.tasklistapp.models.User;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String name);

    Optional<User> findUserByEmail(String email);

}
