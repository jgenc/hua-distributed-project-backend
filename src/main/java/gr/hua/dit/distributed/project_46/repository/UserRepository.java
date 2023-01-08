package gr.hua.dit.distributed.project_46.repository;

import gr.hua.dit.distributed.project_46.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByTin(String tin);

    Boolean existsByUsername(String username);

    Boolean existsByTin(String tin);

}