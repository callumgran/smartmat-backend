package edu.ntnu.idatt2106.smartmat.utils;

import edu.ntnu.idatt2106.smartmat.model.unit.Unit;
import edu.ntnu.idatt2106.smartmat.model.unit.UnitTypeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for parsing units from product names
 *
 * @author Callum G.
 * @version 1.0 02.05.2023
 */
public class ParseUnit {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParseUnit.class);

  /**
   * Parses the unit from a product name
   *
   * @param units        The units to parse
   * @param productName  The product name to parse from
   * @param defaultUnit  The default unit to return if no unit is found
   * @return The unit and amount of the product
   */
  public static UnitAmountTuple parseUnit(
    Collection<Unit> units,
    String productName,
    Unit defaultUnit
  ) {
    units.add(
      new Unit(
        "stk",
        "pk",
        Collections.emptySet(),
        1,
        UnitTypeEnum.BY_PIECE,
        Collections.emptySet()
      )
    );
    final List<UnitStringTuple> unitNames = new ArrayList<>();
    final String lowerProductName = productName.toLowerCase();
    LOGGER.info("Parsing unit from product name: " + productName);
    units.forEach(unit -> {
      Pattern pattern = Pattern.compile(unit.getAbbreviation());
      Matcher matcher = pattern.matcher(lowerProductName);
      while (matcher.find()) {
        if (matcher.start() < 3) continue;
        unitNames.add(
          new UnitStringTuple(
            unit,
            lowerProductName.substring(0, matcher.start() + unit.getAbbreviation().length())
          )
        );
      }
    });

    double amount = 1;
    LOGGER.info("Unit names: {}", unitNames);

    if (unitNames.size() == 0) {
      return new UnitAmountTuple(defaultUnit, amount);
    } else {
      List<UnitAmountTuple> unitAmountTuples = unitNames
        .stream()
        .filter(entry -> {
          LOGGER.info(
            "Option 1: {}",
            entry
              .getString()
              .charAt(entry.getString().length() - 1 - entry.getUnit().getAbbreviation().length())
          );
          LOGGER.info(
            "Option 2: {}",
            entry
              .getString()
              .charAt(entry.getString().length() - 2 - entry.getUnit().getAbbreviation().length())
          );
          return (
            Character.isDigit(
              entry
                .getString()
                .charAt(entry.getString().length() - 1 - entry.getUnit().getAbbreviation().length())
            ) ||
            (
              Character.isDigit(
                entry
                  .getString()
                  .charAt(
                    entry.getString().length() - 2 - entry.getUnit().getAbbreviation().length()
                  )
              ) &&
              entry
                .getString()
                .charAt(
                  entry.getString().length() - 1 - entry.getUnit().getAbbreviation().length()
                ) ==
              ' '
            )
          );
        })
        .map(entry -> {
          String value = entry.getString();
          List<Character> chars = new ArrayList<>();
          int index = value.length() - 1;
          int i = 0;
          while (
            !Character.isDigit(value.charAt(index)) &&
            i < entry.getUnit().getAbbreviation().length() + 2
          ) {
            LOGGER.info("Char: {}", value.charAt(index--));
            i++;
          }
          while (
            Character.isDigit(value.charAt(index)) ||
            value.charAt(index) == ',' ||
            value.charAt(index) == '.'
          ) {
            chars.add(value.charAt(index--));
          }
          double multiplier = 1;
          if (value.charAt(index--) == 'x') {
            List<Character> multiplierChars = new ArrayList<>();
            while (Character.isDigit(value.charAt(index))) {
              multiplierChars.add(value.charAt(index--));
            }
            Collections.reverse(multiplierChars);
            double tmp = Double.parseDouble(
              multiplierChars
                .stream()
                .reduce("", (partialString, character) -> partialString + character, String::concat)
                .replace(",", ".")
            );
            multiplier = tmp > 1 ? tmp : 1;
          }
          Collections.reverse(chars);
          LOGGER.info("Chars: {}", chars);
          return (
            new UnitAmountTuple(
              entry.getUnit(),
              Double.parseDouble(
                chars
                  .stream()
                  .reduce(
                    "",
                    (partialString, character) -> partialString + character,
                    String::concat
                  )
                  .replace(",", ".")
              ) *
              multiplier
            )
          );
        })
        .toList();

      if (unitAmountTuples.size() == 0) {
        return new UnitAmountTuple(defaultUnit, amount);
      } else {
        final List<UnitAmountTuple> sortedAbbreviation = unitAmountTuples
          .stream()
          .sorted((e1, e2) -> {
            int ret = e1.getUnit().getAbbreviation().compareTo(e2.getUnit().getAbbreviation());
            if (ret == 0) {
              return e1.getAmount().compareTo(e2.getAmount());
            }
            return ret;
          })
          .toList();
        if (sortedAbbreviation.get(0).getUnit().getAbbreviation().equals("pk")) {
          return new UnitAmountTuple(defaultUnit, sortedAbbreviation.get(0).getAmount());
        }
        return sortedAbbreviation.get(0);
      }
    }
  }
}
