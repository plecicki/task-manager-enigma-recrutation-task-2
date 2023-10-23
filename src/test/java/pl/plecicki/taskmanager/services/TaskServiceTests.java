package pl.plecicki.taskmanager.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.plecicki.taskmanager.domain.dtos.CreateAssignedTaskDto;
import pl.plecicki.taskmanager.domain.dtos.EditTaskDto;
import pl.plecicki.taskmanager.domain.dtos.TaskDto;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;
import pl.plecicki.taskmanager.exceptions.*;
import pl.plecicki.taskmanager.repositories.JoinUserTaskRepository;
import pl.plecicki.taskmanager.repositories.TaskRepository;
import pl.plecicki.taskmanager.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskServiceTests {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JoinUserTaskRepository joinUserTaskRepository;

    @Test
    public void findTasksTest() throws TaskDoesntExists {
        //Given
        String testTitleTask = "Napisz CV do ENIGMA - tests";

        Task task1 = new Task(0L, testTitleTask, "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        Task task2 = new Task(0L, testTitleTask, "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(2));
        Task task3 = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(1));

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);
        task3 = taskRepository.save(task3);

        TaskDto taskDto = new TaskDto(testTitleTask, null, null, null);

        //When
        List<Task> tasks = taskService.findTasks(taskDto);

        //Then
        assertEquals(2, tasks.size());
        assertEquals(task1.getTaskId(), tasks.get(0).getTaskId());
        assertEquals(task2.getTaskId(), tasks.get(1).getTaskId());

        //CleanUp
        taskRepository.delete(task1);
        taskRepository.delete(task2);
        taskRepository.delete(task3);
    }

    @Test
    public void addTaskWithoutAssignedUser() {
        //Given
        TaskDto taskDto = new TaskDto( "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));

        //When
        Task task = taskService.addTaskWithoutAssignedUser(taskDto);

        //Then
        assertEquals("Aaa", task.getTitle());
        assertEquals(TaskStatus.TO_TEST, task.getTaskStatus());

        //CleanUp
        taskRepository.delete(task);
    }

    @Test
    public void addTaskWithAssignedUsersTest() throws AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem, UserDoesntExistAndUnassignedTaskWasCreated {
        //Given
        TaskDto taskDto = new TaskDto( "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));

        User user1 = new User(0L, "Piotr", "Łecicki", "Zaczi", "sahbdasshouasvcbiuadvb");
        User user2 = new User(0L, "Paweł", "Kowalski", "Zaczi", "sahbdasshouasvcbiuadvb");
        User user3 = new User(0L, "Monika", "Nowak", "Zaczi", "sahbdasshouasvcbiuadvb");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        List<Long> userIds = Arrays.asList(user1.getUserId(), user2.getUserId(), user3.getUserId());

        //When
        Task task = taskService.addTaskWithAssignedUsers(taskDto, userIds);

        //Then
        assertEquals(3, joinUserTaskRepository.findByTask(task).size());
        assertTrue(joinUserTaskRepository.existsByUserAndTask(user1, task));
        assertTrue(joinUserTaskRepository.existsByUserAndTask(user2, task));
        assertTrue(joinUserTaskRepository.existsByUserAndTask(user3, task));

        //CleanUp
        List<JoinUserTask> joinUserTasks = joinUserTaskRepository.findByTask(task);
        joinUserTaskRepository.deleteById(joinUserTasks.get(0).getId());
        joinUserTaskRepository.deleteById(joinUserTasks.get(1).getId());
        joinUserTaskRepository.deleteById(joinUserTasks.get(2).getId());
        taskRepository.delete(task);
        userRepository.delete(user1);
        userRepository.delete(user2);
        userRepository.delete(user3);
    }

    @Test
    public void assignUserToTask() throws TaskDoesntExists, UserDoesntExist, UserHasBeenAssignedBeforeToThisTask {
        //Given
        User user = new User(0L, "Piotr", "Łecicki", "Zaczi", "sahbdasshouasvcbiuadvb");
        user = userRepository.save(user);

        Task task = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        task = taskRepository.save(task);

        //When
        taskService.assignUserToTask(task.getTaskId(), user.getUserId());

        //Then
        assertTrue(joinUserTaskRepository.existsByUserAndTask(user, task));

        //CleanUp
        joinUserTaskRepository.deleteByTaskAndUser(task, user);
        taskRepository.delete(task);
        userRepository.delete(user);
    }

    @Test
    public void unassignUserFromTask() throws TaskDoesntExists, UserDoesntExist, UserIsntAssignToThisTask, UserHasBeenAssignedBeforeToThisTask {
        //Given
        User user = new User(0L, "Piotr", "Łecicki", "Zaczi", "sahbdasshouasvcbiuadvb");
        user = userRepository.save(user);

        Task task = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        task = taskRepository.save(task);

        taskService.assignUserToTask(task.getTaskId(), user.getUserId());

        //When
        taskService.unassignUserFromTask(task.getTaskId(), user.getUserId());

        //Then
        assertFalse(joinUserTaskRepository.existsByUserAndTask(user, task));

        //CleanUp
        taskRepository.delete(task);
        userRepository.delete(user);
    }

    @Test
    public void editTaskTest() throws TaskDoesntExists {
        //Given
        Task task = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        task = taskRepository.save(task);

        EditTaskDto editTaskDto = new EditTaskDto(task.getTaskId(), "Aaa", "Aaaaa",
                TaskStatus.DONE, LocalDate.now().plusDays(3));

        //When
        Task editedTask = taskService.editTask(editTaskDto);

        //Then
        assertEquals(TaskStatus.DONE ,editedTask.getTaskStatus());

        //CleanUp
        taskRepository.delete(editedTask);
    }

    @Test
    public void deleteTaskTest() throws TaskDoesntExists {
        //Given
        Task task = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        task = taskRepository.save(task);

        //When & CleanUp
        taskService.deleteTask(task.getTaskId());

        //Then
        assertFalse(userRepository.existsById(task.getTaskId()));
    }

    @Test
    public void changeStatusTest() throws TaskDoesntExists, ThisStatusHasBeenSetBefore {
        //Given
        Task task = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        task = taskRepository.save(task);

        //When
        taskService.changeStatus(task.getTaskId(), TaskStatus.DONE);

        //Then
        assertEquals(TaskStatus.DONE, taskRepository.findByTaskId(task.getTaskId()).getTaskStatus());

        //CleanUp
        taskRepository.delete(taskRepository.findByTaskId(task.getTaskId()));
    }
}
