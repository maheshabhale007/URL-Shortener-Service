package com.urlshortener.service;

import com.urlshortener.entity.Url;
import com.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

  @Mock
  private UrlRepository urlRepository;

  @InjectMocks
  private UrlService urlService;

  private String validUrl;
  private String shortCode;

  @BeforeEach
  public void setUp() {
    validUrl = "https://www.google.com";
    shortCode = "abc123";
  }

  @Test
  public void testCreateShortUrl_Success() {
    // Arrange
    Url url = new Url(validUrl, shortCode);
    when(urlRepository.findByShortCode(any())).thenReturn(Optional.empty());
    when(urlRepository.save(any(Url.class))).thenReturn(url);

    // Act
    Url result = urlService.createShortUrl(validUrl);

    // Assert
    assertNotNull(result);
    assertEquals(validUrl, result.getLongUrl());
    assertEquals(shortCode, result.getShortCode());
    assertEquals(0L, result.getClickCount());
    assertNotNull(result.getCreatedAt());
    verify(urlRepository, times(1)).save(any(Url.class));
  }

  @Test
  public void testCreateShortUrl_EmptyUrl() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      urlService.createShortUrl("");
    });
    assertEquals("Long URL cannot be empty", exception.getMessage());
    verify(urlRepository, never()).save(any());
  }

  @Test
  public void testCreateShortUrl_NullUrl() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      urlService.createShortUrl(null);
    });
    assertEquals("Long URL cannot be empty", exception.getMessage());
    verify(urlRepository, never()).save(any());
  }

  @Test
  public void testCreateShortUrl_InvalidUrl() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      urlService.createShortUrl("invalid-url");
    });
    assertEquals("Invalid URL format", exception.getMessage());
    verify(urlRepository, never()).save(any());
  }

  @Test
  public void testFindByShortCode_Found() {
    // Arrange
    Url url = new Url(validUrl, shortCode);
    when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(url));

    // Act
    Optional<Url> result = urlService.findByShortCode(shortCode);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(validUrl, result.get().getLongUrl());
    verify(urlRepository, times(1)).findByShortCode(shortCode);
  }

  @Test
  public void testFindByShortCode_NotFound() {
    // Arrange
    when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

    // Act
    Optional<Url> result = urlService.findByShortCode(shortCode);

    // Assert
    assertFalse(result.isPresent());
    verify(urlRepository, times(1)).findByShortCode(shortCode);
  }

  @Test
  public void testIncrementClickCount() {
    // Arrange
    Url url = new Url(validUrl, shortCode);
    url.setClickCount(0L);
    when(urlRepository.save(url)).thenReturn(url);

    // Act
    urlService.incrementClickCount(url);

    // Assert
    assertEquals(1L, url.getClickCount());
    verify(urlRepository, times(1)).save(url);
  }
}