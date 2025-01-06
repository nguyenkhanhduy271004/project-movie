package nguyenduy.local.movie.services.interfaces;

import java.util.List;

public interface IBaseService<T, U> {

  void create(T data);
  void update(T data);
  void delete(Long id);
  U findById(Long id);
  List<U> findAll();
}
