package pl.sda.springboottraining.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.springboottraining.repository.model.Course;
import pl.sda.springboottraining.repository.model.Participant;
import pl.sda.springboottraining.service.CourseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/course")
public class CourseEndpoint {

    private final CourseService courseService;

    @Autowired
    public CourseEndpoint(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> findAll() {
        return courseService.findAll();
    }

    @PostMapping
    public void addCourse(@RequestBody Course course) {
        courseService.create(course);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Integer id) {
        Optional<Course> participant = courseService.getById(id);
        //zwroc status 200 jesli kursant istnieje, lub 404 gdy nie istnieje
        return participant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/participant")
    public List<Participant> findParticipantsByCourseId(@PathVariable Integer id) {
        return courseService.findParticipantsByCourseId(id);
    }

    @PutMapping("/{id}/participant/{participantId}")
    public void assignParticipant(@PathVariable Integer id, @PathVariable Integer participantId) {
        courseService.assign(id, participantId);
    }

    @PutMapping
    public void updateCourse(@RequestBody Course course) {
        courseService.update(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteById(id);
    }

}
