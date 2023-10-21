package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;

@Repository
public interface JoinUserTaskRepository extends CrudRepository<JoinUserTask, Long> {
}
