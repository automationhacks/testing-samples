package com.example.android.testing.espresso.DataAdapterSample;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class DataAdapterPractice {
    @Rule
    public ActivityScenarioRule<LongListActivity> rule = new ActivityScenarioRule<>(LongListActivity.class);

    @Test
    public void whenAppOpens_ThenLastItemIsNotDisplayed() {
        onView(withText("item: 99")).check(doesNotExist());
    }

    @Test
    public void whenScrollToLastItem_ThenLastItemIsDisplayed() {
        // We use onData since we want to scroll to an item in the list view
        // we use hasEntry matcher that takes two args, first the item check
        // and second the value
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is("item: 99")))
                // Then we check that this entry is displayed
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void whenClickOnRow_ThenTheTextIsUpdated() {
        String itemToClickOn = "item: 30";
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is(itemToClickOn)))
                // To click on an element in the list use `onChildView`
                .onChildView(withId(R.id.rowContentTextView)).perform(click());

        // Now that we are on desired item, we can verify using onView method
        String expectedValueAfterClick = "30";
        onView(withId(R.id.selection_row_value)).check(matches(withText(expectedValueAfterClick)));
    }

    @Test
    public void whenTapOnToggle_ThenStateIsChecked() {
        // Scroll to item: 30 in the ListView and tap on toggle button
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is("item: 30")))
                .onChildView(withId(R.id.rowToggleButton))
                .perform(click());

        // Check the text is updated as ON after the click
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is("item: 30")))
                .onChildView(withId(R.id.rowToggleButton))
                .check(matches(isChecked()));
    }

    @Test
    public void whenTapOnToggle_ThenPreviouslyTappedRowIsNotUpdated() {
        // Scroll to item 30 and click on the row so that selected text view gets updated
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is("item: 30")))
                .onChildView(withId(R.id.rowContentTextView))
                .perform(click());

        // Now, scroll to item 60 and tap on toggle button
        onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is("item: 60")))
                .onChildView(withId(R.id.rowToggleButton))
                .perform(click());

        // Check the previously selected text view is not updated to item 60
        onView(withId(R.id.selection_row_value)).check(matches(withText("30")));
    }
}

