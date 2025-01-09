package nguyenduy.local.movie.services.impl;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import nguyenduy.local.movie.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    nguyenduy.local.movie.models.entities.User user = userRepository.findById(Long.parseLong(userId))
        .orElseThrow(() -> new UsernameNotFoundException("User không tồn tại"));

    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

    return new org.springframework.security.core.userdetails.User(
        user.getPhoneNumber(),
        user.getPassword(),
        Collections.singletonList(authority)
    );
  }
}
