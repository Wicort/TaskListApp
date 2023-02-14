package ru.vovchinnikov.tasklistapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vovchinnikov.tasklistapp.models.Category;
import ru.vovchinnikov.tasklistapp.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Repository
public interface CategoriesRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findOneByOwnerAndId(User owner, UUID id);

    List<Category> findAllByOwner(User user);

    Optional<Category> findOneByOwnerAndName(User owner, String name);

    Optional<Category> findOneById(UUID id);
}
