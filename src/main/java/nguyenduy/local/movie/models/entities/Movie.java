package nguyenduy.local.movie.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Entity
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Movie extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @Column(name = "origin_name")
  private String originName;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @Column(name = "type")
  private String type;

  @Column(name = "thumb_url")
  private String thumbUrl;

  @Column(name = "poster_url")
  private String posterUrl;
//
//  @Column(name = "is_copyright")
//  private boolean isCopyright;

//  @Column(name = "has_sub")
//  private boolean hasSub;

  @Column(name = "time")
  private String time;

  @Column(name = "episode_current")
  private String episodeCurrent;

  @Column(name = "episode_total")
  private String episodeTotal;

  @Column(name = "quality")
  private String quality;

  @Column(name = "lang")
  private String lang;

  @Column(name = "view")
  private Integer view;

  @Column(name = "category")
  private String category;

  @Column(name = "coin")
  private Double coin;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "actor_movie", joinColumns = @JoinColumn(name = "movie_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "actor_id", nullable = false))
  private List<Actor> actors = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "director_movie", joinColumns = @JoinColumn(name = "movie_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "director_id", nullable = false))
  private List<Director> directors = new ArrayList<>();

  @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Episode> episodes = new ArrayList<>();

  @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

}
