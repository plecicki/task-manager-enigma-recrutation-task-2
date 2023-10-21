package pl.plecicki.taskmanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.plecicki.taskmanager.domain.dtos.UserDto;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.exceptions.UserDoesntExist;
import pl.plecicki.taskmanager.mappers.UserMapper;
import pl.plecicki.taskmanager.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> findUsers(String name, String surname) throws UserDoesntExist {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> name == null || user.getName().equals(name))
                .filter(user -> surname == null || user.getSurname().equals(surname))
                .collect(Collectors.toList());
        if (users.isEmpty()) throw new UserDoesntExist();
        return users;
    }

    public User addUser(UserDto userDto) {
        return userRepository.save(userMapper.userDtoToUser(userDto));
    }

    public void deleteUser(Long userId) throws UserDoesntExist {
        if (!userRepository.existsById(userId)) throw new UserDoesntExist();
        userRepository.deleteById(userId);
    }
}
