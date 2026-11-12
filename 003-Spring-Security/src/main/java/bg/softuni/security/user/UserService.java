package bg.softuni.security.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
    }

    /** @return false when the username is already taken */
    public boolean register(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            return false;
        }
        userRepository.save(new UserEntity(dto.username(), passwordEncoder.encode(dto.password()), Set.of(UserRole.USER)));
        return true;
    }

    public List<String> allUsernames() {
        return userRepository.findAll().stream().map(UserEntity::getUsername).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }
}
