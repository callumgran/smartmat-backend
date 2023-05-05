package edu.ntnu.idatt2106.smartmat.model.recipe;

import edu.ntnu.idatt2106.smartmat.model.household.WeeklyRecipe;
import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Class representing a food recipe in the system.
 * A recipe uses ingredients and also gives feedback on difficulty, instructions and time.
 * @author Simen G.
 * @version 1.1 - 20.4.2023
 */
@Entity
@Table(name = "`recipes`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "`id`", length = 16, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "`name`", length = 128, nullable = false)
  @NonNull
  private String name;

  @Column(name = "`description`", nullable = false, columnDefinition = "LONGTEXT")
  @NonNull
  private String description;

  @OneToMany(mappedBy = "recipe")
  private Set<RecipeIngredient> ingredients;

  @Column(name = "`instructions`", nullable = false, columnDefinition = "LONGTEXT")
  @NonNull
  private String instructions;

  @Column(name = "`estimated_minutes`", nullable = false)
  private int estimatedMinutes;

  @Enumerated(EnumType.STRING)
  @Column(name = "`recipe_difficulty`", nullable = false)
  @NonNull
  private RecipeDifficulty recipeDifficulty;

  @OneToMany(mappedBy = "recipe")
  private Set<WeeklyRecipe> weeklyRecipes;

  @Column(name = "`image`", nullable = true)
  private String image;
}
