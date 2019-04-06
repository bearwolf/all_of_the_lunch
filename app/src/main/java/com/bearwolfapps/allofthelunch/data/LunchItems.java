package com.bearwolfapps.allofthelunch.data;

import android.util.Log;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LunchItems {
    public static final String DEFAULT_URL = "https://svenska.yle.fi/dataviz/food/foodserver.php";
    private static long _lastFetchedTimestamp = 0;
    private static String _cachedJson;
    private static final int CACHE_TIMEOUT_SECONDS = 300; // 5 minutes

    public static class LunchItem {
        public String humanDate;
        public int machineDate;
        @SerializedName("menus")
        public List<Restaurant> restaurants;

        @Override
        public String toString() {
            return String.format("%s - %d restaurants", humanDate, restaurants.size());
        }
        public LunchItem(String hd, int md, List<Restaurant> menus){
            this.humanDate = hd; this.machineDate = md; this.restaurants = menus;
        }
    }

    public static class MenuItem {
        public String food;
        public String diet;

        @Override
        public String toString() {
            return String.format("%s %s", food, diet);
        }
        public MenuItem(String f, String d){
            this.food = f; this.diet = d;
        }
    }

    public static class Restaurant {
        @SerializedName("restaurant")
        public String name;
        public List<MenuItem> menu;

        @Override
        public String toString() {
            return String.format("restaurang [%s] [%d] menuitems", name, menu.size());
        }

        public Restaurant(String n, List<MenuItem> menu){
            this.name = n; this.menu = menu;
        }
    }

    public static String _fetchJsonFromServer(String fromUrl) throws Exception {
        long currentTimeStamp = System.currentTimeMillis() / 1000;
        if(currentTimeStamp - _lastFetchedTimestamp < CACHE_TIMEOUT_SECONDS){
            Log.i("BjoernLunch", "returning CACHED data");
            return LunchItems._cachedJson;
        }

        java.net.URL url = new URL(fromUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String json_from_server = br.lines().collect(Collectors.joining("\n"));
        // only set timestamp on successful fetch
        Log.i("BjoernLunch", "returning fetched data, setting CACHE");
        _lastFetchedTimestamp = currentTimeStamp;
        _cachedJson = json_from_server;
        return json_from_server;
    }

    public static LunchItem[] _parseLunchFromJson(String json) {
        Gson gson = new Gson();
        LunchItem[] lunchgrejer = gson.fromJson(json, LunchItem[].class);
        return lunchgrejer;
    }

    /**
     * fetches json from given url, parses into List of LunchItem
     *
     * @param fromUrl
     * @return
     * @throws Exception
     */
    public static List<LunchItem> fetchLunchItems(String fromUrl) throws Exception {
        return Arrays.asList(_parseLunchFromJson(_fetchJsonFromServer(fromUrl)));
    }

    public static void main(String[] args) throws Exception {
        List<LunchItem> lunchgrejer = fetchLunchItems(DEFAULT_URL);
        for (LunchItem lg : lunchgrejer) {
            System.out.println(lg);
            for (Restaurant r : lg.restaurants) {
                System.out.println("--" + r);
                for (MenuItem mi : r.menu) {
                    System.out.println("----" + mi);
                }
            }
        }
    }
}