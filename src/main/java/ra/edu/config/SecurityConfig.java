package ra.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.edu.config.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(req -> req
                        // PUBLIC
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/refresh-token",
                                "/api/auth/register"
                        ).permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // AUTHENTICATED USER
                        .requestMatchers(HttpMethod.GET, "/api/auth/me")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // USER MANAGEMENT - ADMIN ONLY
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // STUDENT API
                        .requestMatchers(HttpMethod.GET, "/api/students")
                        .hasAnyRole("ADMIN", "MENTOR")
                        .requestMatchers(HttpMethod.POST, "/api/students")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**")
                        .hasAnyRole("ADMIN", "STUDENT")

                        // MENTOR API
                        .requestMatchers(HttpMethod.GET, "/api/mentors")
                        .hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/mentors", "/api/mentors/*/students")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/mentors/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/mentors/**")
                        .hasAnyRole("ADMIN", "MENTOR")

                        // INTERNSHIP PHASE API
                        .requestMatchers(HttpMethod.GET, "/api/internship_phases", "/api/internship_phases/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/internship_phases")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/internship_phases/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/internship_phases/**")
                        .hasRole("ADMIN")

                        // EVALUATION CRITERIA API
                        .requestMatchers(HttpMethod.GET, "/api/evaluation_criteria", "/api/evaluation_criteria/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/evaluation_criteria")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/evaluation_criteria/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/evaluation_criteria/**")
                        .hasRole("ADMIN")

                        // ASSESSMENT ROUNDS API
                        .requestMatchers(HttpMethod.GET, "/api/assessment_rounds", "/api/assessment_rounds/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/assessment_rounds")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/assessment_rounds/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assessment_rounds/**")
                        .hasRole("ADMIN")

                        // ROUND CRITERIA API
                        .requestMatchers(HttpMethod.GET, "/api/round_criteria", "/api/round_criteria/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/round_criteria")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/round_criteria/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/round_criteria/**")
                        .hasRole("ADMIN")

                        // OTHER API
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
