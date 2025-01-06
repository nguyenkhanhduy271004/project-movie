package nguyenduy.local.movie.converter;

import nguyenduy.local.movie.models.dtos.ActorDTO;
import nguyenduy.local.movie.models.entities.Actor;
import nguyenduy.local.movie.models.request.ActorRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ActorConverter {

  public ActorDTO actorDTOConverter(Actor actor) {
    ModelMapper modelMapper = new ModelMapper();
    ActorDTO actorDTO = modelMapper.map(actor, ActorDTO.class);
    return actorDTO;
  }


  public Actor actorConverter(ActorRequest actorRequest) {
    ModelMapper modelMapper = new ModelMapper();
    Actor actor = modelMapper.map(actorRequest, Actor.class);
    return actor;
  }
}
