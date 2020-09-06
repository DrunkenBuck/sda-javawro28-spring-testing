package pl.sda.springboottraining;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.sda.springboottraining.repository.model.Course;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTestSDA {

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldAddUser() throws Exception {
        Course course = new Course();
        course.setName("java-wro28-testowy-kurs");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(course))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


}
