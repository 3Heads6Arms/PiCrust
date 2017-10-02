package com.anhhoang.picrust;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.local.PiCrustDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by anh.hoang on 9/22/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    private final Recipe EXPECTED_RECIPE = new Recipe(1, "Nutella Pie", 8, null);
    private final Ingredient EXPECTED_INGREDIENT_1 = new Ingredient(2, "CUP", "Graham Cracker crumbs", 1);
    private final Ingredient EXPECTED_INGREDIENT_2 = new Ingredient(6, "TBLSP", "unsalted butter, melted", 1);
    private final Step EXPECTED_STEP_1 = new Step(0, "Recipe introduction", "Recipe Introduction", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", null, 1);
    private final Step EXPECTED_STEP_2 = new Step(1, "Starting prep", "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.", null, null, 1);

    private PiCrustDatabase piCrustDatabase;

    @Before
    public void init() {
        piCrustDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                PiCrustDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @Test
    public void shouldInsertTestAndGet() {
        piCrustDatabase.recipesDao().insert(EXPECTED_RECIPE);

        List<RecipeModel> recipes = piCrustDatabase.recipesDao().getAll();
        RecipeModel recipe = piCrustDatabase.recipesDao().get(EXPECTED_RECIPE.getId());

        assertThat(recipes.size(), is(1));
        assertNotNull(recipe);
        assertRecipe(recipe);
    }

    @Test
    public void shouldInsertRecipeWithIngredientsAndSteps() {
        piCrustDatabase.recipesDao().insert(EXPECTED_RECIPE);
        piCrustDatabase.ingredientsDao().insert(EXPECTED_INGREDIENT_1, EXPECTED_INGREDIENT_2);
        piCrustDatabase.stepsDao().insert(EXPECTED_STEP_1, EXPECTED_STEP_2);

        RecipeModel recipe = piCrustDatabase.recipesDao().get(1);
        assertNotNull(recipe);
        assertNotNull(recipe.ingredients);
        assertThat(recipe.ingredients.size(), is(2));
        assertNotNull(recipe.steps);
        assertThat(recipe.steps.size(), is(2));
    }

    @After
    public void close() {
        piCrustDatabase.close();
    }

    private void assertRecipe(RecipeModel recipe) {
        assertThat(recipe.recipe.getId(), is(EXPECTED_RECIPE.getId()));
        assertThat(recipe.recipe.getName(), is(EXPECTED_RECIPE.getName()));
        assertThat(recipe.recipe.getServings(), is(EXPECTED_RECIPE.getServings()));
        assertThat(recipe.recipe.getImage(), is(EXPECTED_RECIPE.getImage()));
    }
}
