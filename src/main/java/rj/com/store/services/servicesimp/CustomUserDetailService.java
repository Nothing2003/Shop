package rj.com.store.services.servicesimp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.repositories.UserRepositories;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepositories userRepositories;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositories.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"+username));
    }
}
