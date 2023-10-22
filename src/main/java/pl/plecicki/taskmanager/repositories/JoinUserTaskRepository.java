package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.entities.User;

import java.util.List;

@Repository
public interface JoinUserTaskRepository extends CrudRepository<JoinUserTask, Long> {

    List<JoinUserTask> findByUser(User user);
    List<JoinUserTask> findByTask(Task task);
    void deleteByTask(Task task);
    boolean existsByUserAndTask(User user, Task task);
    Task deleteByTaskAndUser(Task task, User user);
}
