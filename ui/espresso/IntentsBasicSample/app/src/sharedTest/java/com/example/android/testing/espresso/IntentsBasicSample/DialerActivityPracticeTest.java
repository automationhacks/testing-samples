package com.example.android.testing.espresso.IntentsBasicSample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DialerActivityPracticeTest {
  public static final String VALID_PHONE_NUMBER = "123-456-7898";

  @Rule
  public GrantPermissionRule grantPermissionRule =
      GrantPermissionRule.grant("android.permission.CALL_PHONE");

  @Rule
  public ActivityScenarioRule<DialerActivity> testRule =
      new ActivityScenarioRule<>(DialerActivity.class);

  @Before
  public void setupIntents() {
    // Initializes intents and begins recording intents, must be called before
    // triggering any actions that call an intent that we want to verify with validation or stubbing
    init();

    /* Espresso does not stub any intents, with below method all external intents would be stubbed */
    intending(not(isInternal()))
        .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
  }

  @After
  public void teardownIntents() {
    // Clears intent state, must be called after each test case
    release();
  }

  /** Test to enter a phone number and make a call and verify an intent is launched */
  @Test
  public void whenTapOnCallNumber_ThenOutgoingCallIsMade() {
    // Type a phone no and close keyboard
    Uri phoneNumber = Uri.parse("tel:" + VALID_PHONE_NUMBER);
    onView(withId(R.id.edit_text_caller_number))
        .perform(typeText(VALID_PHONE_NUMBER), closeSoftKeyboard());

    // Tap on call number button
    onView(withId(R.id.button_call_number)).perform(click());

    // Verify an intent is called with action and phone no and package
    intended(allOf(hasAction(Intent.ACTION_CALL), hasData(phoneNumber)));
  }

  @Test
  public void whenPickNumber_AndTapOnCallWithStub_ThenStubbedResponseIsReturned() {
    // To stub all intents to contacts activity to return a valid phone number
    // we use intending() and verify if component has class name ContactsActivity
    // then we responding with valid result
    intending(hasComponent(hasShortClassName(".ContactsActivity")))
        .respondWith(
            new Instrumentation.ActivityResult(
                Activity.RESULT_OK, ContactsActivity.createResultData(VALID_PHONE_NUMBER)));

    onView(withId(R.id.button_pick_contact)).perform(click());
    onView(withId(R.id.edit_text_caller_number)).check(matches(withText(VALID_PHONE_NUMBER)));
  }
}
