package ra.edu.config.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.edu.entity.User;
import ra.edu.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustom implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        User user = userRepository.loadUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().name()));
        UserDetailCustom detailCustom = UserDetailCustom.builder()
                .username(username)
                .password(user.getPasswordHash())
                .authorities(grantedAuthorities)
                .build();
        return detailCustom;
    }
}
