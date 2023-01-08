package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.dao.TeacherDAO;
import gr.hua.dit.distributed.project_46.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TeacherFormController {

    @Autowired
    private TeacherDAO teacherDAO;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/teacherform")
    public String showSignUpForm(Teacher teacher) {
        return "add-teacher";
    }

    @PostMapping(path = "/teacherform", consumes = "application/x-www-form-urlencoded")
    public String createTeacher(Model model, @ModelAttribute Teacher teacher) {
        teacherDAO.save(teacher);
        return "redirect:/";

    }
}
