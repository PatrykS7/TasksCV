package Tasks.repo;

import Tasks.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepoTest {

    @Autowired
    private UserRepo underTest;

    private String testId;

    @BeforeAll
    void populateDatabase(){

        //given
        User firstUser = new User("Pat",
                "Sta",
                Date.from(Instant.parse("1999-03-20T00:00:00.000Z")),
                "organization_A",
                "aba",
                1);

        User secondUser = new User("Mark",
                "Ban",
                Date.from(Instant.parse("1999-03-20T00:00:00.000Z")),
                "organization_B",
                "baa",
                2);
        secondUser.setDeleted(true);

        User thirdUser = new User("Bark",
                "Ban",
                Date.from(Instant.parse("1989-06-23T00:00:00.000Z")),
                "organization_B",
                "agd",
                3);
        thirdUser.setDeleted(true);

        testId = underTest.save(firstUser).getId();
        underTest.save(secondUser);
        underTest.save(thirdUser);
    }

    @AfterAll
    void emptyDatabase(){

        underTest.deleteAll();
    }

    @Test
    void itShouldFindAllNonDeletedUsers() {

        //when
        List<User> userList = underTest.findAllByDeletedFalseOrDeletedNull();

        //then
        assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    void findByIdAndDeletedFalseOrDeletedNull() {

        //when
        Optional<User> testUser = underTest.findByIdAndDeletedFalseOrDeletedNull(testId);

        //then
        assertThat(testUser.isPresent()).isEqualTo(true);
    }

    @Test
    void findAllByDeletedTrue() {

        //when
        List<User> userList = underTest.findAllByDeletedTrue();

        //then
        assertThat(userList.size()).isEqualTo(2);
    }
}