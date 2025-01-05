package nguyenduy.local.movie.databases.seeder;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nguyenduy.local.movie.models.entities.Actor;
import nguyenduy.local.movie.models.entities.Director;
import nguyenduy.local.movie.models.entities.Episode;
import nguyenduy.local.movie.models.entities.Movie;
import nguyenduy.local.movie.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) throws Exception{
    if (isTableEmpty()) {
      seedData();
    } else {
      System.out.println("Data already exists in the database.");
    }
  }

  private boolean isTableEmpty() {
    Long count = (Long) entityManager.createQuery("SELECT COUNT(id) from User").getSingleResult();
    return count == 0;
  }

  private void seedData() {
//    String hashedPassword = passwordEncoder.encode("1234");
//
//    User user1 = new User("0987654321", hashedPassword, "USER");
//    User user2 = new User("0123456789", hashedPassword, "ADMIN");
//
//    entityManager.persist(user1);
//    entityManager.persist(user2);
//
//    System.out.println("User data seeding completed.");

//    Movie movie = new Movie();
//    movie.setName("Đơn vị chiến thuật: Comrades in Arms");
//    movie.setOriginName("Tactical Unit: Comrades in Arms");
//    movie.setContent("Hai nhà lãnh đạo đơn vị chiến thuật buộc phải gác lại những khác biệt của họ để giúp bắt một nhóm cướp.");
//    movie.setType("single");
//    movie.setSlug("don-vi-chien-thuat-comrades-in-arms");
//    movie.setThumbUrl("https://img.ophim.live/uploads/movies/don-vi-chien-thuat-comrades-in-arms-thumb.jpg");
//    movie.setPosterUrl("https://img.ophim.live/uploads/movies/don-vi-chien-thuat-comrades-in-arms-poster.jpg");
//    movie.setCopyright(false);
//    movie.setHasSub(true);
//    movie.setTime("1H20M41S");
//    movie.setEpisodeCurrent("Full");
//    movie.setEpisodeTotal("1");
//    movie.setQuality("HD");
//    movie.setLang("Vietsub");
//    movie.setView(82);
//
//    Actor actor1 = new Actor();
//    actor1.setName("Maggie SiuMeiKi");
//    Actor actor2 = new Actor();
//    actor2.setName("Suet Lam");
//    Actor actor3 = new Actor();
//    actor3.setName("Nhậm Đạt Hoa");
//
//    Director director = new Director();
//    director.setName("Wing-cheong Law");
//
//    Episode episode = new Episode();
//    episode.setName("Full");
//
//    // Persist the Movie first
//    entityManager.persist(movie);
//
//    // Set the Movie reference in Episode after Movie is persisted
//    episode.setMovie(movie);
//
//    // Add the actors and directors to the movie
//    movie.getActors().add(actor1);
//    movie.getActors().add(actor2);
//    movie.getActors().add(actor3);
//    movie.getDirectors().add(director);
//    movie.getEpisodes().add(episode);
//
//    // Persist the other entities
//    entityManager.persist(actor1);
//    entityManager.persist(actor2);
//    entityManager.persist(actor3);
//    entityManager.persist(director);
//    entityManager.persist(episode);
//
//    System.out.println("Data seeding completed.");
  }

}