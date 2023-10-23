package pl.plecicki.taskmanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.plecicki.taskmanager.domain.dtos.EditTaskDto;
import pl.plecicki.taskmanager.domain.dtos.TaskDto;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;
import pl.plecicki.taskmanager.exceptions.*;
import pl.plecicki.taskmanager.mappers.TaskMapper;
import pl.plecicki.taskmanager.repositories.JoinUserTaskRepository;
import pl.plecicki.taskmanager.repositories.TaskRepository;
import pl.plecicki.taskmanager.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JoinUserTaskRepository joinUserTaskRepository;
    private final TaskMapper taskMapper;

    public List<Task> findTasks(TaskDto taskDto)
            throws TaskDoesntExists {
        String title = taskDto.getTitle();
        String description = taskDto.getDescription();
        TaskStatus taskStatus = taskDto.getTaskStatus();
        LocalDate deadline = taskDto.getDeadline();
        List<Task> tasks = taskRepository.findAll();
        if (title != null && !"".equals(title)) {
            tasks = tasks.stream()
                    .filter(task -> task.getTitle().equals(title))
                    .toList();
        }
        if (description != null && !"".equals(description)) {
            tasks = tasks.stream()
                    .filter(task -> task.getDescription().equals(description))
                    .toList();
        }
        if (taskStatus != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getTaskStatus() == taskStatus)
                    .toList();
        }
        if (deadline != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getDeadline().equals(deadline))
                    .toList();
        }
        if (tasks.isEmpty()) throw new TaskDoesntExists();
        return tasks;
    }

    public Task addTaskWithoutAssignedUser(TaskDto taskDto) {
        return taskRepository.save(taskMapper.mapTaskDtoToTask(taskDto));
    }

    public Task addTaskWithAssignedUsers(TaskDto taskDto, List<Long> userIds)
            throws UserDoesntExistAndUnassignedTaskWasCreated, AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem {
        Task savedTask =  taskRepository.save(taskMapper.mapTaskDtoToTask(taskDto));
        boolean isAssignedAtLeastOneUser = false;
        boolean isAnyNotExistingUser = false;
        for (Long userId : userIds) {
            if (userRepository.existsById(userId)) {
                User assignedUser = userRepository.findByUserId(userId);
                joinUserTaskRepository.save(new JoinUserTask(0L,assignedUser,savedTask));
                isAssignedAtLeastOneUser = true;
            } else {
                isAnyNotExistingUser = true;
            }
        }
        if (isAssignedAtLeastOneUser && !isAnyNotExistingUser) {
            return savedTask;
        } else if (!isAssignedAtLeastOneUser) {
            throw new UserDoesntExistAndUnassignedTaskWasCreated();
        }
        throw new AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem();
    }

    public Task assignUserToTask(Long taskId, Long userId)
            throws UserDoesntExist, TaskDoesntExists, UserHasBeenAssignedBeforeToThisTask {
        if (!userRepository.existsById(userId)) throw new UserDoesntExist();
        if (!taskRepository.existsById(taskId)) throw new TaskDoesntExists();
        User assignedUser = userRepository.findByUserId(userId);
        Task assignedTask = taskRepository.findByTaskId(taskId);
        if (joinUserTaskRepository.existsByUserAndTask(assignedUser, assignedTask))
            throw new UserHasBeenAssignedBeforeToThisTask();
        joinUserTaskRepository.save(new JoinUserTask(0L, assignedUser, assignedTask));
        assignedTask.setJoinUserTasks(null);
        return assignedTask;
    }

    @Transactional
    public Task unassignUserFromTask(Long taskId, Long userId)
            throws UserDoesntExist, TaskDoesntExists, UserIsntAssignToThisTask {
        if (!userRepository.existsById(userId)) throw new UserDoesntExist();
        if (!taskRepository.existsById(taskId)) throw new TaskDoesntExists();
        User assignedUser = userRepository.findByUserId(userId);
        Task assignedTask = taskRepository.findByTaskId(taskId);
        if (!joinUserTaskRepository.existsByUserAndTask(assignedUser, assignedTask))
            throw new UserIsntAssignToThisTask();
        joinUserTaskRepository.deleteByTaskAndUser(assignedTask, assignedUser);
        return assignedTask;
    }

    public Task editTask(EditTaskDto editTaskDto) throws TaskDoesntExists {
        if (!taskRepository.existsById(editTaskDto.getTaskId())) throw new TaskDoesntExists();
        return taskRepository.save(taskMapper.mapEditTaskDtoToTask(editTaskDto));
    }

    public void deleteTask(Long taskId) throws TaskDoesntExists {
        if (!taskRepository.existsById(taskId)) throw new TaskDoesntExists();
        Task task = taskRepository.findByTaskId(taskId);
        List<JoinUserTask> joinUserTasks = joinUserTaskRepository.findByTask(task);
        joinUserTaskRepository.deleteAll(joinUserTasks);
        taskRepository.delete(task);
    }

    public void changeStatus(Long taskId, TaskStatus taskStatus)
            throws TaskDoesntExists, ThisStatusHasBeenSetBefore {
        if (!taskRepository.existsById(taskId)) throw new TaskDoesntExists();
        Task task = taskRepository.findByTaskId(taskId);
        if (task.getTaskStatus() == taskStatus) throw new ThisStatusHasBeenSetBefore();
        task.setTaskStatus(taskStatus);
        taskRepository.save(task);
    }
}
