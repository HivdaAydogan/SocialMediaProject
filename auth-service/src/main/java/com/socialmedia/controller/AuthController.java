package com.socialmedia.controller;

import com.socialmedia.dto.request.*;
import com.socialmedia.dto.response.RegisterResponseDto;
import com.socialmedia.repository.entity.Auth;
import com.socialmedia.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.socialmedia.constant.ApiUrls.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER + "-with-rabbitmq")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMQ(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMQ(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @Hidden
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateAuth(@RequestBody AuthUpdateRequestDto dto){
        return ResponseEntity.ok(authService.updateAuth(dto));
    }

    //@RequestBody, @RequestParam, @PathVariable  --> request ve response tipleridir.
    //@RequestParam, @PathVariable genellikle @GetMapping ile kullanılırlar. Ama bu sadece @GetMapping ile kullanıldıkları anlamına gelmez.
    @GetMapping(FIND_BY_ID)
    //hiçbir tip belirlemezseniz default olarak @RequestParam çalışır
    //http://localhost:8090/auth/find-by-id?id=2
    public ResponseEntity<Auth> findByIdParam(@RequestParam Long id){
        return ResponseEntity.ok(authService.findById(id).get());
    }
    @GetMapping(FIND_BY_ID + "/{id}")
    //http://localhost:8090/auth/find-by-id/2
    public ResponseEntity<Auth> findByIdPathVariable(@PathVariable Long id){
        return ResponseEntity.ok(authService.findById(id).get());
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @DeleteMapping(DELETE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> delete(String token){
        return ResponseEntity.ok(authService.delete(token));
    }

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto dto){
        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    @Hidden
    @PutMapping("/password-change")
    public ResponseEntity<Boolean> passwordChange(@RequestBody ToAuthPasswordChangeRequestDto dto){
        return ResponseEntity.ok(authService.passwordChange(dto));
    }

    @PostMapping(FORGOT_PASSWORD + "-with-rabbitmq")
    public ResponseEntity<String> forgotPasswordWithRabbitMq(@RequestBody ForgotPasswordRequestDto dto){
        return ResponseEntity.ok(authService.forgotPasswordWithRabbitMq(dto));
    }
}
