package Tasks.services;

import Tasks.entities.User;
import Tasks.entities.UserModificationHistory;
import Tasks.repo.TaskRepo;
import Tasks.repo.UserModificationHistoryRepo;
import Tasks.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTests {

    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserModificationHistoryRepo userModificationHistoryRepo;
    @Mock
    private Errors errors;

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
    void canSaveUser() {
        //given
        User firstUser = new User("Pat",
                "Sta",
                Date.from(Instant.parse("1999-03-20T00:00:00.000Z")),
                "organization_A",
                "aba",
                1);

        //when
        Mockito.when(userModificationHistoryRepo.save(Mockito.any())).thenReturn(new UserModificationHistory("1"));
        userService.saveUser(firstUser, errors);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(firstUser);
    }

    @Test
    void canDeleteUserById() {

        //given
        User firstUser = new User(
                "Pat",
                "Sta",
                Date.from(Instant.parse("1999-03-20T00:00:00.000Z")),
                "organization_A",
                "aba",
                1);
        firstUser.setId("123");

        //when
        Mockito.when(userRepo.findById("123")).thenReturn(Optional.of(firstUser));
        Mockito.when(userModificationHistoryRepo.save(Mockito.any())).thenReturn(new UserModificationHistory("123"));
        userService.deleteUserById(firstUser.getId());

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        InOrder inOrder = Mockito.inOrder(userRepo);
        inOrder.verify(userRepo).findById(firstUser.getId());
        inOrder.verify(userRepo).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getDeleted()).isEqualTo(true);
    }
}