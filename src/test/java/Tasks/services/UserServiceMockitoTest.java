package Tasks.services;

import Tasks.repo.TaskRepo;
import Tasks.repo.UserModificationHistoryRepo;
import Tasks.repo.UserRepo;
import Tasks.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceMockitoTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserModificationHistoryRepo userModificationHistoryRepo;

    private UserService userService;

    @BeforeEach
    void setUp() {

        userService = new UserService(userRepo, taskRepo, userModificationHistoryRepo);
    }

    @Test
    void canGetAllNonDeletedUsers() {

        userService.getAllUsers();
        verify(userRepo).findAllByDeletedFalseOrDeletedNull();
    }

    @Test
    void canGetAllDeletedUsers() {

        userService.getAllDeletedUsers();
        verify(userRepo).findAllByDeletedTrue();
    }

    @Test
    void canGetUerById() {

        userService.getUerById("s");
        verify(userRepo).findByIdAndDeletedFalseOrDeletedNull("s");
    }

    @Test
    void canDeleteUserById() {

        userService.deleteUserById("");
        verify(userRepo).findById("");
    }
}