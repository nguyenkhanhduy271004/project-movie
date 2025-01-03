package nguyenduy.local.movie;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieApplication {

	public static void main(String[] args) {

		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

		// Convert key to Base64 (to store in application.properties)
		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

		System.out.println("HS512 Secret Key (Base64): " + base64Key);
		SpringApplication.run(MovieApplication.class, args);
	}

}
