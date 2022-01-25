package com.example.demo.dao;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Dao {
    private static final AtomicLong counter = new AtomicLong((System.currentTimeMillis() * 1000));
    private static final Map<Long, Pair<String, String>> someKeyValueStore = new HashMap<>();

    private Dao() {
    }

    public static String getShortURL(String url) {
        var id = getNewId();
        var shortUrl = idToShortURL(id);
        someKeyValueStore.put(id, Pair.with(shortUrl, url));
        return shortUrl;
    }

    public static String getOriginalURL(String shortUrl) {
        var id = shortURLtoID(shortUrl);
        var entity = someKeyValueStore.get(id);
        return entity.getValue1();
    }

    private static Long getNewId() {
        return counter.incrementAndGet();
    }

    private static String idToShortURL(Long n) {
        char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        var shortURL = new StringBuilder();
        while (n > 0) {
            shortURL.append(map[(int) (n % 62)]);
            n = n / 62;
        }
        return shortURL.reverse().toString();
    }

    private static Long shortURLtoID(String shortURL) {
        long id = 0L;
        for (int i = 0; i < shortURL.length(); i++) {
            if ('a' <= shortURL.charAt(i) && shortURL.charAt(i) <= 'z') id = id * 62 + shortURL.charAt(i) - 'a';
            if ('A' <= shortURL.charAt(i) && shortURL.charAt(i) <= 'Z') id = id * 62 + shortURL.charAt(i) - 'A' + 26;
            if ('0' <= shortURL.charAt(i) && shortURL.charAt(i) <= '9') id = id * 62 + shortURL.charAt(i) - '0' + 52;
        }
        return id;
    }
}
