package com.ourspace.backend.core.security.helpers;

/**
 * Enum defining the authorization schemas.
 * As introduced in HTTP/1.0 https://www.rfc-editor.org/rfc/rfc1945
 */
public enum AuthorizationSchemas {
  BASIC("Basic"), BEARER("Bearer"), DIGEST("Digest");

  private final String text;

  AuthorizationSchemas(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
