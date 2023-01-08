package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Course;
import gr.hua.dit.distributed.project_46.entity.Teacher;

import java.util.List;

public interface TeacherDAO {

    public List<Teacher> findAll();
    public void save(Teacher teacher);

    public Teacher findById(int id);
}
