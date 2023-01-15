package com.example.tacocloud.tacos;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngredientByIdConverter implements Converter<String,Ingredient> {

    private Map<String, Ingredient> ingredientMap = new HashMap<>();

    public IngredientByIdConverter(){
        ingredientMap.put("FLTO",new Ingredient("FLTO","Flour Tortilla", Ingredient.Type.WRAP));
        ingredientMap.put("COTO",new Ingredient("COTO","Corn Tortilla", Ingredient.Type.WRAP));
        ingredientMap.put("CRBF",new Ingredient("CRBF", "Ground beef", Ingredient.Type.PROTEIN));
        ingredientMap.put("CARN",new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
        ingredientMap.put("TMTO",new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
        ingredientMap.put("LETC",new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
        ingredientMap.put("CHED",new Ingredient("CHED","Cheddar", Ingredient.Type.CHEESE));
        ingredientMap.put("JACK",new Ingredient("JACK","Monterrey Jack", Ingredient.Type.CHEESE));
        ingredientMap.put("SRCR",new Ingredient("SRCR","Sour Cream", Ingredient.Type.SAUCE));


    }

    @Override
    public Ingredient convert(String source) {
        return ingredientMap.get(source);
    }
}
