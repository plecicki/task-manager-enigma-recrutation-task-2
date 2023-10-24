package pl.plecicki.taskmanager.domain.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    public Task(Long taskId, String title, String description, TaskStatus taskStatus, LocalDate deadline) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.deadline = deadline;
    }

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
    @JsonBackReference
    private List<JoinUserTask> joinUserTasks;
}
