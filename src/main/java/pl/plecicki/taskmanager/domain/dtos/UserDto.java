package pl.plecicki.taskmanager.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private String name;
    private String surname;
    private String username;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }
}
