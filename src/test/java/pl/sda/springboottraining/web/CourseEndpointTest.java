package pl.sda.springboottraining.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.service.CourseService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseEndpointTest {

    @Mock
    CourseService courseService;

    @InjectMocks
    CourseEndpoint courseEndpoint;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        // MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseEndpoint).build();
    }

    @Test
    public void shouldGetAllCourses() throws Exception {
        Course testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName("SDA-TEST-COURSE");


        List<Course > courses = new ArrayList<>();
        courses.add(testCourse);

        given(courseService.findAll()).willReturn(courses);


        mockMvc.perform(get("/course"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("SDA-TEST-COURSE"));
    }

}