package com.anhhoang.picrust;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.anhhoang.picrust.ui.recipes.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Created by anh.hoang on 10/2/17.
 */

@RunWith(AndroidJUnit4.class)
public class ActivitiesTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityTestRule = new ActivityTestRule<>(RecipesActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();

        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void shouldHaveItem() {
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(ViewMatchers.withText("Nutella Pie"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldNavigateToDetail() {
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.recipe_detail_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldNavigateToIngredients() {
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.recipe_detail_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.ingredients_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldNavigateToStep() {
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.recipe_detail_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        onView(ViewMatchers.withId(R.id.step_description_text_view))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }
}
