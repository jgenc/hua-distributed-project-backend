package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Course;
import gr.hua.dit.distributed.project_46.entity.Teacher;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TeacherDAOImpl implements TeacherDAO{
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Teacher> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Teacher", Teacher.class);
        List<Teacher> teachers = query.getResultList();
        return teachers;
    }

    @Override
    @Transactional
    public void save(Teacher teacher) {
        Teacher ateacher = entityManager.merge(teacher);
    }

    @Override
    @Transactional
    public Teacher findById(int id) {
        return entityManager.find(Teacher.class, id);
    }

}
