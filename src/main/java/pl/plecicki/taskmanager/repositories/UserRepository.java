package pl.plecicki.taskmanager.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.plecicki.taskmanager.domain.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
