package Tasks.services;

import Tasks.repo.TaskModificationHistoryRepo;
import Tasks.repo.TaskRepo;
import Tasks.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceMockitoTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private MongoOperations mongoOperations;
    @Mock
    private TaskModificationHistoryRepo taskModificationHistoryRepo;

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
}