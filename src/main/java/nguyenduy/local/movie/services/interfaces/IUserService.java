package nguyenduy.local.movie.services.interfaces;

import nguyenduy.local.movie.models.dtos.LoginRequest;
import nguyenduy.local.movie.models.dtos.LoginResponse;

public interface IUserService {
  Object authenticate(LoginRequest loginRequest);
}
