package rj.com.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rj.com.store.enities.User;
import rj.com.store.repositories.ProductRepository;
import rj.com.store.repositories.UserRepositories;

@SpringBootApplication
public class CakeshopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CakeshopApplication.class, args);
	}
	@Autowired
	UserRepositories userRepositories;
	@Autowired
	ProductRepository productRepository;
	@Override
	public void run(String... args) throws Exception {
		User user = userRepositories.findByEmail("SoumojitMakar").orElse(null);
		if (user == null) {
			user=User.builder()
					.email("SoumojitMakar")
					.name("SoumojitMakar")
					.about("SoumojitMakar")
					.gender("Male")
					.password("123456")
					.build();
		}


	}
}
