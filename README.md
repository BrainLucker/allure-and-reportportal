#### Домашнее задание к занятию "4.1. Репортинг"

## Задание 1. "Проснулись" (Allure)

[Report](https://i.imgur.com/X0Bd8t5.png)

## Задание 2. Report Portal

[Report](https://i.imgur.com/p58TcLM.png), [Test Logs](https://i.imgur.com/1XaGjPP.png)

### Мануал по интеграции Report Portal в проект
0. Для запуска системы потребуются установленные Docker и Docker Compose.
   Оптичмальные технические требования можно посмотреть [тут](https://reportportal.io/docs/Optimal-Performance-Hardware).
   Для загрузки контейнеров через Docker может потребоваться VPN (запущенный в ОС).

1. Для Gradle-проекта на JUnit5 (остальные см. [тут](https://reportportal.io/installation)) в **_build.gradle_**
   добавить репозиторий `mavenLocal()` и следующие зависимости:
   https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/build.gradle#L33-L35
   Для автоопределения расширений и задания необходимого уровня логирования в блоке `test` добавить строки:
   https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/build.gradle#L41-L42
2. Создать папку **_/META-INF/services_** в директории **_src/test/resources_** и поместить туда файл [org.junit.jupiter.api.extension.Extension](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension).
3. Подключить к проекту логгер, положив в директорию **_src/test/resources_** готовый файл [logback.xml](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/src/test/resources/logback.xml). Сюда же добавить файл
   [reportportal.properties](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/src/test/resources/reportportal.properties)
   – потом его нужно будет заполнить данными.
4. Для выполнения скриншотов дополнительно понадобятся классы
   [LoggingUtils](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/src/test/java/ru/netology/delivery/util/LoggingUtils.java)
   и [ScreenShooterReportPortalExtension](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/src/test/java/ru/netology/delivery/util/ScreenShooterReportPortalExtension.java).
6. Над тестовым классом необходимо поставить аннотацию `@ExtendWith({ScreenShooterReportPortalExtension.class})`.
   Для записи в лог информации о произведенных действиях в тесты можно добавить строки типа `logInfo("В поле ввода введено значение " + testvalue);`.
   Добавить требуемые импорты.
6. [Скачать](https://raw.githubusercontent.com/reportportal/reportportal/master/docker-compose.yml) или взять файл [docker-compose.yml](https://github.com/BrainLucker/allure-and-reportportal/blob/32b1d0c48ed1098321c110d34bd8bd91276102a3/docker-compose.yml) и поместить в проект.
   Теперь необходимо настроить его для запуска на определенной ОС, [раскомментировав](https://reportportal.io/docs/Deploy-ReportPortal-with)
   соответствующие строки.
7. Открыть терминал в корне проекта и запустить приложение командой:

   `docker-compose -p reportportal up -d --force-recreate`

   Дождаться скачивания и создания всех необходимых контейнеров.
8. Открыть ReportPortal в браузере: по-умолчанию по адресу http://localhost:8080/. Войти под админской учетной записью: superadmin; erebus.
9. В интерфейсе системы зайти в профиль пользователя и скопировать требуемые настройки (4 первые строчки). Вставить эти настройки в файл **_reportportal.properties_**.
10. Далее можно запускать SUT и тесты. Посмотреть их результаты и историю прогонов можно через браузер, зайдя в раздел «Launches».