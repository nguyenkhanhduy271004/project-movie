package nguyenduy.local.movie.converter;

import nguyenduy.local.movie.models.dtos.DirectorDTO;
import nguyenduy.local.movie.models.entities.Director;
import nguyenduy.local.movie.models.request.DirectorRequest;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner.Mode;
import org.springframework.stereotype.Component;

@Component
public class DirectorConverter {

  public DirectorDTO directorDTOConverter(Director director) {
    ModelMapper modelMapper = new ModelMapper();
    DirectorDTO directorDTO = modelMapper.map(director, DirectorDTO.class);
    return directorDTO;
  }

  public Director directorConverter(DirectorRequest directorRequest) {
    ModelMapper modelMapper = new ModelMapper();
    Director director = modelMapper.map(directorRequest, Director.class);
    return director;
  }

}
