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

import androidx.test.espresso.web.webdriver.Locator;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WebViewPracticeTest {
    // Launch activity with ActivityScenarioRule
    @Rule
    public ActivityScenarioRule<WebViewActivity> mActivityScenarioRule = new ActivityScenarioRule<>(WebViewActivity.class);

    @Before
    public void enableJSOnWebView() {
        // We enable Javascript execution on the webview
        onWebView().forceJavascriptEnabled();
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
