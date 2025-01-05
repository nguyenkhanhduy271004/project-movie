package nguyenduy.local.movie.services.interfaces;

import nguyenduy.local.movie.models.request.LoginRequest;
import nguyenduy.local.movie.models.request.RegisterRequest;

public interface IUserService {
  Object authenticate(LoginRequest loginRequest);

  void register(RegisterRequest registerRequest);
}
