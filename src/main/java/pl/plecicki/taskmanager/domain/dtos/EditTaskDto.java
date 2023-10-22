package pl.plecicki.taskmanager.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class EditTaskDto {

    private Long taskId;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private LocalDate deadline;
}
