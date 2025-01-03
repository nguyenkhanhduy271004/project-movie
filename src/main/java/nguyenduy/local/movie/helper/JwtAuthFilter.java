package nguyenduy.local.movie.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nguyenduy.local.movie.services.JwtService;
import nguyenduy.local.movie.services.impl.CustomUserDetailsService;
import nguyenduy.local.movie.services.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomUserDetailsService customUserDetailsService;
  private final ObjectMapper objectMapper;
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  @Override
  public void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userId;

    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.error("Token miss");
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    userId = jwtService.getUserIdFromJwt(jwt);

    if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

      logger.info("userDetails: " + userDetails.getUsername());

      if(!jwtService.isValidToken(jwt, userDetails)) {
        sendErrorResponse(
            request, response,
            HttpServletResponse.SC_UNAUTHORIZED,
            "Xác thực không thành công",
            "Không tìm thấy token");
      }

//      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//          userDetails,
//          null,
//          userDetails.getAuthorities()
//      );
//
//      authToken.setDetails(
//          new WebAuthenticationDetailsSource().buildDetails(request)
//      );
//
//      SecurityContextHolder.getContext().setAuthentication(authToken);
//      logger.info("Xác thực tài khoản thành công: " + userDetails.getUsername());
    }
    filterChain.doFilter(request, response);
  }

  private void sendErrorResponse(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      int statusCode,
      String error,
      String message) throws IOException{
    response.setStatus(statusCode);
    response.setContentType("application/json;charset=UTF-8");
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", System.currentTimeMillis());
    errorResponse.put("status", statusCode);
    errorResponse.put("error", error);
    errorResponse.put("message", message);
    errorResponse.put("path", request.getRequestURI());

    String jsonResponse = objectMapper.writeValueAsString(errorResponse);

    response.getWriter().write(jsonResponse);
  }
}
