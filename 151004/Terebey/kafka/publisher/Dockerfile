# Используем базовый образ с поддержкой Java
FROM openjdk:17-jdk

# Установка рабочей директории внутри контейнера
WORKDIR /app

# Копируем JAR-файл вашего приложения в контейнер
COPY build/libs/publisher-0.0.1-SNAPSHOT.jar /app/publisher.jar

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "publisher.jar"]
