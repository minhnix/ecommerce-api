package com.nix.ecommerceapi.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class SlugUtils {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\p{IsAlphabetic}\\d-]");
    private static final Pattern WHITESPACE = Pattern.compile(" ");

    public static String createSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFC);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        Random random = new Random();
        int randomValue = 1000000 + random.nextInt(1000000);
        slug = "%s-%d".formatted(slug, randomValue);
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
