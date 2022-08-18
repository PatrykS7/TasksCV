package Tasks.services;

import Tasks.repo.TaskModificationHistoryRepo;
import Tasks.repo.TaskRepo;
import Tasks.repo.UserModificationHistoryRepo;
import Tasks.repo.UserRepo;
import Tasks.services.ModificationHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ModificationHistoryServiceMockitoTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserModificationHistoryRepo userModificationHistoryRepo;
    @Mock
    private TaskModificationHistoryRepo taskModificationHistoryRepo;

    private ModificationHistoryService modificationHistoryService;

    @BeforeEach
    void setUp() {

        modificationHistoryService = new ModificationHistoryService(userModificationHistoryRepo, taskModificationHistoryRepo, userRepo, taskRepo);
    }

    @Test
    void canGetHistoryByUserId() {

        modificationHistoryService.getHistoryByUserId("a");
        verify(userRepo).findById("a");
    }

    @Test
    void canGetHistoryByTaskId() {

        modificationHistoryService.getHistoryByTaskId("a");
        verify(taskRepo).findById("a");
    }
}