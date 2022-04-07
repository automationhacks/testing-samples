package com.example.android.testing.espresso.RecyclerViewSample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewSamplePracticeTest {

  @Rule
  public ActivityScenarioRule activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

  /**
   * This is a negative test that tries to scroll to a descendant that does not exist in the app We
   * use Junit @Test annotation to verify that this test throws a PerformException
   */
  @Test(expected = PerformException.class)
  public void whenAppOpens_ThenItemWithTextIsNotVisible() {
    onView(withId(R.id.recyclerView))
        // Here scrollTo will fail if there are no items that match expected descriptions
        .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("not on the list"))));
  }

  /**
   * Test to scroll in a recycler view to an item at a fixed position And verify that the element
   * with expected text is displayed
   */
  @Test
  public void whenScrollToItemAtAPosition_ThenItemIsDisplayed() {
    int itemBelowFold = 40;
    onView(withId(R.id.recyclerView))
        .perform(RecyclerViewActions.actionOnItemAtPosition(itemBelowFold, click()));

    String expectedText = String.format("This is element #%d", itemBelowFold);
    onView(withText(expectedText)).check(matches(isDisplayed()));
  }

  /**
   * This test scrolls in recycler view till it finds an element with text: "This is the middle!" It
   * uses a field already set in the View holder implementation to determine it has reached the
   * point And a custom hamcrest matcher
   */
  @Test
  public void whenScrollToItemInTheMiddle_ThenCheckItemWithSpecialTextIsDisplayed() {
    onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()));

    onView(withText("This is the middle!")).check(matches(isDisplayed()));
  }

  private static TypeSafeMatcher<CustomAdapter.ViewHolder> isInTheMiddle() {
    return new TypeSafeMatcher<CustomAdapter.ViewHolder>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("item in the middle");
      }

      @Override
      protected boolean matchesSafely(CustomAdapter.ViewHolder viewHolder) {
        return viewHolder.getIsInTheMiddle();
      }
    };
  }
}
