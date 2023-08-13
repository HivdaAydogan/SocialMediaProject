package com.socialmedia.controller;

import com.socialmedia.dto.request.PasswordChangeRequestDto;
import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import com.socialmedia.dto.request.UserUpdateRequestDto;
import com.socialmedia.repository.entity.UserProfile;
import com.socialmedia.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.socialmedia.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER_PROFILE)
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/find-all")
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @PostMapping(CREATE_USER)
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    //@RequestParam -->
    //@PathVariable -->,

    //Delete, Post, Put Mapping farkları ?
    //Neden Delete, Post, Put mapping vardır diye sorduğumuzda tek fark controller metotlarını
    //işaretlerken okunabilirliği arttırmaktır.
    //Genellikle PostMapping --> veri kaydetme, veri tabanında değişiklik yapma işlemlerinde,
    //           PutMapping --> veri tabanındaki varolan veriyi değiştirme(update) işlemlerinde,
    //           DeleteMapping --> silme işlemlerinde kullanılır.
    @PutMapping(UPDATE)
    @Operation(summary = "kullanıcının giriş yaptıktan sonra eksik bilgilerini doldurduğu metot")
    public ResponseEntity<Boolean> updateUser(String token, @RequestBody UserUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.updateUser(token,dto));
    }
    @Hidden
    @DeleteMapping("/delete-by-id/{authId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.deleteById(authId));
    }

    @Hidden
    @PutMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @Hidden
    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserSetPasswordRequestDto dto){
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }

    @PutMapping(PASSWORD_CHANGE)
    public ResponseEntity<Boolean> passwordChange(PasswordChangeRequestDto dto){
        return ResponseEntity.ok(userProfileService.passwordChange(dto));
    }
}
