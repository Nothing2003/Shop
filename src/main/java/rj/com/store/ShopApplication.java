package rj.com.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import rj.com.store.enities.Role;
import rj.com.store.enities.User;
import rj.com.store.repositories.ProductRepository;
import rj.com.store.repositories.RoleRepository;
import rj.com.store.repositories.UserRepositories;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ShopApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepositories userRepositories;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		Role role1 = roleRepository.findByRoleName("ROLE_ADMIN").orElse(null);
		if (role1 == null) {
			role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setRoleName("ROLE_ADMIN");
			roleRepository.save(role1);
		}
		Role role2 =roleRepository.findByRoleName("ROLE_NORMAL").orElse(null);
		if (role2 == null) {
			role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setRoleName("ROLE_NORMAL");
			roleRepository.save(role2);
		}
		User user=userRepositories.findByEmail("soumojitmakar@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setName("Soumojit Makar");
			user.setEmail("soumojitmakar@gmail.com");
			user.setPassword(passwordEncoder.encode("soumojitmakar"));
			user.setRoles(List.of(role1,role2));
			user.setGender("Male");
			user.setImageName("https://res-console.cloudinary.com/dfikzvebd/media_explorer_thumbnails/8b0789a5b6b0a31d118be5dd0e62e62a/detailed");
			user.setAbout("I am Admin");
			user.setUserId(UUID.randomUUID().toString());
			userRepositories.save(user);
		}

	}
}
