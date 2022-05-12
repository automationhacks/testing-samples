package com.example.android.testing.espresso.IdlingResourceSample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChangeTextIdlingResourcePracticeTest {
    // We create a variable to hold an idling resource instance
    private IdlingResource mIdlingResource;

    /**
     * Before a test executes, we get idling resource from activity and register it into
     * the IdlingRegistry
     */
    @Before
    public void registerIdlingResource() {
        // We use ActivityScenario to launch and get access to our MainActivity
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        // activityScenario.onActivity provides a thread safe mechanism to access the activity
        // We pass the activity as a lambda and then register it into idling registry
        activityScenario.onActivity((ActivityScenario.ActivityAction<MainActivity>) activity -> {
            mIdlingResource = activity.getIdlingResource();
            IdlingRegistry.getInstance().register(mIdlingResource);
        });
    }

    @Test
    public void whenUserEntersTextAndTapsOnChangeText_ThenTextChangesWithADelay() {
        // Type text in text box with type something ...
        String text = "This is gonna take some time";
        onView(withId(R.id.editTextUserInput)).perform(typeText(text), closeSoftKeyboard());

        // Tap on "Change text taking some time" button
        onView(withId(R.id.changeTextBt)).perform(click());

        // Assert the entered text is displayed on the screen
        onView(withId(R.id.textToBeChanged)).check(matches(withText(text)));

        /* NOTE: We'll get below error if we try to run this test without idling resources
         * androidx.test.espresso.base.AssertionErrorHandler$AssertionFailedWithCauseError: 'an instance of android.widget.TextView and view.getText()
         * with or without transformation to match: is "This is gonna take some time"' doesn't match the selected view.
           Expected: an instance of android.widget.TextView and view.getText() with or without transformation to match: is "This is gonna take some time"
           Got: view.getText() was "Waiting for message…"
         */
    }

    @After
    public void unregisterIdlingResource() {
        /**
         * After the test has finished, we unregister this idling resource
         * from the IdlingRegistry
         */
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
