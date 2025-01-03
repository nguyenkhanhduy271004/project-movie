package nguyenduy.local.movie.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "episode")
@NoArgsConstructor
@AllArgsConstructor
public class Episode extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @Column(name = "episode_number")
  private Integer episodeNumber;

  @Column(name = "duration")
  private String duration;

  @Column(name = "video_url")
  private String videoUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;
}
