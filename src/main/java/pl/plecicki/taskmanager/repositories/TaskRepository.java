package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.plecicki.taskmanager.domain.entities.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}
