package nguyenduy.local.movie.specifications;

import nguyenduy.local.movie.models.entities.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecifications {

  public static Specification<Movie> findMovie(String type, String category, String lang) {
    return (root, query, criteriaBuilder) -> {
      Specification<Movie> spec = Specification.where(null);

      if (type != null && !type.isEmpty()) {
        spec = spec.and((root1, query1, criteriaBuilder1) ->
            criteriaBuilder1.equal(root1.get("type"), type));
      }

      if (category != null && !category.isEmpty()) {
        spec = spec.and((root1, query1, criteriaBuilder1) ->
            criteriaBuilder1.like(root1.get("category"), "%" + category + "%"));
      }

      if (lang != null && !lang.isEmpty()) {
        spec = spec.and((root1, query1, criteriaBuilder1) ->
            criteriaBuilder1.equal(root1.get("lang"), lang));
      }

      return spec.toPredicate(root, query, criteriaBuilder);
    };
  }
}
