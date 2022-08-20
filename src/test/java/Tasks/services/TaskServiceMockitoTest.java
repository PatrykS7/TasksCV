package Tasks.services;

import Tasks.entities.Task;
import Tasks.entities.TaskModificationHistory;
import Tasks.entities.User;
import Tasks.entities.UserModificationHistory;
import Tasks.repo.TaskModificationHistoryRepo;
import Tasks.repo.TaskRepo;
import Tasks.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.validation.Errors;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceMockitoTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private MongoOperations mongoOperations;
    @Mock
    private TaskModificationHistoryRepo taskModificationHistoryRepo;
    @Mock
    private Errors errors;

    private TaskService taskService;

    @BeforeEach
    void setUp() {

        taskService = new TaskService(taskRepo, mongoOperations, taskModificationHistoryRepo);
    }

    @Test
    void canGetAllTasks() {

        taskService.getAllTasks();
        verify(taskRepo).findAllByDeletedFalseOrDeletedNull();
    }

    @Test
    void anGetAllDeletedTasks() {

        taskService.getAllDeletedTasks();
        verify(taskRepo).findAllByDeletedTrue();
    }

    @Test
    void canGetTaskById() {

        taskService.getTaskById("a");
        verify(taskRepo).findByIdAndDeletedFalseOrDeletedNull("a");
    }

    @Test
    void canSaveTask(){

        Task firstTask = new Task(
                "TaskOne",
                3,
                5,
                Date.from(Instant.parse("1999-03-20T00:00:00.000Z")),
                true,
                "organization_A",
                "aba",
                3,
                20
        );

        //when
        Mockito.when(taskModificationHistoryRepo.save(Mockito.any())).thenReturn(new TaskModificationHistory("1"));
        taskService.saveTask(firstTask, errors);

        //then
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);

        verify(taskRepo).save(taskArgumentCaptor.capture());

        Task capturedTask = taskArgumentCaptor.getValue();

        assertThat(capturedTask).isEqualTo(firstTask);
    }
}