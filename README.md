### 1- MongoDB denemesi 
    Veritabanına bir kayıt yapılmalıdır. Bu işlem için DTO ve Mapper kullanınız.
### 2- UserProfile update işlemi 
    Register işlemi auth service' den feign client aracılığıyla yapıldı. Ancak userprofile service' de kullanıcının bazı bilgileri eksiktir.
    Eksik olan bilgilerin update metodu ile kullanıcıya tamamlatılması gerekmektedir. Bu işlem için gerekli metodu yazınız.
### 3- Auth update işlemi
    Update işleminde değiştirilen verilerin de auth service' e gönderilerek update edilmesi gerekmektedir.
    Bunun için UserprofileService' den AuthService' e bir manager yazılmalıdır. Ancak userprofile' dan auth' a veriyi gönderirken eşleştirme yapabilmek için
    bazı ek property'lere ihtiyaç vardır. UserProfile' da ve Auth' da gelen giden verilerde değişiklik yapmak gerekebilir.
    Bu işlem için gerekli manager ve service metotlarını yazınız.
### 4- ConfigServer 
    ConfigServer adında bir modül oluşturarak service .yml dosylarının konfigürasyonunu tamamlayınız.
### 5- Auth Delete işlemi
    Kullanıcının sistemden silinmesi gerekmektedir. Bu işlem için bir delete metodunu yazınız. 
    Bu metot kullanıcının statüsünü değiştirerek DELETED yapmalıdır.
### 6- UserProfile Delete işlemi
    Auth service' de yapılan delete işleminin userprofile service' e iletilöesi gerekmektedir. Open feign kullanarak bu işlemi gerçekleştiriniz.
### 7- AuthService' de activateStatus metodu
    Bu metotta düzenleme yapmalısınız. Yalnızca durumu PENDING olan kullanıcılar kod kullanarak durumunu ACTIVE yapabilir.
    Ayrıca bu işlemin UserProfile Service' e de gönderilmesi gerekmektedir. Oradaki durumunda değiştirilmesi gerekmektedir.
### 8- AuthService' de forgotPassword
    Kullanıcı sisteme giriş yapmak için kullandığı şifresini unutabilir. Bu şifreyi istediği zaman sıfırlayabilmelidir.
    Bunun için forgotPassword adında bir metot yazınız. Bu şifre değişikliğini UserProfile Service' e de gönderiniz.
### 9- Register işleminin RabbitMQ ile yapılması
    AuthService'de rabbitmq kullanbilmek için gerekli katmanların oluşturularak servis metoduna producer sınıfının çağrılması gerekmektedir.
    Önce config --> rabbitmq.model --> rabbitmq.producer sıralamsı ile katmanları doldurunuz.
### 10- ForgotPassword işelminin RabbitMq ile yapılması
    Auth forgot password metodunu rabbitmq ile userprofile service'e aktarınız.
### 11- Kaydın maile gönderilmesi
    Kayıt oan kişinin username, email, activationCode ve password bilgileri mailine gönderilmelidir.
    Bu işlem için rabbitmq kullanınız.
    -   config(queue, binding belirlenecek) --> rabbitmq --> producer(queue belirlenecek, metot yazılacak) --> model(gönderilecek veriler belirlenecek
        --> service(producer metodunu çağırıp içerisine model verilecek)
        !! modele auth nesnesi mapplenmelidir(bknz. mapper)
### 12- UserProfile Service'te passwordChange
    Kullanıcının şifresini değiştirebildiği bir metot yazmalısınız. Burada değişen şifre OpenFeign aracılığıyla auth service'e gönderilmelidir.
    Şifre AuthService'de de setlenmelidir.


