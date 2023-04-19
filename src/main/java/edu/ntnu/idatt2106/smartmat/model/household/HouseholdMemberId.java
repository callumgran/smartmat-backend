package edu.ntnu.idatt2106.smartmat.model.household;

import edu.ntnu.idatt2106.smartmat.model.user.User;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing the primary key of a household member.
 * @author Callum G.
 * @version 1.0
 * @date 18.4.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HouseholdMemberId implements Serializable {

  private User user;

  private Household household;
}
