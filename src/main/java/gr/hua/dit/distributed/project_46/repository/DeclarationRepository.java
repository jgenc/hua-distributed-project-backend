package gr.hua.dit.distributed.project_46.repository;

import gr.hua.dit.distributed.project_46.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

    @Query("select d from Declaration d where d.notary.tin = ?1 or d.seller.tin = ?1 or d.purchaser.tin = ?1")
    List<Declaration> findByTin(String tin);

}
