package edu.lysak.recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import edu.lysak.recipes.model.Recipe;
import edu.lysak.recipes.model.User;
import edu.lysak.recipes.repository.RecipeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe getRecipe(Long id) {
        return getRecipeFromDb(id);
    }

    public Long addRecipe(Recipe recipe, User user) {
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(user);
        return recipeRepository.save(recipe).getId();
    }

    public void deleteRecipe(Long recipeId, User user) {
        if (!isRecipeAuthor(recipeId, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Recipe recipe = getRecipeFromDb(recipeId);
        recipeRepository.delete(recipe);
    }

    @Transactional
    public void updateRecipe(Long recipeId, Recipe recipe, User user) {
        if (!isRecipeAuthor(recipeId, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Recipe oldRecipe = getRecipeFromDb(recipeId);
        mapRecipe(recipe, oldRecipe);
        recipeRepository.save(oldRecipe);
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    private boolean isRecipeAuthor(Long recipeId, User user) {
        Recipe recipe = getRecipeFromDb(recipeId);
        return recipe.getUser().getId().equals(user.getId());
    }

    private Recipe getRecipeFromDb(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void mapRecipe(Recipe recipe, Recipe oldRecipe) {
        oldRecipe.setDate(LocalDateTime.now());
        oldRecipe.setName(recipe.getName());
        oldRecipe.setCategory(recipe.getCategory());
        oldRecipe.setDescription(recipe.getDescription());
        oldRecipe.setIngredients(recipe.getIngredients());
        oldRecipe.setDirections(recipe.getDirections());
    }
}
