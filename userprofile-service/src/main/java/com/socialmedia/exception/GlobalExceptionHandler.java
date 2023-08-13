package com.socialmedia.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ErrorMessage createError(ErrorType errorType, Exception exception) {
        return ErrorMessage.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }

    @ExceptionHandler(UserProfileManagerException.class)
    @ResponseBody //JSON veya XML verilerin dönüşlerinin nasıl olacağını belirtir
    public ResponseEntity<ErrorMessage> handleManagerException(UserProfileManagerException exception){
        ErrorType errorType = exception.getErrorType();
        HttpStatus httpStatus = errorType.httpStatus;
        return new ResponseEntity<>(createError(errorType,exception),httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllExceptions(Exception exception) {
        ErrorType errorType = ErrorType.INTERNAL_ERROR;
        List<String> fields = new ArrayList<>();
        fields.add(exception.getMessage());
        ErrorMessage errorMessage = createError(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.ok("Beklenmeyen bir hata olustu: " + ex.getMessage());
    }

    //İstek atılırken arka tarafta belirlenen property'lerin validasyon kontrollerini dikkate alır.
    //Aşağıdaki metot bu validasyonların tipini ve mesajını döndürmek üzere tasarlanmıştır.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorType errorType = ErrorType.VALIDATION_ERROR;
        List<String> fields = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(e -> fields.add(e.getField() + ": " + e.getDefaultMessage()));
        ErrorMessage errorMessage = createError(errorType, exception);
        errorMessage.setFields(fields);
        return new ResponseEntity<>(errorMessage, errorType.getHttpStatus());
    }

    //Http istediğinin veya yanıtının okunamaması durumunda fırlatılan hatadır.
    //JSON formatta bir veri gönderirken bu formattaki bir yanlışlık sonucunda(örn; fazladan virgül kalması, bir değerin eksik yazılması)
    //fırlatılan hatadır.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorMessage> handleMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    //Veri dönüşümü sırasında fırlatılan hatadır.
    //Bir parametre Date formatında bir veri beklerken, String veya Integer tipinde bir veri gönderildiğinde
    //Veri dönüşümü olmayacağı için bu hata fırlatılır.
    @ExceptionHandler(InvalidFormatException.class)
    public final ResponseEntity<ErrorMessage> handleInvalidFormatException(InvalidFormatException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    //İki parametrenin tiplerinin birbiriyle uyuşmamasından ortaya çıkan hatadır.
    //String tutulan bir parametrenin(değişken) Integer olarak gönderilmesi sırasında fırlatılan hatadır.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(MethodArgumentTypeMismatchException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    //@PathVariable eksik olduğunda fırlatılacak exception
    //@PathVariable --> auth/find-by-id/{id}
    @ExceptionHandler(MissingPathVariableException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentMisMatchException(MissingPathVariableException exception) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }

    //Bir veritabında uniqe olan bir sütunda aynı değerden birden fazla var ise fırlatılacak hatadır.
    //Yani username property' si veritabanına uniqe olarak işaretlendiyse ve bir değere(örn; java8) sahipse
    //bu değerden bir tane daha olmamalıdır. Eğer siz el ile bu değerden bir tane daha eklerseniz veriyi çekmek
    //istediğinizde DataIntegrityViolation hatası alırsınız.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorMessage> handlePsqlException(DataIntegrityViolationException exception) {
        ErrorType errorType = ErrorType.USERNAME_DUPLICATE;
        return new ResponseEntity<>(createError(errorType, exception), errorType.getHttpStatus());
    }
}
