/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.espresso.DataAdapterSample;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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

/**
 * Tests to verify that the behavior of {@link LongListActivity} is correct.
 * <p>
 * Note that in order to scroll the list you shouldn't use {@link ViewActions#scrollTo()} as
 * {@link Espresso#onData(org.hamcrest.Matcher)} handles scrolling.</p>
 *
 * @see #onRow(String)
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LongListActivityTest {

    private static final String TEXT_ITEM_30 = "item: 30";

    private static final String TEXT_ITEM_30_SELECTED = "30";

    private static final String TEXT_ITEM_60 = "item: 60";

    // Match the last item by matching its text.
    private static final String LAST_ITEM_ID = "item: 99";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test. This is a
     * replacement for ActivityTestRule.
     */
    @Rule
    public ActivityScenarioRule<LongListActivity> rule = new ActivityScenarioRule<>(
        LongListActivity.class);

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    public void lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        onView(withText(LAST_ITEM_ID)).check(doesNotExist());
    }

    /**
     * Check that the item is created. onData() takes care of scrolling.
     */
    @Test
    public void list_Scrolls() {
        onRow(LAST_ITEM_ID).check(matches(isCompletelyDisplayed()));
    }

    /**
     * Clicks on a row and checks that the activity detected the click.
     */
    @Test
    public void row_Click() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());

        // Check that the activity detected the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
    }

    /**
     * Checks that a toggle button is checked after clicking on it.
     */
    @Test
    public void toggle_Click() {
        // Click on a toggle button.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).perform(click());

        // Check that the toggle button is checked.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).check(matches(isChecked()));
    }

    /**
     * Make sure that clicking on the toggle button doesn't trigger a click on the row.
     */
    @Test
    public void toggle_ClickDoesntPropagate() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());

        // Click on the toggle button, in a different row.
        onRow(TEXT_ITEM_60).onChildView(withId(R.id.rowToggleButton)).perform(click());

        // Check that the activity didn't detect the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
    }

    /**
     * Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param str the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    private static DataInteraction onRow(String str) {
        return onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is(str)));
    }
}