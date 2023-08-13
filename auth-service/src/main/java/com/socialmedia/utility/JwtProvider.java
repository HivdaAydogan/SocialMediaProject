package com.socialmedia.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.repository.enums.ERole;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtProvider {

    String audience = "socialmedia";
    String issuer = "bilgeadam";
    String secretKey = "secretKey";

    // id ve role bilgisi ile token üretmek için kullanılır.
    public Optional<String> createToken(Long id, ERole eRole){
        String token = null;
        Date date = new Date(System.currentTimeMillis() + (1000*60*5)); //tokenın geçerlilik süresi = 5dk
        try{
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())  // tokenın oluşturulduğu zamanı belirtir.
                    .withExpiresAt(date) // token geçerlilik süresi
                    .withClaim("id",id)
                    .withClaim("role", eRole.toString())
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Long> getIdFromToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            // Metoda verilen token bizim belirlemiş olduğumuz standartlara göre kontrol edilir.
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            // Token doğrulanırsa decode edilecektir.
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);  // == Optional<Long>
        }catch (Exception e){
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }

}
