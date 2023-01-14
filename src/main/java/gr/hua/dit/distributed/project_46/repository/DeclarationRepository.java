package gr.hua.dit.distributed.project_46.repository;

import gr.hua.dit.distributed.project_46.entity.Declaration;
import gr.hua.dit.distributed.project_46.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    @Transactional
    @Modifying
    @Query("update Declaration d set d.notary = ?1 where d.notary = ?2")
    int updateNotary(Person newNotary, Person notary);

    @Transactional
    @Modifying
    @Query("update Declaration d set d.purchaser = ?1 where d.purchaser = ?2")
    int updatePurchaser(Person newPurchaser, Person purchaser);

    @Transactional
    @Modifying
    @Query("update Declaration d set d.seller = ?1 where d.seller = ?2")
    int updateSeller(Person newSeller, Person seller);

}
