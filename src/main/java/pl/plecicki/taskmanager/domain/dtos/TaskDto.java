package pl.plecicki.taskmanager.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
public class TaskDto {

    private String title;
    private String description;
    private TaskStatus taskStatus;
    private LocalDate deadline;
}
