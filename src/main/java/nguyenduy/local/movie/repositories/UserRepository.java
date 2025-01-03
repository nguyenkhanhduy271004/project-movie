package nguyenduy.local.movie.repositories;

import java.util.Optional;
import nguyenduy.local.movie.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByPhoneNumber(String phoneNumber);
}
