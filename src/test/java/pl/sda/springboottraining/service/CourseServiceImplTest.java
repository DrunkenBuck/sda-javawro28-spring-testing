package pl.sda.springboottraining.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.sda.springboottraining.repository.CourseRepository;
import pl.sda.springboottraining.repository.ParticipantDBRepository;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.repository.model.Participant;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;
    @Mock
    ParticipantDBRepository participantDBRepository;
    @Mock
    EmailService emailService;

    @InjectMocks
    CourseServiceImpl courseService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddParticipantToCourse() {
        int courseId = 1;
        int participantId = 5;

        Course testCourse = new Course();
        testCourse.setId(courseId);
        testCourse.setDescription("SDA-TEST-COURSE");

        Participant testParticipant = new Participant();
        testParticipant.setId(participantId);
        testParticipant.setEmail("test-participant@sda.pl");


        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        Mockito.when(participantDBRepository.findById(participantId)).thenReturn(Optional.of(testParticipant));

        courseService.assign(courseId, participantId);


        // verify
        assertEquals(1, testCourse.getParticipants().size());
        assertTrue(testCourse.getParticipants().contains(testParticipant));


        assertEquals(1, testParticipant.getCourses().size());
        assertTrue(testParticipant.getCourses().contains(testCourse));

        Mockito.verify(emailService, Mockito.times(1))
                .sendSimpleMessage(testParticipant.getEmail(), "Nowy kurs", "Zostałeś przypisany do kursu: " + testCourse.getName());
    }

    @Test
    public void test_findParticipantsByCourseId() {
        // testy gdy cos jest
        // testy -> zwroc pusta liste gdy nie ma nic

    }

}