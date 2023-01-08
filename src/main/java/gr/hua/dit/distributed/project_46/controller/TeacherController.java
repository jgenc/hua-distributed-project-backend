package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.dao.TeacherDAO;
import gr.hua.dit.distributed.project_46.entity.Course;
import gr.hua.dit.distributed.project_46.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherDAO teacherDAO;

    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    List<Teacher> getall() {
       return teacherDAO.findAll();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    Teacher save(@RequestBody Teacher teacher) {
        teacher.setId(0);
        teacherDAO.save(teacher);
        return teacher;
    }

    @GetMapping("/{id}")
    Teacher get(@PathVariable int id) {
        Teacher teacher = teacherDAO.findById(id);
        return teacher;
    }

}
