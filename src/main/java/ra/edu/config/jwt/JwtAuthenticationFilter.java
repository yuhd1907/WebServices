package ra.edu.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // chặn request để lấy token và giải mã nếu có
        String token = getTokenFromRequest(request);
        // kiểm tra token
        if (token!=null && jwtService.validateToken(token)){
            // token hợp lệ
            // giải mã nó
            String username = jwtService.getUsernameFromToken(token);
            // load userdetail từ database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // có đc thông tin xác thực
            // lưu vào security context : userDetail , password (null), danh sách quyền
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            );

        };
        // cho request i tới các filter tiếp theop
        filterChain.doFilter(request,response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization!=null && authorization.startsWith("Bearer ")) {
            // lấy token , cắt t Bearer đi
            return authorization.substring(7);
        }
        return null; // ko có token
    }
}
