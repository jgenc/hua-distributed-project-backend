package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Course;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO{

    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<Course> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Course", Course.class);
        List<Course> courses = query.getResultList();
        return courses;
    }

    @Override
    @Transactional
    public void save(Course course) {
        Course acourse = entityManager.merge(course);
    }

    @Override
    @Transactional
    public Course findById(int id) {
        return entityManager.find(Course.class, id);
    }
}
