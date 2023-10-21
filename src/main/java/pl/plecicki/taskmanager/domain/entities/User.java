package pl.plecicki.taskmanager.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USERS")
public class User {

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
