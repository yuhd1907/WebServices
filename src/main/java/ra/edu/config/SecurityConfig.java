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
// Phân quyền theo phương thức
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    // Tạo cấu hình tùy chỉnh
    // Các tài khoản mặc định (username, password, ROLE)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserDetailsService userDetailsService;

    // thực hiện xác thực thông qua userdertail service và password encoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); //
    }
    // Tầng xác thực và phân quyền
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

                        // STUDENT API - Danh sách: ADMIN & MENTOR; Chi tiết: ADMIN, MENTOR, STUDENT
                        .requestMatchers(HttpMethod.GET, "/api/students")
                        .hasAnyRole("ADMIN", "MENTOR")
                        .requestMatchers("/api/students/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // MENTOR API
                        .requestMatchers("/api/mentors/**")
                        .hasAnyRole("ADMIN", "MENTOR", "STUDENT")

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
                        .requestMatchers(HttpMethod.GET, "/api/assessment_rounds", "/api/assessment_rounds/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                                // Assessment Rounds
                                .requestMatchers(HttpMethod.POST, "/api/assessment_rounds").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/assessment_rounds/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/assessment_rounds/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/assessment_rounds/**").authenticated()

                                // Round Criteria
                                .requestMatchers(HttpMethod.POST, "/api/round_criteria").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/round_criteria/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/round_criteria/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/round_criteria/**").authenticated()

                                // Internship Assignments
                                .requestMatchers(HttpMethod.POST, "/api/internship_assignments").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/internship_assignments/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/internship_assignments/**").authenticated()

                                // Assessment Results
                                .requestMatchers(HttpMethod.POST, "/api/assessment_results").hasRole("MENTOR")
                                .requestMatchers(HttpMethod.PUT, "/api/assessment_results/**").hasRole("MENTOR")
                                .requestMatchers(HttpMethod.GET, "/api/assessment_results/**").authenticated()

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
