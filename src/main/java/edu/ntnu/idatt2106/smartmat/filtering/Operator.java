package edu.ntnu.idatt2106.smartmat.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;

/**
 * Enum for filtering operators.
 * Based on the operator enum from the blog post below and IDATT2105 project.
 * @see <a href="https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/">Piinalpin</a>
 * @author Callum G.
 * @version 1.0 - 21.04.2023
 */
public enum Operator {
  EQUAL {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());
      Expression<?> key = root.get(request.getKeyWord());
      return builder.and(builder.equal(key, value), predicate);
    }
  },
  NOT_EQUAL {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());
      Expression<?> key = root.get(request.getKeyWord());
      return builder.and(builder.notEqual(key, value), predicate);
    }
  },
  GREATER_THAN {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());

      if (request.getFieldType() == FieldType.DATE) {
        Expression<LocalDate> key = root.get(request.getKeyWord());
        return builder.and(builder.greaterThan(key, (LocalDate) value), predicate);
      }

      if (
        request.getFieldType() != FieldType.CHARACTER &&
        request.getFieldType() != FieldType.BOOLEAN &&
        request.getFieldType() != FieldType.STRING
      ) {
        Expression<Number> key = root.get(request.getKeyWord());
        return builder.and(builder.gt(key, (Number) value), predicate);
      }

      return predicate;
    }
  },
  GREATER_THAN_EQUAL {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());

      if (request.getFieldType() == FieldType.DATE) {
        Expression<LocalDate> key = root.get(request.getKeyWord());
        return builder.and(builder.greaterThanOrEqualTo(key, (LocalDate) value), predicate);
      }

      if (
        request.getFieldType() != FieldType.CHARACTER &&
        request.getFieldType() != FieldType.BOOLEAN &&
        request.getFieldType() != FieldType.STRING
      ) {
        Expression<Number> key = root.get(request.getKeyWord());
        return builder.and(builder.ge(key, (Number) value), predicate);
      }

      return predicate;
    }
  },
  LESS_THAN {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());

      if (request.getFieldType() == FieldType.DATE) {
        Expression<LocalDate> key = root.get(request.getKeyWord());
        return builder.and(builder.lessThan(key, (LocalDate) value), predicate);
      }

      if (
        request.getFieldType() != FieldType.CHARACTER &&
        request.getFieldType() != FieldType.BOOLEAN &&
        request.getFieldType() != FieldType.STRING
      ) {
        Expression<Number> key = root.get(request.getKeyWord());
        return builder.and(builder.lt(key, (Number) value), predicate);
      }

      return predicate;
    }
  },
  LESS_THAN_EQUAL {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());

      if (request.getFieldType() == FieldType.DATE) {
        Expression<LocalDate> key = root.get(request.getKeyWord());
        return builder.and(builder.lessThanOrEqualTo(key, (LocalDate) value), predicate);
      }

      if (
        request.getFieldType() != FieldType.CHARACTER &&
        request.getFieldType() != FieldType.BOOLEAN &&
        request.getFieldType() != FieldType.STRING
      ) {
        Expression<Number> key = root.get(request.getKeyWord());
        return builder.and(builder.le(key, (Number) value), predicate);
      }

      return predicate;
    }
  },
  BETWEEN {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Object value = request.getFieldType().parse(request.getValue().toString());
      Object valueTo = request.getFieldType().parse(request.getValueTo().toString());

      if (request.getFieldType() == FieldType.DATE) {
        Expression<LocalDate> key = root.get(request.getKeyWord());
        return builder.and(builder.between(key, (LocalDate) value, (LocalDate) valueTo), predicate);
      }

      if (
        request.getFieldType() != FieldType.CHARACTER &&
        request.getFieldType() != FieldType.BOOLEAN &&
        request.getFieldType() != FieldType.STRING
      ) {
        Expression<Number> key = root.get(request.getKeyWord());
        return builder.and(
          builder.and(builder.ge(key, (Number) value), builder.le(key, (Number) valueTo)),
          predicate
        );
      }

      return predicate;
    }
  },
  IN {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Expression<?> key = root.get(request.getKeyWord());
      return builder.and(key.in(request.getValues()), predicate);
    }
  },
  NOT_IN {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Expression<?> key = root.get(request.getKeyWord());
      return builder.and(builder.not(key.in(request.getValues())), predicate);
    }
  },
  LIKE {
    public <T> Predicate build(
      Root<T> root,
      CriteriaBuilder builder,
      FilterRequest request,
      Predicate predicate
    ) {
      Expression<String> key = root.get(request.getKeyWord());
      return builder.or(
        builder.like(builder.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"),
        predicate
      );
    }
  };

  public abstract <T> Predicate build(
    Root<T> root,
    CriteriaBuilder cb,
    FilterRequest request,
    Predicate predicate
  );
}
