package pl.plecicki.taskmanager.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.plecicki.taskmanager.domain.dtos.UserDto;
import pl.plecicki.taskmanager.domain.entities.JoinUserTask;
import pl.plecicki.taskmanager.domain.entities.Task;
import pl.plecicki.taskmanager.domain.entities.User;
import pl.plecicki.taskmanager.domain.enums.TaskStatus;
import pl.plecicki.taskmanager.exceptions.UserDoesntExist;
import pl.plecicki.taskmanager.repositories.JoinUserTaskRepository;
import pl.plecicki.taskmanager.repositories.TaskRepository;
import pl.plecicki.taskmanager.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JoinUserTaskRepository joinUserTaskRepository;

    @Test
    void getFilteredUsersTest() throws UserDoesntExist {
        //Given
        String testFilterName = "TestFilterName 23.10.23r.";

        User user1 = new User(0L, "Piotr", "Łecicki", "Zaczi", "sahbdasshouasvcbiuadvb");
        User user2 = new User(0L, testFilterName, "Kowalski", "Zaczi", "sahbdasshouasvcbiuadvb");
        User user3 = new User(0L, testFilterName, "Nowak", "Zaczi", "sahbdasshouasvcbiuadvb");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        //When
        List<User> users = userService.findUsers(testFilterName, null);

        //Then
        assertEquals(2, users.size());
        assertEquals(testFilterName, users.get(users.size() - 2).getName());
        assertEquals(testFilterName, users.get(users.size() - 1).getName());

        //CleanUp
        userRepository.deleteById(user1.getUserId());
        userRepository.deleteById(user2.getUserId());
        userRepository.deleteById(user3.getUserId());
    }

    @Test
    public void addUserTest() {
        //Given
        UserDto userDto = new UserDto("Piotr", "Łecicki", "Zaczi", "Aaa");

        //When
        User user = userService.addUser(userDto);

        //Then
        assertTrue(userRepository.existsById(user.getUserId()));

        //CleanUp
        userRepository.delete(user);
    }

    @Test
    public void deleteUserTest() {
        //Given
        UserDto userDto = new UserDto("Piotr", "Łecicki", "Zaczi", "Aaa");
        User user = userService.addUser(userDto);

        //When
        userRepository.delete(user);

        //Then
        assertFalse(userRepository.existsById(user.getUserId()));
    }

    @Test
    public void getUsersTasksTest() throws UserDoesntExist {
        //Given
        Task task1 = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(3));
        Task task2 = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(2));
        Task task3 = new Task(0L, "Aaa", "Aaaaa",
                TaskStatus.TO_TEST, LocalDate.now().plusDays(1));

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);
        task3 = taskRepository.save(task3);

        User user = new User(0L, "Piotr", "Łecicki", "Zaczi", "sahbdasshouasvcbiuadvb");
        user = userRepository.save(user);

        JoinUserTask joinUserTask1 = joinUserTaskRepository.save(new JoinUserTask(0L, user, task1));
        JoinUserTask joinUserTask2 = joinUserTaskRepository.save(new JoinUserTask(0L, user, task2));
        JoinUserTask joinUserTask3 = joinUserTaskRepository.save(new JoinUserTask(0L, user, task3));

        //When
        List<Task> tasks = userService.getUsersTasks(user.getUserId());

        //Then
        assertEquals(3, tasks.size());
        assertEquals(task1.getTaskId(), tasks.get(0).getTaskId());
        assertEquals(task2.getTaskId(), tasks.get(1).getTaskId());
        assertEquals(task3.getTaskId(), tasks.get(2).getTaskId());

        //CleanUp
        joinUserTaskRepository.delete(joinUserTask3);
        joinUserTaskRepository.delete(joinUserTask2);
        joinUserTaskRepository.delete(joinUserTask1);
        taskRepository.delete(task1);
        taskRepository.delete(task2);
        taskRepository.delete(task3);
        userRepository.delete(user);
    }
}
