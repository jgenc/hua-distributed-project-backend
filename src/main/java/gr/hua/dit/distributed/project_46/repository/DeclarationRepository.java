package gr.hua.dit.distributed.project_46.repository;

import gr.hua.dit.distributed.project_46.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

    @Query("select d from Declaration d where d.notaryTin = ?1 or d.purchaserTin = ?1 or d.sellerTin = ?1")
    List<Declaration> findByTin(String tin);

}
