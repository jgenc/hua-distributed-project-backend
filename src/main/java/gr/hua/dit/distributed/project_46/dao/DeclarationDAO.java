package gr.hua.dit.distributed.project_46.dao;

import gr.hua.dit.distributed.project_46.entity.Declaration;

import java.util.List;

public interface DeclarationDAO {
    public List<Declaration> findAll();

    public void save(Declaration declaration);

    public Declaration findById(int id);
}
