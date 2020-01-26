package com.SlackApiAutomate;

import static com.SlackApiAutomate.SlackApiUrls.*;
import static com.SlackApiAutomate.Token.ADMIN_TOKEN;

import com.SlackApiAutomate.util.DataProviderClass;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SlackApiTest {
  private ApiService apiService = new ApiService();
  private JsonPath jsonPath;
  private Map<String, String> createChannelData = new HashMap<String, String>();

  @Test(
      description = "The channels  should be created as these are all valid names.",
      dataProvider = "validNames", dataProviderClass = DataProviderClass.class)
  public void checkNameValidCombinations(String name) {
    jsonPath = apiService.postApiQueryParams(CREATE_API_URL.getUrl(),
                                             Collections.singletonMap("name", name),
                                             ADMIN_TOKEN.getToken());
    createChannelData.put(name, jsonPath.getString("channel.id"));
    Assert.assertEquals(jsonPath.getBoolean("ok"), true);
  }

  @Test(priority = 1,
        description = "The channel names are invalid and should always return a false response.",
        dataProvider = "invalidNames", dataProviderClass = DataProviderClass.class)
  public void checkInvalidNameCombinations(String name) {
    Assert.assertEquals(apiService.postApiQueryParams(CREATE_API_URL.getUrl(),
                                                      Collections.singletonMap("name", name),
                                                      ADMIN_TOKEN.getToken())
                            .getBoolean("ok"), false);
  }

  @Test(priority = 1,
        description = "Check whether channels are created or not.",
        dataProvider = "validNames", dataProviderClass = DataProviderClass.class)
  public void checkChannelsCreatedOrNot(String name) {
    jsonPath = apiService.getApiQueryParams(CHANNEL_INFO.getUrl(),
                                            Collections.singletonMap("channel",
                                                                     createChannelData.get(name)),
                                            ADMIN_TOKEN.getToken());
    Assert.assertEquals(jsonPath.getBoolean("ok"), true);
  }

  @Test(priority = 2,
        description = "Join the creted channels and verify joined or not.",
        dataProvider = "validNames", dataProviderClass = DataProviderClass.class)
  public void joinCreatedChannel(String name) {
    jsonPath = apiService.postApiQueryParams(JOIN_CREATED_CHANNEL.getUrl(),
                                             Collections.singletonMap("channel",
                                                                      createChannelData.get(name)),
                                             ADMIN_TOKEN.getToken());
    Assert.assertEquals(jsonPath.getBoolean("ok"), true);
  }

  @Test(priority = 3,
        description = "Renaming the channel.",
        dataProvider = "validNames",
        dataProviderClass = DataProviderClass.class)
  public void renameChannel(final String name) {
    jsonPath = apiService.postApiQueryParams(RENAME_CHANNEL.getUrl(),
                                             new HashMap<String, String>() {
                                               {
                                                 put("channel", createChannelData.get(name));
                                                 put("name", name + "renamed");
                                               }

                                               ;
                                             }, ADMIN_TOKEN.getToken());
    createChannelData.put(name + "renamed", createChannelData.remove(name));
    Assert.assertEquals(jsonPath.getBoolean("ok"), true);
  }

  @Test(priority = 4, description = "List all channels and verify if name changed or not.")
  public void listAllChannelsAndVerifyNames() {
    String cursor = "";
    boolean hasNext = true;
    while (hasNext) {
      Response response = apiService.getResponseQueryParams(LIST_ALL_CHANNELS.getUrl(),
                                                            Collections.singletonMap("cursor",
                                                                                     cursor),
                                                            ADMIN_TOKEN.getToken());
      int sizeOfResponse = new ArrayList<String>((Collection<? extends String>) response.body()
          .path("channels")).size();
      for (int i = 0; i < sizeOfResponse; i++) {
        String channel = "channels[" + i + "]";
        if (createChannelData.values().contains(response.body().path(channel + ".id"))) {
          Assert.assertEquals(response.body().path(channel + ".id"),
                              createChannelData.get(response.body().path(channel + ".name")));
        }
      }
      if (!response.body().path("response_metadata.next_cursor").equals("")) {
        cursor = response.body().path("response_metadata.next_cursor");
      } else {
        hasNext = false;
      }
    }
  }

  @Test(priority = 5,
        description = "Archive a channel",
        dataProvider = "renameChannels",
        dataProviderClass = DataProviderClass.class)
  public void archiveAChannel(String name) {
    jsonPath = apiService.postApiQueryParams(ARCHIEVE_CHANNEL.getUrl(),
                                             Collections.singletonMap("channel",
                                                                      createChannelData.get(name)),
                                             ADMIN_TOKEN.getToken());
    Assert.assertEquals(jsonPath.getBoolean("ok"),
                        true);
  }

  @Test(priority = 6, description = "Verify if all the channels are archived.")
  public void verifyAllChannelsAreArchived() {
    for (String value : createChannelData.values()) {
      Assert.assertEquals(apiService.getApiQueryParams(CHANNEL_INFO.getUrl(),
                                                       Collections.singletonMap(
                                                           "channel",
                                                           value),
                                                       ADMIN_TOKEN.getToken())
                              .getBoolean("channel.is_archived"), true);
    }
  }
}
