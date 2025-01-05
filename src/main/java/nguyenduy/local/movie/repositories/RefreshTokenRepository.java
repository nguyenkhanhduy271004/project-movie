package nguyenduy.local.movie.repositories;

import java.util.Optional;
import nguyenduy.local.movie.models.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  boolean existsByRefreshToken(String refreshToken);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  RefreshToken findByUserId(Long userId);
}
