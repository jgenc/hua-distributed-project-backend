package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Course;

import java.util.List;

public interface CourseDAO {

    public List<Course> findAll();
    public void save(Course course);

    public Course findById(int id);
}
