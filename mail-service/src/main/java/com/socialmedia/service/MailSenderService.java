package com.socialmedia.service;

import com.socialmedia.rabbitmq.model.MailForgotPassModel;
import com.socialmedia.rabbitmq.model.MailSenderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendRegisterUsersInfo(MailSenderModel mailSenderModel){
        // Alternatif MimeMessage araştırılabilir.
        System.out.println(mailSenderModel);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");  //sunucu olarak kullanılacak mail
        mailMessage.setTo(mailSenderModel.getEmail());  // kullanıcının girmiş olduğu mail
        mailMessage.setSubject("DOGRULAMA KODU");
        mailMessage.setText(
                "Tebrikler, başarıyla kayıt oldunuz. Giriş ve onay bilgileriniz aşağıdaki gibidir.\n"
                + "Kullanıcı adı: " + mailSenderModel.getUsername()
                + "\n Şifre: " + mailSenderModel.getPassword()
                + "\n Doğrulama kodu: " + mailSenderModel.getActivationCode()
        );
        javaMailSender.send(mailMessage);
    }

    public void sendForgotPassword(MailForgotPassModel mailForgotPassModel){
        System.out.println(mailForgotPassModel);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}"); //sunucu olarak kullanılacak mail
        mailMessage.setTo(mailForgotPassModel.getEmail()); //kullanıcının girmiş olduğu mail
        mailMessage.setSubject("ŞİFRE SIFIRLAMA MAİLİ");
        mailMessage.setText("Tebrikler, şifreniz başarıyla sıfırlanmıştır. \n"
                + "Kullanıcı adı: " + mailForgotPassModel.getUsername()
                + "\nŞifre: " + mailForgotPassModel.getRandomPassword()
                + "\nLütfen giriş yaptığınızda şifrenizi değiştiriniz."
        );
        javaMailSender.send(mailMessage);
    }


}
