package com.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.dto.UrlRequest;
import com.urlshortener.entity.Url;
import com.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @AfterEach
  public void tearDown() {
    urlRepository.deleteAll();
  }

  @Test
  public void testShortenUrl_Success() throws Exception {
    // Arrange
    String longUrl = "https://www.google.com";
    UrlRequest request = new UrlRequest();
    request.setLongUrl(longUrl);

    // Act & Assert
    mockMvc.perform(post("/api/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.longUrl").value(longUrl))
        .andExpect(jsonPath("$.shortCode").isNotEmpty())
        .andExpect(jsonPath("$.clickCount").value(0));
  }

  @Test
  public void testShortenUrl_InvalidUrl() throws Exception {
    // Arrange
    UrlRequest request = new UrlRequest();
    request.setLongUrl("invalid-url");

    // Act & Assert
    mockMvc.perform(post("/api/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid URL format"));
  }

  @Test
  public void testRedirect_Success() throws Exception {
    // Arrange
    String longUrl = "https://www.google.com";
    String shortCode = "abc123";
    Url url = new Url(longUrl, shortCode);
    urlRepository.save(url);

    // Act & Assert
    mockMvc.perform(get("/api/" + shortCode))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(longUrl));

    // Verify click count
    Url updatedUrl = urlRepository.findByShortCode(shortCode).orElseThrow();
    assert updatedUrl.getClickCount() == 1L;
  }

  @Test
  public void testRedirect_NotFound() throws Exception {
    // Act & Assert
    mockMvc.perform(get("/api/nonexistent"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Short URL not found"));
  }
}