package com.example.android.testing.espresso.web.BasicSample;

import static androidx.test.espresso.web.sugar.Web.onWebView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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

    @Test
    public void whenUserEntersText_AndTapsOnChange_ThenTextIsChangedInANewPage() {

    }
}
