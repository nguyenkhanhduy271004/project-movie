package nguyenduy.local.movie.services.impl;

import java.util.ArrayList;
import java.util.List;
import nguyenduy.local.movie.converter.DirectorConverter;
import nguyenduy.local.movie.exceptions.ActorException;
import nguyenduy.local.movie.models.dtos.DirectorDTO;
import nguyenduy.local.movie.models.entities.Director;
import nguyenduy.local.movie.models.request.DirectorRequest;
import nguyenduy.local.movie.repositories.DirectorRepository;
import nguyenduy.local.movie.services.interfaces.IDirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class DirectorServiceImpl implements IDirectorService {

  @Autowired
  private DirectorConverter directorConverter;

  @Autowired
  private DirectorRepository directorRepository;

  @Override
  public void create(DirectorRequest data) {
    try {
      Director director = directorConverter.directorConverter(data);
      directorRepository.save(director);
    } catch (Exception e) {
      throw new ActorException("Xảy ra lỗi khi tạo đạo diễn");
    }
  }

  @Override
  public void update(DirectorRequest data) {
    try {
      Director director = directorConverter.directorConverter(data);
      directorRepository.save(director);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new ActorException("Xảy ra lỗi khi cập nhật thông tin đạo diễn");
    }
  }

  @Override
  public void delete(Long id) {
    try {
      directorRepository.deleteById(id);
    } catch (DataAccessException e) {
      throw new ActorException("Không tìm thấy đạo diễn với id: " + id);
    } catch (Exception e) {
      throw new ActorException("Xảy ra lỗi khi xóa đạo diễn");
    }
  }

  @Override
  public DirectorDTO findById(Long id) {
    try {
      Director director = directorRepository.findById(id)
          .orElseThrow(() -> new ActorException("Không tìm thấy đạo diễn với id: " + id));
      return directorConverter.directorDTOConverter(director);
    } catch (Exception e) {
      throw new ActorException("Xảy ra tìm đạo diễn");
    }
  }

  @Override
  public List<DirectorDTO> findAll() {
    try {
      List<Director> diretors = directorRepository.findAll();
      List<DirectorDTO> result = new ArrayList<>();
      for (Director director : diretors) {
        DirectorDTO directorDTO = directorConverter.directorDTOConverter(director);
        result.add(directorDTO);
      }
      return result;
    } catch (Exception e) {
      throw new ActorException("Xảy ra tìm tất cả đạo diễn");
    }
  }
}
