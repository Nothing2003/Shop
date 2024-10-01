package rj.com.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rj.com.store.enities.User;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.security.JwtHelper;

@SpringBootTest
class CakeshopApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private UserRepositories userRepositories;
	@Autowired
	private JwtHelper jwtHelper;
	@Test
	void testToken(){
	User user= userRepositories.findByEmail("soumojitmakar@gmail.com").orElse(null);
		if (user != null) {
			String token = jwtHelper.generateToken(user);
			System.out.println(token);
			System.out.println(jwtHelper.getUsernameFromToken(token));
			System.out.println(jwtHelper.getExpirationDateFromToken(token));
			System.out.println(	jwtHelper.isTokenExpired(token));
		}
	}
}
