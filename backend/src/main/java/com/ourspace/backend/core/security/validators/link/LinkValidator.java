package com.ourspace.backend.core.security.validators.link;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link Link} constraint.
 */
@Component
public class LinkValidator implements ConstraintValidator<Link, String> {

  /**
   * Checks if the given value is a valid URL.
   *
   * @param value   the value to validate
   * @param context the constraint validator context
   * @return true if the value is a valid URL, false otherwise
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });

    return urlValidator.isValid(value);
  }
}
