package nguyenduy.local.movie.services.impl;

import java.util.ArrayList;
import java.util.List;
import nguyenduy.local.movie.converter.ActorConverter;
import nguyenduy.local.movie.exceptions.ActorException;
import nguyenduy.local.movie.models.dtos.ActorDTO;
import nguyenduy.local.movie.models.entities.Actor;
import nguyenduy.local.movie.models.request.ActorRequest;
import nguyenduy.local.movie.repositories.ActorRepository;
import nguyenduy.local.movie.services.interfaces.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceServiceImpl implements IActorService {

  @Autowired
  private ActorRepository actorRepository;

  @Autowired
  private ActorConverter actorConverter;


  @Override
  public void create(ActorRequest data) {
    try {
      Actor actor = actorConverter.actorConverter(data);
      actorRepository.save(actor);
    } catch (Exception e) {
      throw new ActorException("Xảy ra lỗi khi tạo diễn viên");
    }
  }

  @Override
  public void update(ActorRequest data) {
    try {
      Actor actor = actorConverter.actorConverter(data);
      actorRepository.save(actor);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new ActorException("Xảy ra lỗi khi cập nhật thông tin diễn viên");
    }
  }

  @Override
  public void delete(Long id) {
    try {
      actorRepository.deleteById(id);
    } catch (DataAccessException e) {
      throw new ActorException("Không tìm thấy diễn viên với id: " + id);
    } catch (Exception e) {
      throw new ActorException("Xảy ra lỗi khi xóa diễn viên");
    }
  }

  @Override
  public ActorDTO findById(Long id) {
    try {
      Actor actor = actorRepository.findById(id)
          .orElseThrow(() -> new ActorException("Không tìm thấy diễn viên với id: " + id));
      return actorConverter.actorDTOConverter(actor);
    } catch (Exception e) {
      throw new ActorException("Xảy ra tìm diễn viên");
    }
  }

  @Override
  public List<ActorDTO> findAll() {
    try {
      List<Actor> actors = actorRepository.findAll();
      List<ActorDTO> result = new ArrayList<>();
      for (Actor actor : actors) {
        ActorDTO actorDTO = actorConverter.actorDTOConverter(actor);
        result.add(actorDTO);
      }
      return result;
    } catch (Exception e) {
      throw new ActorException("Xảy ra tìm tất cả diễn viên");
    }
  }
}
