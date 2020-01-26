package com.SlackApiAutomate;

public enum Token {
  ADMIN_TOKEN("xoxp-922548494615-909218887955-908135806610-ab56f5a61a1eeb3ef109282e5ee3f943");
  private final String token;

  Token(String token) {
    this.token = token;
  }

  public String getToken() {
    return this.token;
  }
}
