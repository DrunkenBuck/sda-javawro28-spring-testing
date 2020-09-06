package pl.sda.springboottraining.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.service.CourseService;

import java.util.List;

class CourseEndpointTestSDA {

    @Mock
    CourseService courseService;

    @InjectMocks
    CourseEndpoint courseEndpoint;

    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseEndpoint).build();
    }

    @Test
    public void shouldReturnAllParticipants() throws Exception {
        int courseId = 10;
        Course course = new Course();
        course.setId(courseId);
        course.setName("SDA-TEST-COURSE");


        Mockito.when(courseService.findAll()).thenReturn(List.of(course));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/course"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(courseId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("SDA-TEST-COURSE"));
    }

    @Test
    public void shouldAddNewCourse() throws Exception {
        int courseId = 10;
        Course course = new Course();
        course.setId(courseId);
        course.setName("SDA-TEST-COURSE");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // todo: napisz kolejny test, tym razem z błędną nazwą kursu - sprawdz, czy status do BAD_REQUEST
    @Test
    public void shouldReturnBadRequestWhenNameTooShort() throws Exception {
        int courseId = 10;
        Course course = new Course();
        course.setId(courseId);
        course.setName("AB");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}