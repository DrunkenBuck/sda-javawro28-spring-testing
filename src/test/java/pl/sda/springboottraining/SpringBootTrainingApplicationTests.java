package pl.sda.springboottraining;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;
import pl.sda.springboottraining.repository.model.Course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpringBootTrainingApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @LocalServerPort
    private int port;


    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    public void should_save_new_course() throws Exception {
        // cannot create test without authentication
        Course testCourse = new Course();

        testCourse.setName("SDA-TEST-COURSE-INTEGRAION-TEST-2");

        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/course", testCourse, Object.class);


        assertEquals(objectResponseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
    }

}
