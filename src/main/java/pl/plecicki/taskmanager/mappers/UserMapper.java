package pl.plecicki.taskmanager.mappers;

import org.springframework.stereotype.Service;
import pl.plecicki.taskmanager.domain.dtos.UserDto;
import pl.plecicki.taskmanager.domain.entities.User;

@Service
public class UserMapper {

    public User userDtoToUser(UserDto userDto) {
        return new User(
                0L,
                userDto.getName(),
                userDto.getSurname(),
                userDto.getUsername(),
                userDto.getPassword()
        );
    }
}
