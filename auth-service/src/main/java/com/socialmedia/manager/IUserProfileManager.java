package com.socialmedia.manager;

import com.socialmedia.dto.request.UserCreateRequestDto;
import com.socialmedia.dto.request.UserSetPasswordRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "${openfeign.user-manager-url}", name = "auth-userprofile")
public interface IUserProfileManager {

    //Buradaki metodun, user-profile'daki metodun dönüş tipiyle aynı olması gerekmektedir.
    //Ancak metot ismi farklı olabilir. Ama aynı olması okunabilirlik açısından daha iyidir.
    //Metodun parametresinde bulunan veri gönderme tipi(@RequestBOdy, @RequestParam vb.) birebir aynı olmalıdır.
    //Metodun dto parametresinin ismiyle userprofile controller metodundaki parametre isminin aynı olması okunabilirlik açısından önemlidir.
    //Dto'ların içerisindeki property'ler de aynı olması zorunludur.
    @PostMapping("/create-user") //http://localhost:8080/user-profile/create-user
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto);

    //OpenFeign ile gönderilen parametrelerin mutlaka bir gönderi tipiyle(@RequestParam, @PAthVariable, @RequestBody)
    //işaretlenmesi gerekmektedir.
    @DeleteMapping("/delete-by-id/{authId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long authId);

    @PutMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @PutMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserSetPasswordRequestDto dto);
}
