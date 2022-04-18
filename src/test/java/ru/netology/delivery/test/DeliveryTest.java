package ru.netology.delivery.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.util.ScreenShooterReportPortalExtension;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.delivery.util.LoggingUtils.logInfo;

@ExtendWith({ScreenShooterReportPortalExtension.class})
public class DeliveryTest {
    private final String notification = "Встреча успешно запланирована на ";
    private final SelenideElement form = $(".form");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x800";
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    public void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(7);

        form.$("[data-test-id=city] input").val(validUser.getCity());
        logInfo("В поле ввода введен город " + validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        logInfo("В поле ввода введена дата встречи " + firstMeetingDate);
        form.$("[data-test-id=name] input").val(validUser.getName());
        logInfo("В поле ввода введены имя и фамилия " + validUser.getName());
        form.$("[data-test-id=phone] input").val(validUser.getPhone());
        logInfo("В поле ввода введен номер телефона " + validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        logInfo("Выполнен клик по чекбоксу");
        form.$$("button").find(exactText("Запланировать")).click();
        logInfo("Выполнен клик по кнопке 'Запланировать'");
        $("[data-test-id=success-notification] .notification__content")
                .should(appear).shouldHave(text(notification + firstMeetingDate));
        logInfo(notification + firstMeetingDate);
        // Повторная отправка формы с новой датой
        form.$("[data-test-id=date] input.input__control").doubleClick().sendKeys(secondMeetingDate);
        logInfo("В поле ввода введена новая дата встречи " + secondMeetingDate);
        form.$$("button").find(exactText("Запланировать")).click();
        logInfo("Выполнен клик по кнопке 'Запланировать'");
        $$("[data-test-id=replan-notification] button").find(exactText("Перепланировать")).click();
        logInfo("Выполнен клик по кнопке 'Перепланировать'");
        $("[data-test-id=success-notification] .notification__content")
                .should(appear).shouldHave(text(notification + secondMeetingDate));
        logInfo(notification + secondMeetingDate);
    }

    @Test
    @DisplayName("Should successful plan meeting if rare user's name")
    public void shouldSuccessfulPlanMeetingIfRareName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var MeetingDate = DataGenerator.generateDate(4);

        form.$("[data-test-id=city] input").val(validUser.getCity());
        logInfo("В поле ввода введен город " + validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick().sendKeys(MeetingDate);
        logInfo("В поле ввода введена дата встречи " + MeetingDate);
        form.$("[data-test-id=name] input").val("Алёна Алёхина");
        logInfo("В поле ввода введены имя и фамилия " + "Алёна Алёхина");
        form.$("[data-test-id=phone] input").val(validUser.getPhone());
        logInfo("В поле ввода введен номер телефона " + validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        logInfo("Выполнен клик по чекбоксу");
        form.$$("button").find(exactText("Запланировать")).click();
        logInfo("Выполнен клик по кнопке 'Запланировать'");
        $("[data-test-id=success-notification] .notification__content").should(appear).shouldHave(text(notification + MeetingDate));
        logInfo(notification + MeetingDate);
    }

    @Test
    @DisplayName("Should get error message if entered invalid phone number")
    public void shouldGetErrorIfInvalidPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var MeetingDate = DataGenerator.generateDate(4);

        form.$("[data-test-id=city] input").val(validUser.getCity());
        logInfo("В поле ввода введен город " + validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick().sendKeys(MeetingDate);
        logInfo("В поле ввода введена дата встречи " + MeetingDate);
        form.$("[data-test-id=name] input").val(validUser.getName());
        logInfo("В поле ввода введены имя и фамилия " + validUser.getName());
        form.$("[data-test-id=phone] input").val(DataGenerator.generateWrongPhone("en"));
        logInfo("В поле ввода введен номер телефона " + validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        logInfo("Выполнен клик по чекбоксу");
        form.$$("button").find(exactText("Запланировать")).click();
        logInfo("Выполнен клик по кнопке 'Запланировать'");
        form.$("[data-test-id=phone] .input__sub").shouldHave(text("Неверный формат номера мобильного телефона"));
        logInfo("Появляется предупреждение: " + "Неверный формат номера мобильного телефона");
    }
}