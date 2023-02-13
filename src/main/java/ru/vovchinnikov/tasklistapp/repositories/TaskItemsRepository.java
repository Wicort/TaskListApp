package ru.vovchinnikov.tasklistapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vovchinnikov.tasklistapp.models.TaskItem;
import ru.vovchinnikov.tasklistapp.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Ovchinnikov Viktor
 */
@Repository
public interface TaskItemsRepository extends JpaRepository<TaskItem, UUID> {
    List<TaskItem> findAllByOwner(User user);

    Optional<TaskItem> findOneByOwnerAndId(User owner, UUID id);
}
