package edu.lysak.recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import edu.lysak.recipes.model.Recipe;
import edu.lysak.recipes.model.User;
import edu.lysak.recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping("/api/recipe/new")
    public Map<String, Long> addRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal User user) {
        Long id = recipeService.addRecipe(recipe, user);
        return Map.of("id", id);
    }

    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal User user) {
        recipeService.deleteRecipe(id, user);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/api/recipe/{id}")
    public void updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal User user) {
        recipeService.updateRecipe(id, recipe, user);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/search")
    public List<Recipe> searchRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ) {
        if ((category == null && name == null) || (category != null && name != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (category != null) {
            return recipeService.getRecipesByCategory(category);
        } else {
            return recipeService.getRecipesByName(name);
        }
    }
}
