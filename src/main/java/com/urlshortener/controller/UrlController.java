package com.urlshortener.controller;

import com.urlshortener.dto.UrlRequest;
import com.urlshortener.entity.Url;
import com.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class UrlController {

  @Autowired
  private UrlService urlService;


  @Operation(summary = "Create a short URL", description = "Shortens a given long URL and returns the short URL details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Short URL created successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Url.class))),
      @ApiResponse(responseCode = "400", description = "Invalid URL format",
          content = @Content(mediaType = "application/json"))
  })
  @PostMapping("/shorten")
  public ResponseEntity<Url> shortenUrl(@RequestBody UrlRequest request) {
    Url url = urlService.createShortUrl(request.getLongUrl());
    return ResponseEntity.ok(url);
  }

  @Operation(summary = "Redirect to long URL", description = "Redirects to the original long URL based on the short code")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "302", description = "Redirect to long URL"),
      @ApiResponse(responseCode = "404", description = "Short URL not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/{shortCode}")
  public RedirectView redirect(@PathVariable String shortCode) {
    Url url = urlService.findByShortCode(shortCode)
        .orElseThrow(() -> new RuntimeException("Short URL not found"));
    urlService.incrementClickCount(url);
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(url.getLongUrl());
    redirectView.setContextRelative(false); // Treat URL as absolute
    return redirectView;
  }

  @Operation(summary = "Get URL statistics", description = "Returns statistics for a short URL, including click count")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Url.class))),
      @ApiResponse(responseCode = "404", description = "Short URL not found",
          content = @Content(mediaType = "application/json"))
  })
  @GetMapping("/stats/{shortCode}")
  public ResponseEntity<Url> getStats(@PathVariable String shortCode) {
    Url url = urlService.findByShortCode(shortCode)
        .orElseThrow(() -> new RuntimeException("Short URL not found"));
    return ResponseEntity.ok(url);
  }
}