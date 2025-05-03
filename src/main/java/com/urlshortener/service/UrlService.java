package com.urlshortener.service;

import com.urlshortener.entity.Url;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.util.ShortCodeGenerator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

  @Autowired
  private UrlRepository urlRepository;

  public Url createShortUrl(String longUrl) {
    // Validate longUrl (basic check)
    if (longUrl == null || longUrl.isEmpty()) {
      throw new IllegalArgumentException("Long URL cannot be empty");
    }

    try {
      new URL(longUrl); // Ensure it's a valid URL
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid URL format");
    }

    // Generate unique short code
    String shortCode;
    do {
      shortCode = ShortCodeGenerator.generateShortCode();
    } while (urlRepository.findByShortCode(shortCode).isPresent());

    // Create and save URL
    Url url = new Url(longUrl, shortCode);
    return urlRepository.save(url);
  }

  public Optional<Url> findByShortCode(String shortCode) {
    return urlRepository.findByShortCode(shortCode);
  }

  public void incrementClickCount(Url url) {
    url.setClickCount(url.getClickCount() + 1);
    urlRepository.save(url);
  }
}