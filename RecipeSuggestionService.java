import java.util.ArrayList;
import java.util.List;

public class RecipeSuggestionService {

    private ArrayList<Recipe> recipes;

    public RecipeSuggestionService() {
        recipes = new ArrayList<>();
        loadRecipes();
    }

    // Add predefined recipes
    private void loadRecipes() {

        recipes.add(new Recipe(
                "Tomato Rice",
                List.of("rice", "tomato", "onion")
        ));

        recipes.add(new Recipe(
                "Vegetable Fried Rice",
                List.of("rice", "carrot", "peas", "onion")
        ));

        recipes.add(new Recipe(
                "Butter Toast",
                List.of("bread", "butter")
        ));

        recipes.add(new Recipe(
                "Omelette",
                List.of("egg", "onion", "tomato")
        ));

        recipes.add(new Recipe(
                "Simple Sandwich",
                List.of("bread", "tomato", "onion")
        ));

        recipes.add(new Recipe(
                "Potato Fry",
                List.of("potato", "onion", "oil")
        ));
    }

    // Suggest recipes based on available pantry ingredients
    public ArrayList<String> suggestRecipes(
            ArrayList<PantryItem> pantryItems) {

        ArrayList<String> suggestions = new ArrayList<>();

        for (Recipe recipe : recipes) {

            boolean allIngredientsAvailable = true;

            for (String ingredient : recipe.getIngredients()) {

                boolean found = false;

                for (PantryItem item : pantryItems) {

                    if (item.getName().equalsIgnoreCase(ingredient)
                            && item.getQuantity() > 0
                            && !item.isExpired()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    allIngredientsAvailable = false;
                    break;
                }
            }

            if (allIngredientsAvailable) {
                suggestions.add(recipe.getName());
            }
        }

        return suggestions;
    }

    // Internal Recipe class
    private static class Recipe {

        private String name;
        private List<String> ingredients;

        public Recipe(String name, List<String> ingredients) {
            this.name = name;
            this.ingredients = ingredients;
        }

        public String getName() {
            return name;
        }

        public List<String> getIngredients() {
            return ingredients;
        }
    }
}