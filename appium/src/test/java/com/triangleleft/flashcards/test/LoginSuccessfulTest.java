package com.triangleleft.flashcards.test;

import com.triangleleft.flashcards.page.LoginPage;
import com.triangleleft.flashcards.page.MainPage;
import com.triangleleft.flashcards.rule.AppiumRule;
import com.triangleleft.flashcards.rule.MockWebServerRule;
import com.triangleleft.flashcards.util.MockJsonResponse;
import com.triangleleft.flashcards.util.MockServerResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.triangleleft.flashcards.util.TestUtils.isDisplayed;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class LoginSuccessfulTest {

    public static final String LOGIN = "login";

    private MockWebServerRule webServerRule = new MockWebServerRule();
    private AppiumRule appium = new AppiumRule(true);

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(webServerRule).around(appium);

    @Test
    @MockJsonResponse("login/valid_response.json")
    public void successfulLogin() {
        // After login, we immediately request userdata
        webServerRule.getWebServer().enqueue(MockServerResponse.make("userdata/userdata_french.json"));
        LoginPage loginPage = appium.getApp().loginPage();
        //loginPage.login(LOGIN, "password");

        MainPage mainPage = appium.getApp().mainPage();
        assertThat(mainPage.title, isDisplayed());
    }

}
