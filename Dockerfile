# docker tarafından kullanılacak JDK sürümünü belirtir.
FROM amazoncorretto:17
# JAR_FILE --> jar dosyassının path yolunun dinamik olarak verilebilmesini sağlar.
ARG JAR_FILE=target/*.jar
# JAR dosyalarının bilgisayarda bir kopyasını üretmek için, verilmediğinde de çalışabilir.
COPY ${JAR_FILE} application.jar
# dockerfile'ın hangi parametreleri alarak çalışacağını belirler.
ENTRYPOINT ["java","-Xmx2048M","-jar","/application.jar"]
