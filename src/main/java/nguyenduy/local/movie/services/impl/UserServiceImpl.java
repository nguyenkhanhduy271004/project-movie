package nguyenduy.local.movie.services.impl;

import java.util.HashMap;
import java.util.Map;
import nguyenduy.local.movie.exceptions.BadCredentialException;
import nguyenduy.local.movie.models.dtos.LoginRequest;
import nguyenduy.local.movie.models.dtos.LoginResponse;
import nguyenduy.local.movie.models.dtos.UserDTO;
import nguyenduy.local.movie.models.entities.User;
import nguyenduy.local.movie.repositories.UserRepository;
import nguyenduy.local.movie.resources.ErrorResource;
import nguyenduy.local.movie.services.JwtService;
import nguyenduy.local.movie.services.interfaces.IUserService;
import org.apache.catalina.authenticator.BasicAuthenticator.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{

  @Autowired
  private JwtService jwtService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;



  Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public Object authenticate(LoginRequest loginRequest) {

    try {
      String phoneNumber = loginRequest.getPhoneNumber();
      User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new BadCredentialException("Email hoặc mật khẩu không chính xác"));

      if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new BadCredentialException("Email hoặc mật khẩu không chính xác");
      }
      String token = jwtService.generateToken(user.getId(), user.getPhoneNumber());
      LoginResponse loginResponse = new LoginResponse(token, new UserDTO(user.getId(), user.getPhoneNumber()));
      return loginResponse;
    } catch (BadCredentialException e) {
      logger.error("Lỗi xác thực : {}", e.getMessage());
      Map<String, String> errors = new HashMap<>();
      errors.put("message", e.getMessage());
      ErrorResource errorResource = new ErrorResource("Có vấn đề xảy ra trong quá trình xác thực", errors);
      return errorResource;
    }
  }
}
