package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.plecicki.taskmanager.domain.entities.Task;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAll();
    Task save(Task task);
    Task findByTaskId(Long taskId);
}
