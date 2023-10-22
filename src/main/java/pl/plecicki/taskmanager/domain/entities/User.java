package pl.plecicki.taskmanager.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "USERS")
public class User {

    public User(Long userId, String name, String surname, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, name = "USER_ID")
    private Long userId;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "user")
    private List<JoinUserTask> joinUserTasks;
}
