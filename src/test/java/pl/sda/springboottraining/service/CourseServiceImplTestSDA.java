package pl.sda.springboottraining.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.sda.springboottraining.repository.CourseRepository;
import pl.sda.springboottraining.repository.ParticipantDBRepository;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.repository.model.Participant;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseServiceImplTestSDA {

    // z zaleznosci robimy Mocki
    @Mock
    ParticipantDBRepository participantRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    EmailService emailService;


    // do klasy ktora chcemy testowac chcemy wstrzyknac te mocki
    @InjectMocks
    CourseServiceImpl courseService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldAssignNewParticipantToCourse() {
        // AAA
        // Arrange - tworzymy zmienne, mockujemy obiekty
        int courseId = 1;
        Course course = new Course();
        course.setId(courseId);
        course.setName("SDA-TEST-COURSE");

        int participantId = 5;
        Participant participant = new Participant();
        participant.setId(participantId);
        participant.setEmail("test-user@sda.pl");
        participant.setLastName("Kowalski");


        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        Mockito.when(participantRepository.findById(participantId)).thenReturn(Optional.of(participant));

        // ACT
        courseService.assign(courseId, participantId);

        // Assert
        assertTrue(course.getParticipants().contains(participant));
        assertTrue(participant.getCourses().contains(course));
        assertEquals(1, course.getParticipants().size());

        Mockito.verify(
                emailService,
                Mockito.times(1))
                .sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

    }

    @Test
    public void shouldGetAllParticipantsFromCourse() {
        int courseId = 1;
        Course course = createCourse(courseId, "SDA-TEST-COURSE");

        int participantId = 5;
        Participant participant = createParticipant(participantId, "test-user@sda.pl", "Kowalski");
        Participant participant2 = createParticipant(participantId, "test-user2@sda.pl", "Nowak");
        Participant participant3 = createParticipant(participantId, "test-user3@sda.pl", "Wiśniewski");

        course.getParticipants().add(participant);
        course.getParticipants().add(participant2);
        course.getParticipants().add(participant3);

        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        List<Participant> participantsByCourseId = courseService.findParticipantsByCourseId(courseId);
        assertEquals(3, participantsByCourseId.size());
    }

    public Course createCourse(int courseId, String courseName) {
        Course course = new Course();
        course.setId(courseId);
        course.setName(courseName);
        return course;
    }

    public Participant createParticipant(int participantId, String email, String lastName) {
        Participant participant = new Participant();
        participant.setId(participantId);
        participant.setEmail(email);
        participant.setLastName(lastName);
        return participant;
    }

}