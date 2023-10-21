package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.plecicki.taskmanager.domain.entities.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
