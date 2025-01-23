package nguyenduy.local.movie.converter;

import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.models.request.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User userConverter(RegisterRequest registerRequest) {
    ModelMapper modelMapper = new ModelMapper();
    User user = modelMapper.map(registerRequest, User.class);
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setRole("USER");
    user.setCoin(0D);
    return user;
  }
}
