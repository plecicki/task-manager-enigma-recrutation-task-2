package pl.plecicki.taskmanager.domain.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TASKS")
public class Task {

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true, name = "TASK_ID")
    private Long taskId;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private TaskStatus taskStatus;

    @Column
    private LocalDate deadline;

    @OneToMany(mappedBy = "task")
    private List<JoinUserTask> joinUserTasks;
}
