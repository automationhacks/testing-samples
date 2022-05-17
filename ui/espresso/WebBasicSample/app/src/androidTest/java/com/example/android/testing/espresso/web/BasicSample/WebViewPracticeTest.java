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


    /**
     * Creates a test rule. Enables JS on the web view to enable espresso to drive the mobile WebView
     */
    @Rule
    public ActivityTestRule<WebViewActivity> mTestRule = new ActivityTestRule<WebViewActivity>(WebViewActivity.class, false, false) {
        @Override
        protected void afterActivityLaunched() {
            onWebView().forceJavascriptEnabled();
        }
    };

    /**
     * We launch our activity with an intent that has the URL and the form that we
     * want to load
     */
    @Before
    public void launchActivity() {
        mTestRule.launchActivity(withWebFormIntent());
    }

    public Intent withWebFormIntent() {
        Intent basicFormIntent = new Intent();
        basicFormIntent.putExtra(WebViewActivity.KEY_URL_TO_LOAD, WebViewActivity.WEB_FORM_URL);
        return basicFormIntent;
    }

    @Test
    public void whenUserEntersText_AndTapsOnChangeTextAndSubmit_ThenTextIsChangedInANewPage() {
        String text = "Peekaboo";
        // We start our test by finding the WebView we want to work with
        onWebView(withId(R.id.web_view))
                // Find the text box using id
                .withElement(findElement(Locator.ID, "text_input"))
                // Clear any existing text to ensure predictable result
                .perform(clearElement())
                // Type text
                .perform(webKeys(text))
                // Find submit button
                .withElement(findElement(Locator.ID, "submitBtn"))
                // Click on submit button
                .perform(webClick())
                // Find the element where the changed text is displayed
                .withElement(findElement(Locator.ID, "response"))
                // Verify the text for the element has our initially entered text
                .check(webMatches(getText(), containsString(text)));
    }

    /**
     * This test repeats steps as above, it just clicks on change text button and verifies the
     * result text is updated on the same page
     */
    @Test
    public void whenUserEntersText_AndTapsOnChange_ThenTextIsChangedInANewPage() {
        String text = "Peekaboo";
        onWebView(withId(R.id.web_view))
                .withElement(findElement(Locator.ID, "text_input"))
                .perform(clearElement())
                .perform(webKeys(text))
                .withElement(findElement(Locator.ID, "changeTextBtn"))
                .perform(webClick())
                .withElement(findElement(Locator.ID, "message"))
                .check(webMatches(getText(), containsString(text)));
    }
}
