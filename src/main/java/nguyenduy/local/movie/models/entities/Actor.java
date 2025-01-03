package nguyenduy.local.movie.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "actor")
@NoArgsConstructor
@AllArgsConstructor
public class Actor extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  @Column(name = "profile_picture_url")
  private String profilePictureUrl;

  @Column(name = "nationality")
  private String nationality;

  @Column(name = "awards")
  private String awards;

  @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
  private List<Movie> movies = new ArrayList<>();

}
