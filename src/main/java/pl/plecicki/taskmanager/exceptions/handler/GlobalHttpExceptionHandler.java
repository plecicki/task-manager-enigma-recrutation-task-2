package pl.plecicki.taskmanager.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.plecicki.taskmanager.exceptions.*;

@ControllerAdvice
public class GlobalHttpExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskDoesntExists.class)
    public ResponseEntity<Object> handleTaskDoesntExists(TaskDoesntExists exception) {
        return new ResponseEntity<>("This task doesn't exists", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ThisStatusHasBeenSetBefore.class)
    public ResponseEntity<Object> handleThisStatusHasBeenSetBefore(ThisStatusHasBeenSetBefore exception) {
        return new ResponseEntity<>("This status has been set before", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesntExist.class)
    public ResponseEntity<Object> handleUserDoesntExist(UserDoesntExist exception) {
        return new ResponseEntity<>("This user doesn't exists", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserDoesntExistAndUnassignedTaskWasCreated.class)
    public ResponseEntity<Object> handleUserDoesntExistAndUnassignedTaskWasCreated
            (UserDoesntExistAndUnassignedTaskWasCreated exception) {
        return new ResponseEntity<>("This user doesn't exists and unassigned task was created", HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserHasBeenAssignedBeforeToThisTask.class)
    public ResponseEntity<Object> handleUserHasBeenAssignedBeforeToThisTask(UserHasBeenAssignedBeforeToThisTask exception) {
        return new ResponseEntity<>("User has been assigned before to this task", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsntAssignToThisTask.class)
    public ResponseEntity<Object> handleUserIsntAssignToThisTask(UserIsntAssignToThisTask exception) {
        return new ResponseEntity<>("User isn't assigned to this task", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem.class)
    public ResponseEntity<Object> handleAtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem
            (AtLeastOneOfUsersIdsIsWrongAndTaskWasCreatedWithoutThem exception) {
        return new ResponseEntity<>("At least one of of users ids is wrong and task was created without them",
                HttpStatus.NO_CONTENT);
    }
}
