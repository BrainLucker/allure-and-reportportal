package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    final String notification = "Встреча успешно запланирована на ";
    final SelenideElement form = $(".form");

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
        form.$("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        form.$("[data-test-id=name] input").val(validUser.getName());
        form.$("[data-test-id=phone] input").val(validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").should(appear).shouldHave(text(notification + firstMeetingDate));
        // Повторная отправка формы с новой датой
        form.$("[data-test-id=date] input.input__control").doubleClick().sendKeys(secondMeetingDate);
        form.$$("button").find(exactText("Запланировать")).click();
        $$("[data-test-id=replan-notification] button").find(exactText("Перепланировать")).click();
        $("[data-test-id=success-notification] .notification__content").should(appear).shouldHave(text(notification + secondMeetingDate));
    }

    @Test
    @DisplayName("Should successful plan meeting if rare user's name")
    public void shouldSuccessfulPlanMeetingIfRareName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var MeetingDate = DataGenerator.generateDate(4);

        form.$("[data-test-id=city] input").val(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick().sendKeys(MeetingDate);
        form.$("[data-test-id=name] input").val("Алёна Алёхина");
        form.$("[data-test-id=phone] input").val(validUser.getPhone());
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").should(appear).shouldHave(text(notification + MeetingDate));
    }

    @Test
    @DisplayName("Should get error message if entered invalid phone number")
    public void shouldGetErrorIfInvalidPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var MeetingDate = DataGenerator.generateDate(4);

        form.$("[data-test-id=city] input").val(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick().sendKeys(MeetingDate);
        form.$("[data-test-id=name] input").val(validUser.getName());
        form.$("[data-test-id=phone] input").val(DataGenerator.generateWrongPhone("en"));
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$$("button").find(exactText("Запланировать")).click();
        form.$("[data-test-id=phone] .input__sub").shouldHave(text("Неверный формат номера мобильного телефона"));
    }
}