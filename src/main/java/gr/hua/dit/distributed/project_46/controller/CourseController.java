package gr.hua.dit.distributed.project_46.controller;
import java.util.List;
import java.util.Optional;


import gr.hua.dit.distributed.project_46.dao.CourseDAO;
import gr.hua.dit.distributed.project_46.dao.TeacherDAO;
import gr.hua.dit.distributed.project_46.entity.Course;
import gr.hua.dit.distributed.project_46.entity.Review;
import gr.hua.dit.distributed.project_46.entity.Student;
import gr.hua.dit.distributed.project_46.entity.Teacher;
import gr.hua.dit.distributed.project_46.repository.ReviewRepository;
import gr.hua.dit.distributed.project_46.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("")
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable int id) {
        return courseDAO.findById(id);
    }

    @PostMapping("")
    public Course save(@RequestBody Course course) {
        course.setId(0);
        courseDAO.save(course);
        return course;
    }

    @PostMapping("/{cid}/teacher")
    public Teacher addTeacher(@PathVariable int cid, @RequestBody Teacher teacher) {
        int teacherId = teacher.getId();
        Course course = courseDAO.findById(cid);

        if(course == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        if(teacherId != 0) {
            Teacher ateacher = teacherDAO.findById(teacherId);
            course.setTeacher(ateacher);
            teacherDAO.save(teacher);
            return ateacher;
        }

        course.setTeacher(teacher);
        teacherDAO.save(teacher);
        return teacher;

    }


    @PostMapping("/{cid}/review")
    public Review addReview(@PathVariable int cid, @RequestBody Review review) {
        Course course = courseDAO.findById(cid);

        if(course == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        course.addReview(review);
        reviewRepository.save(review);
        return review;

    }

    @GetMapping("/{cid}/reviews")
    public List<Review> getCourseReviews(@PathVariable int cid) {
        Course course = courseDAO.findById(cid);

        if(course == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        return course.getReviews();

    }

    @PostMapping("/{cid}/student")
    public Student setStudent(@PathVariable int cid, @RequestBody Student student) {
        int studentId = student.getId();
        Course course = courseDAO.findById(cid);

        if(course == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        if(studentId != 0) {
            Student astudent = studentRepository.findById(studentId).orElse(null);

            course.addStudent(astudent);
            studentRepository.save(astudent);
            return astudent;
        }


        course.addStudent(student);
        studentRepository.save(student);
        return student;

    }













}