package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;

public interface JoinUserTaskRepository extends CrudRepository<JoinUserTask, Long> {
}
