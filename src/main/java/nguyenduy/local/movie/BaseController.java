package nguyenduy.local.movie;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/api")
public class BaseController {

  private final JdbcTemplate jdbcTemplate;

  public BaseController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

   @GetMapping
  public String index() {
    String sql = "CREATE TABLE IF NOT EXISTS test_table ("
        + "id INT AUTO_INCREMENT PRIMARY KEY,"
        + "name VARCHAR(255) NOT NULL"
        + ")";
    this.jdbcTemplate.execute(sql);
    return "Tạo sql thành công";
  }
}
