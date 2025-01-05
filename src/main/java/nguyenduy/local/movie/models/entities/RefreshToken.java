package nguyenduy.local.movie.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity{

  @Column(name = "refresh_token", nullable = false, unique = true)
  private String refreshToken;

  @OneToOne
  private User user;

  @Column(name = "expiry_date", nullable = false)
  private LocalDateTime expiryDate;

}
