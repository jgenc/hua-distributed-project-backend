package gr.hua.dit.distributed.project_46.repository;

import gr.hua.dit.distributed.project_46.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

}
