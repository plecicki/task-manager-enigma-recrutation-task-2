package pl.plecicki.taskmanager.mappers;

import org.springframework.stereotype.Service;
import pl.plecicki.taskmanager.domain.dtos.EditTaskDto;
import pl.plecicki.taskmanager.domain.dtos.TaskDto;
import pl.plecicki.taskmanager.domain.entities.Task;

@Service
public class TaskMapper {

    public Task mapTaskDtoToTask(TaskDto taskDto) {
        return new Task(
                0L,
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getTaskStatus(),
                taskDto.getDeadline()
        );
    }

    public Task mapEditTaskDtoToTask(EditTaskDto editTaskDto) {
        return new Task(
                editTaskDto.getTaskId(),
                editTaskDto.getTitle(),
                editTaskDto.getDescription(),
                editTaskDto.getTaskStatus(),
                editTaskDto.getDeadline()
        );
    }
}
