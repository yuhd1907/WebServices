package ra.edu.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expired}")
    private long expiredTime; // Access token: 1 giờ

    @Value("${jwt.refresh-expired}")
    private long refreshExpiredTime; // Refresh token: 7 ngày

    // ---- ACCESS TOKEN (1 giờ) ----
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---- REFRESH TOKEN (7 ngày) ----
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---- Key dùng để ký ----
    private Key getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // ---- Validate token (trả false nếu hết hạn hoặc lỗi) ----
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token hết hạn: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token không hợp lệ: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token không được hỗ trợ: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Key không hợp lệ: {}", e.getMessage());
        }
        return false;
    }

    // ---- Lấy username từ token bình thường ----
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ---- Lấy username từ REFRESH TOKEN (kể cả khi access token đã hết hạn) ----
    public String getUsernameFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    // ---- Lấy thời gian hết hạn của access token ----
    public Date getExpirationDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}

