package pl.plecicki.taskmanager.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CreateAssignedTaskDto {

    private TaskDto taskDto;
    private List<Long> userIds;
}
