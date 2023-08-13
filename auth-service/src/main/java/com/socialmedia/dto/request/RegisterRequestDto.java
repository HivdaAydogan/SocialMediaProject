package com.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDto {
    @NotEmpty(message = "Ad alanını boş bırakmayınız.")
    @Size(min = 3, max = 20, message = "Adınız en az 2 en fazla 20 karakter olabilir.")
    private String name;
    @NotEmpty(message = "Soyadı alanını boş bırakmayınız.")
    @Size(min = 3, max = 20, message = "Soyadınız en az 2 en fazla 20 karakter olabilir.")
    private String surname;
    @NotEmpty(message = "Kullanıcı adını boş bırakmayınız.")
    @Size(min = 3, max = 20, message = "Kullanıcı adı en az 3 en fazla 20 karakter olabilir.")
    private String username;
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;

    //@NotBlank --> username = ""
    //@NotNull  --> username == null (swaggerdan veri silinmiş)
    @NotEmpty // Yukarıdaki her ikisini bir den kontrol eden anotasyondur.
    @Size(min = 8, max = 32, message = "Şifre en az 8 en çok 32 karakter olabilir.")
    private String password;
    private String rePassword;
}
