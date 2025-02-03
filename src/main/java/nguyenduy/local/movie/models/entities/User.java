package nguyenduy.local.movie.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "role")
  private String role;

  @Column(name = "coin")
  private double coin;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "user_movies", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "movie_id")
  private List<Long> movieIds = new ArrayList<>();

  @OneToOne (mappedBy = "user")
  private RefreshToken refreshToken;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Transaction> transactions = new ArrayList<>();



}
