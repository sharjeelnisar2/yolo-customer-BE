package com.yolo.customer.recipe;

import com.yolo.customer.recipe.recipeImage.RecipeImage;
import com.yolo.customer.recipe.recipeImage.RecipeImageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeImageRepository recipeImageRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeImageRepository recipeImageRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeImageRepository = recipeImageRepository;
    }
    public List<Recipe> findAll(Integer page, Integer size, Integer ideaId, String search) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        if (size > 1000) {
            size = 1000;
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Recipe> pageRecipes;

        if ((ideaId == null || ideaId == 0) && (search == null || search.isEmpty())) {
            pageRecipes = recipeRepository.findAll(paging);
        } else if (ideaId != null && ideaId > 0) {
            pageRecipes = recipeRepository.findByIdeaId(ideaId, paging);
        } else {
            pageRecipes = recipeRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, paging);
        }

        if (pageRecipes.isEmpty()) {
            throw new EntityNotFoundException("No recipes found with the given criteria.");
        }

        return pageRecipes.getContent();
    }

    // Create a new recipe
    @Transactional
    public Recipe createRecipe(RecipeRequest  newRecipe) throws EntityNotFoundException {
//        Idea idea = ideaRepository.findbyCode();
//        Integer ideaId = idea.getId();
        Integer ideaId= 1;

        Recipe recipe = new Recipe();
        recipe.setName(newRecipe.getName());
        recipe.setDescription(newRecipe.getDescription());
        recipe.setServingSize(newRecipe.getServingSize());
        recipe.setPrice(newRecipe.getPrice());
        recipe.setCode(newRecipe.getCode());
        recipe.setIdeaId(ideaId);
//        recipe.setIdeaId(newRecipe.getIdeaId());
        recipe.setCurrencyId(1);

        Recipe createdRecipe = recipeRepository.save(recipe);

        List<String> imageUrls = newRecipe.getUrl();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                RecipeImage recipeImage = new RecipeImage();
                recipeImage.setUrl(imageUrl);
                recipeImage.setRecipeId(createdRecipe.getId());
                recipeImageRepository.save(recipeImage);
            }
        }


        return recipeRepository.save(recipe);
    }

}
