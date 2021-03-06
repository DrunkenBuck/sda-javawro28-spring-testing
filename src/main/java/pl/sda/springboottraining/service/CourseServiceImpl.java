package pl.sda.springboottraining.service;

import org.springframework.stereotype.Service;
import pl.sda.springboottraining.repository.CourseRepository;
import pl.sda.springboottraining.repository.ParticipantDBRepository;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.repository.model.Participant;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final ParticipantDBRepository participantDBRepository;

    private final EmailService emailService;

    public CourseServiceImpl(CourseRepository courseRepository,
                             ParticipantDBRepository participantDBRepository,
                             EmailService emailService) {
        this.courseRepository = courseRepository;
        this.participantDBRepository = participantDBRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public void create(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void update(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void deleteById(Integer id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<Participant> findParticipantsByCourseId(Integer id) {
        return courseRepository
                .findById(id)
                .map(Course::getParticipants)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> getById(Integer id) {
        return courseRepository.findById(id);
    }

    @Override
    public void assign(Integer id, Integer participantId) {

        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Participant participant = participantDBRepository
                .findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        course.addParticipant(participant);
        participant.addCourse(course);

        courseRepository.save(course);
        participantDBRepository.save(participant);

        emailService.sendSimpleMessage(participant.getEmail(),
                "Nowy kurs",
                "Zostałeś przypisany do kursu: " + course.getName());
    }
}
