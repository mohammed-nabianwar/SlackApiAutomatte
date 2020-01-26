package com.SlackApiAutomate;

public enum SlackApiUrls {
  CREATE_API_URL("https://slack.com/api/conversations.create"),
  JOIN_CREATED_CHANNEL("https://slack.com/api/conversations.join"),
  RENAME_CHANNEL("https://slack.com/api/conversations.rename"),
  LIST_ALL_CHANNELS("https://slack.com/api/conversations.list"),
  ARCHIEVE_CHANNEL("https://slack.com/api/conversations.archive"),
  CHANNEL_INFO("https://slack.com/api/conversations.info");


  private final String url;

  SlackApiUrls(String url) {
    this.url = url;
  }

  public String getUrl() {
    return this.url;
  }
}
