package pl.plecicki.taskmanager.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Initializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException {
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(new User(0L,"Init_name", "Init_surname", "user",
                    "$2a$12$pRqgKhtX9rxlULLRKuKehO4EYZfUgcb4v7wk.QAXNOSxfPmSMC16W"));
        }
    }
}
