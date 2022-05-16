package com.example.android.testing.espresso.web.BasicSample;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static androidx.test.espresso.web.sugar.Web.onWebView;
import static androidx.test.espresso.web.webdriver.DriverAtoms.clearElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.findElement;
import static androidx.test.espresso.web.webdriver.DriverAtoms.getText;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webClick;
import static androidx.test.espresso.web.webdriver.DriverAtoms.webKeys;
import static org.hamcrest.Matchers.containsString;

import android.content.Intent;

import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WebViewPracticeTest {

    @Rule
    public ActivityTestRule<WebViewActivity> activityScenarioRule = new ActivityTestRule<WebViewActivity>(WebViewActivity.class, false, false) {
        @Override
        protected void afterActivityLaunched() {
            onWebView().forceJavascriptEnabled();
        }
    };

    @Before
    public void launchActivity() {
        activityScenarioRule.launchActivity(withWebFormIntent());
    }

    public Intent withWebFormIntent() {
        Intent basicFormIntent = new Intent();
        basicFormIntent.putExtra(WebViewActivity.KEY_URL_TO_LOAD, WebViewActivity.WEB_FORM_URL);
        return basicFormIntent;
    }

    @Test
    public void whenUserEntersText_AndTapsOnChange_ThenTextIsChangedInANewPage() {
        String text = "Peekaboo";
        onWebView(withId(R.id.web_view))
                .withElement(findElement(Locator.ID, "text_input"))
                .perform(clearElement())
                .perform(webKeys(text))
                .withElement(findElement(Locator.ID, "submitBtn"))
                .perform(webClick())
                .withElement(findElement(Locator.ID, "response"))
                .check(webMatches(getText(), containsString(text)));

    }
}
