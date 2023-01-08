package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Declaration;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DeclarationDAOImpl implements DeclarationDAO{

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Declaration> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Declaration ", Declaration.class);
        List<Declaration> declarations = query.getResultList();
        return declarations;
    }

    @Override
    @Transactional
    public void save(Declaration declaration) {
        Declaration adeclaration = entityManager.merge(declaration);
    }

    @Override
    @Transactional
    public Declaration findById(int id) {
        return entityManager.find(Declaration.class, id);
    }
}
