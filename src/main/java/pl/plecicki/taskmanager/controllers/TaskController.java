package pl.plecicki.taskmanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.plecicki.taskmanager.domain.dtos.CreateAssignedTaskDto;
import pl.plecicki.taskmanager.domain.dtos.EditTaskDto;
import pl.plecicki.taskmanager.domain.dtos.TaskDto;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;
import pl.plecicki.taskmanager.exceptions.*;
import pl.plecicki.taskmanager.services.TaskService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/task")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> findTasks(@RequestBody TaskDto taskDto) throws TaskDoesntExists {
        return ResponseEntity.ok(taskService.findTasks(taskDto));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/unassigned")
    public ResponseEntity<Task> addTaskWithoutAssignedUser(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.addTaskWithoutAssignedUser(taskDto));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/assigned")
    public ResponseEntity<Task> addTaskWithAssignedUsers(@RequestBody CreateAssignedTaskDto createAssignedTaskDto)
            throws UserDoesntExistAndUnassignedTaskWasCreated, AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem {
        TaskDto taskDto = createAssignedTaskDto.getTaskDto();
        List<Long> userIds = createAssignedTaskDto.getUserIds();
        return ResponseEntity.ok(taskService.addTaskWithAssignedUsers(taskDto, userIds));
    }

    @PutMapping(value = "/{taskId}/{userId}/assign")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId)
            throws TaskDoesntExists, UserDoesntExist, UserHasBeenAssignedBeforeToThisTask {
        return ResponseEntity.ok(taskService.assignUserToTask(taskId, userId));
    }

    @PutMapping(value = "/{taskId}/{userId}/unassign")
    public ResponseEntity<Task> unassignUserFromTask(@PathVariable Long taskId, @PathVariable Long userId)
            throws TaskDoesntExists, UserDoesntExist, UserIsntAssignToThisTask {
        return ResponseEntity.ok(taskService.unassignUserFromTask(taskId, userId));
    }

    @PutMapping
    public ResponseEntity<Task> editTask(@RequestBody EditTaskDto editTaskDto) throws TaskDoesntExists {
        return ResponseEntity.ok(taskService.editTask(editTaskDto));
    }

    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) throws TaskDoesntExists {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{taskId}/status")
    @ResponseBody
    public ResponseEntity<Void> changeStatus(@PathVariable Long taskId, @RequestParam TaskStatus status)
            throws TaskDoesntExists, ThisStatusHasBeenSetBefore {
        taskService.changeStatus(taskId, status);
        return ResponseEntity.ok().build();
    }
}
