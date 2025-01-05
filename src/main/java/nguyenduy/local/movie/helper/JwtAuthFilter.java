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
import nguyenduy.local.movie.models.entities.RefreshToken;
import nguyenduy.local.movie.repositories.RefreshTokenRepository;
import nguyenduy.local.movie.services.JwtService;
import nguyenduy.local.movie.services.impl.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/api/v1/auth/login")
        || path.startsWith("/api/v1/auth/register")
        || path.startsWith("/api/v1/movie");
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String userId;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        sendErrorResponse(
            request, response,
            HttpServletResponse.SC_UNAUTHORIZED,
            "Xác thực không thành công",
            "Không tìm thấy token");
        return;
      }

      jwt = authHeader.substring(7);
      if(!isValidToken(jwt)) {
        sendErrorResponse(
            request, response,
            HttpServletResponse.SC_UNAUTHORIZED,
            "Xác thực không thành công",
            "Token không hợp lệ");
        return;
      }

      userId = jwtService.getUserIdFromJwt(jwt);

      if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

        final String usernameFromToken = jwtService.getUsernameFromJwt(jwt);
        if(!usernameFromToken.equals(userDetails.getUsername())) {
          logger.error("Token không hợp lệ");
          sendErrorResponse(
              request, response,
              HttpServletResponse.SC_UNAUTHORIZED,
              "Xác thực không thành công",
              "Token không hợp lệ");
          return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("Xác thực tài khoản thành công: " + userDetails.getUsername());
      }
      filterChain.doFilter(request, response);
    } catch (ServletException | IOException  e) {
      logger.error("Lỗi trong quá trình xác thực JWT", e);
      sendErrorResponse(
          request, response,
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Network Error",
          e.getMessage());
    }

  }

  private void sendErrorResponse(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      int statusCode,
      String error,
      String message) throws IOException {
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

  private boolean isValidToken(String token) {
    try {
      if(jwtService.isTokenExpired(token)) {
        logger.error("Token đã hết hạn");
        return false;
      }

      if(!jwtService.isTokenFormatValid(token)) {
        logger.error("Token không đúng định dạng");
        return false;
      }

      if(!jwtService.isSignatureValid(token)) {
        logger.error("Chữ ký token không hợp lệ");
        return false;
      }

      if(!jwtService.isIssuerToken(token)) {
        logger.error("Nguồn gốc của token không hợp lệ");
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
