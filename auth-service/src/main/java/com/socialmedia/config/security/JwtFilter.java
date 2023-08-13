package com.socialmedia.config.security;


import com.socialmedia.exception.AuthManagerException;
import com.socialmedia.exception.ErrorType;
import com.socialmedia.utility.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

// OncePerRequestFilter --> her bir istek için yalnızca bir kez çalışarak bir filtre uygulamamızı sağlayan Spring Framework sınıfıdır.
public class JwtFilter extends OncePerRequestFilter {

    // Spring ayağa kalktığında ilgili sınıftan oluşturulan bir bean varsa bunu arka tarafta yakalayıp burada kullanmamızı sağlar
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtUserDetails jwtUserDetails;
    /**
     * HttpServlet --> Java Servlet API'si tarafından sağlanan bir sınıftır ve HTTP protokolü üzerinden gelen istekleri işlemek için kullanılır.
     * Servletler, dinamik web uygulamaları geliştirmek için kullanılan Java sınıflarıdır ve web sunucusunda çalışır.
     * HTTP protokolüne özgü istekleri (GET, POST, PUT, DELETE vb.) işleyebilme yeteneği sağlar.
     *
     *
     * HttpServletRequest --> Gelen HTTP isteğini temsil eden bir HttpServletRequest nesnesidir.
     *                        Bu nesne, istemci tarafından sunucuya gönderilen isteği içeren bilgileri içerir.
     *                        Örneğin istek başlıkları, parametreler, yol ve HTTP yöntemi.
     *
     * HttpServletResponse --> Cevabı alır.
     * FilterChain --> Filtre zinciri içindeki bir sonraki filtreye veya hedef kaynağa isteği iletmek için kullanılan bir FilterChain nesnesidir.
     *                 Filtre zinciri, isteği işlemek ve yanıt üretmek için bir dizi filtreyi içeren bir yapıdır.
     *
     * Bu parametreler, doFilterInternal metodunun filtreleme işlemini gerçekleştirmek için gelen isteği alması,
     * isteği işlemesi ve yanıtı ileterek devam etmesi için gerekli olan temel bileşenleri temsil eder.
     */


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7); // Bearer 'dan sonrasına abone ol
            Optional<Long> id = jwtProvider.getIdFromToken(token);
            if (id.isPresent()){
                // loadUserByUserId metodunu çağırarak kullanıcı kimliğine(id) göre kullanıcı ayrıntılarını(UserDetails) alır.
                // Bu ayrıntılar, kullanıcının kimlik bilgilerini, rollerini ve yetkilerinden oluşmaktadır.
                // Bu ayrıntıları
                UserDetails userDetails = jwtUserDetails.loadUserByUserId(id.get());
                // Kimlik doğrulama token'ını(authenticationToken) oluşturur.
                // Bu token, kimlik doğrulama işlemlerinde kullanılır.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                // Bu, kimlik doğrulamasının gerçekleştirildiğini ve kimlik bilgilerinin doğru olduğunu belirtir.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else {
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
        }
        // Filtre zincirindeki bir sonraki filtreye veya hedef kaynağa isteği iletmek için filterChain'i kullanır.
        // bu, filtreleme işlemini tamamlar ve sonucunda isteğin devam etmesini sağlar.
        filterChain.doFilter(request,response);
        /**
         * Bu kod parçası, JWT'nin geçerliliğini doğrulamak için kullanıcı kimliği ve rollerini kontrol eder.
         * Geçerli bir JWT varsa, kullanıcının kimlik bilgileri ve yetkileriyle birlikte kimlik doğrulaması gerçekleştirilir
         * ve bu bilgiler güvenlik bağlamına yerleştirilir.
         * Son olarak, filtre zincirindeki bir sonraki adıma geçilir ve istek devam eder.
         */
    }
}
