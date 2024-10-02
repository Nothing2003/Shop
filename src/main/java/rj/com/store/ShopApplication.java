package rj.com.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import rj.com.store.enities.Providers;
import rj.com.store.enities.Role;
import rj.com.store.enities.User;
import rj.com.store.helper.AppCon;
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
	public void run(String... args) {
		Role role1 = roleRepository.findByRoleName("ROLE_"+AppCon.ROLE_ADMIN).orElse(null);
		if (role1 == null) {
			role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setRoleName("ROLE_"+ AppCon.ROLE_ADMIN);
			roleRepository.save(role1);
		}
		Role role2 =roleRepository.findByRoleName("ROLE_"+AppCon.ROLE_NORMAL).orElse(null);
		if (role2 == null) {
			role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setRoleName("ROLE_"+AppCon.ROLE_NORMAL);
			roleRepository.save(role2);
		}
		User user=userRepositories.findByEmail("soumojitmakar1234@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setName("Soumojit Makar");
			user.setEmail("soumojitmakar1234@gmail.com");
			user.setPassword(passwordEncoder.encode("soumojitmakar"));
			user.setRoles(List.of(role1,role2));
			user.setGender("Male");
			user.setImageName("https://res-console.cloudinary.com/dfikzvebd/media_explorer_thumbnails/8b0789a5b6b0a31d118be5dd0e62e62a/detailed");
			user.setAbout("I am Admin");
			user.setProviders(Providers.SELF);
			user.setUserId(UUID.randomUUID().toString());
			userRepositories.save(user);
		}
		User user2=userRepositories.findByEmail("soumojitmakar1@gmail.com").orElse(null);
		if (user2 == null) {
			user2 = new User();
			user2.setName("Soumojit Makar");
			user2.setEmail("soumojitmakar1@gmail.com");
			user2.setPassword(passwordEncoder.encode("soumojitmakar"));
			user2.setRoles(List.of(role2));
			user2.setGender("Male");
			user2.setImageName("https://res-console.cloudinary.com/dfikzvebd/media_explorer_thumbnails/8b0789a5b6b0a31d118be5dd0e62e62a/detailed");
			user2.setAbout("I am Normal User");
			user2.setProviders(Providers.SELF);
			user2.setUserId(UUID.randomUUID().toString());
			userRepositories.save(user2);
		}
	}
}
