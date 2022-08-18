package Tasks.mockMvc;

import Tasks.controllers.TaskController;
import Tasks.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
public class GetMethodsTaskStatusTest {

    @MockBean
    TaskService taskService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetAllUsersOkStatus() throws Exception {

        mockMvc.perform(get("/task/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void shouldGetAllDeletedUsersOkStatus() throws Exception {

        mockMvc.perform(get("/task/allDeleted"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
