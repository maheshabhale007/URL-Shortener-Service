package com.urlshortener.util;

import java.util.Random;

public class ShortCodeGenerator {
  private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int CODE_LENGTH = 6;
  private static final Random RANDOM = new Random();

  public static String generateShortCode() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < CODE_LENGTH; i++) {
      int index = RANDOM.nextInt(ALPHA_NUMERIC.length());
      sb.append(ALPHA_NUMERIC.charAt(index));
    }
    return sb.toString();
  }
}