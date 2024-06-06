package ru.mts.customerservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final int EXPIRATION;

    public JwtService(
            @Value("${spring.security.secret.key}") String secretKey,
            @Value("${spring.security.secret.expiration}") int expiration
    ) {
        this.SECRET_KEY = secretKey;
        this.EXPIRATION = expiration;
    }

    /**
     * Извлекает номер телефона пользователя из данного токена JWT
     *
     * @param token - токен JWT, из которого можно извлечь номер телефона пользователя
     * @return извлеченный номер телефона из токена
     */
    public String extractUserPhone(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает утверждение из данного токена JWT, используя предоставленную функцию распознавания утверждений
     *
     * @param token          - Токен JWT, из которого извлекается утверждение
     * @param claimsResolver - Функция, которая определяет тип утверждения для извлечения из токена
     * @param <T>            Общий тип извлекаемого утверждения
     * @return значение извлеченного утверждения
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Генерирует токен JWT для заданных данных пользователя
     *
     * @param userDetails Указывает данные пользователя, для которых генерируется токен
     * @return токен JWT, представляющий данные пользователя
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Генерирует токен JWT с необязательными дополнительными утверждениями для заданных пользовательских данных
     *
     * @param extraClaims дополнительные утверждения для включения в токен JWT
     * @param userDetails Содержит сведения о пользователе, для которого генерируется токен
     * @return токен JWT, представляющий данные пользователя с дополнительными утверждениями
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Проверяет, является ли данный токен JWT действительным для предоставленных данных пользователя
     *
     * @param token       - Токен JWT для проверки
     * @param userDetails - Данные пользователя, по которым можно проверить токен
     * @return значение True, если токен действителен для данных пользователя, в противном случае значение false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserPhone(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Возвращает время истечения срока действия токенов JWT в секундах
     *
     * @return время истечения срока действия токенов JWT
     */
    public int getExpirationTime() {
        return EXPIRATION;
    }

    /**
     * Проверяет, истек ли срок действия данного токена JWT.
     *
     * @param token - токен JWT для проверки.
     * @return значение True, если срок действия токена истек, и значение false в противном случае.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия из данного токена JWT.
     *
     * @param token - токен JWT, из которого можно извлечь дату истечения срока действия.
     * @return дату истечения срока действия токена.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Проводит парсинг всего токена JWT и извлекает все утверждения
     *
     * @param token - Токен JWT для синтаксического анализа
     * @return все утверждения, содержащиеся в токене
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Генерирует ключ HMAC SHA для подписи JWT
     *
     * @return ключ HMAC SHA для подписи JWT
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
