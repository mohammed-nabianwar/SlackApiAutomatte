package com.SlackApiAutomate;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;


public class ApiService {

  public JsonPath postApiQueryParams(String uri, Map<String, String> input, String token) {
    return given().queryParam("token", token)
        .queryParams(input)
        .accept(ContentType.JSON)
        .when()
        .post(uri)
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath();
  }

  public JsonPath getApiQueryParams(String url, Map<String, String> input, String token) {
    return given()
        .queryParam("token", token)
        .queryParams(input)
        .accept(ContentType.JSON)
        .when()
        .get(url)
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath();
  }

  public Response getResponseQueryParams(String url, Map<String, String> input, String token) {
    return given()
        .queryParam("token", token)
        .queryParams(input)
        .accept(ContentType.JSON)
        .when()
        .get(url)
        .then()
        .statusCode(200)
        .extract()
        .response();
  }
}
