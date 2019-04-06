package com.bearwolfapps.allofthelunch.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilterLunchItems{

    public static class FilterOptions{
        public String text;
        public boolean veganOnly;
        public boolean searchTextInRestaurantName;
        public boolean searchTextInMenuItemName;
        public String dylan = "Dylan";
        public String box = "Båx";
        public String gvc = "GVC";
        public String noPiccolo ="Piccolo";
        public String noFazer = "Fazer Cafe";
        public String noHuoltamo ="Huoltamo";
        public String noStudio10 = "Studio 10";
        public String noIsoPaja = "Iso Paja";
        public boolean piccolo;
        public boolean studio10;
        public boolean huoltamo;
        public boolean fazer;
        public boolean isopaj;

        public FilterOptions(String text, boolean veganOnly,boolean piccolo, boolean studio10, boolean huoltamo, boolean fazer, boolean isopaj, boolean strn, boolean stmn){
            this.text = text;
            this.veganOnly = veganOnly;
            this.piccolo = piccolo;
            this.studio10 = studio10;
            this.huoltamo = huoltamo;
            this.fazer = fazer;
            this.isopaj = isopaj;
            this.searchTextInRestaurantName = strn;
            this.searchTextInMenuItemName = stmn;

        }
    }

    public static List<LunchItems.Restaurant> filterLunchItems(
            List<LunchItems.Restaurant> items,
            FilterOptions options)
    {
        List<LunchItems.Restaurant> res = items;
        if (options.searchTextInRestaurantName)
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.dylan.toLowerCase()) && !r.name.toLowerCase().contains(options.gvc.toLowerCase()) && !r.name.toLowerCase().contains(options.box.toLowerCase());
            }).collect(Collectors.toList());
         if((options.piccolo == true))
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.noPiccolo.toLowerCase());
            }).collect(Collectors.toList());
        if((options.studio10 == true))
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.noStudio10.toLowerCase());
            }).collect(Collectors.toList());
        if((options.huoltamo == true))
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.noHuoltamo.toLowerCase());
            }).collect(Collectors.toList());
        if((options.fazer == true))
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.noFazer.toLowerCase());
            }).collect(Collectors.toList());
        if((options.isopaj == true))
            res = res.stream().filter((LunchItems.Restaurant r) -> {
                return !r.name.toLowerCase().contains(options.noIsoPaja.toLowerCase());
            }).collect(Collectors.toList());
        // if there are any restaurants left (filtering by name might have removed them all
        // filter the menuitems of the remaining restaurants
        if(res.size() > 0)
        {
            // rebuild the menus of the restaurants
            res = res.stream().map( (LunchItems.Restaurant r) -> {
                return new LunchItems.Restaurant(r.name,
                        // by filtering them according to the given filters
                        r.menu.stream().filter((LunchItems.MenuItem mi) -> {
                            if(options.searchTextInMenuItemName)
                                if(mi.food.toLowerCase().contains(options.text.toLowerCase()) == false)
                                    return false;
                            if(options.veganOnly && (mi.diet.contains("V") == false))
                                return false;

                            return true;
                        }).collect(Collectors.toList())
                );
            }).collect(Collectors.toList());
        }

        return res;
    }

    // for testing
    public static void main(String[] args){
        LunchItems.LunchItem[] lunchItems = {
                new LunchItems.LunchItem("2019-03-20", 10, Arrays.asList(new LunchItems.Restaurant[]{
                        new LunchItems.Restaurant("GeneralRestaurant", Arrays.asList(new LunchItems.MenuItem[]{
                            new LunchItems.MenuItem("Cake", "G"),
                            new LunchItems.MenuItem("Steak", "L"),
                            new LunchItems.MenuItem("Salad", "G,L"),
                            new LunchItems.MenuItem("Soup", "G"),
                        })),
                        new LunchItems.Restaurant("MeatRestaurant", Arrays.asList(new LunchItems.MenuItem[]{
                                new LunchItems.MenuItem("Meat", "G"),
                                new LunchItems.MenuItem("Raw meat", "G"),
                        })),
                })),
                new LunchItems.LunchItem("2019-03-21", 11, Arrays.asList(new LunchItems.Restaurant[]{
                        new LunchItems.Restaurant("GeneralRestaurant", Arrays.asList(new LunchItems.MenuItem[]{
                                new LunchItems.MenuItem("Cake", "G"),
                                new LunchItems.MenuItem("Steak", "L"),
                                new LunchItems.MenuItem("Salad", "G,L"),
                                new LunchItems.MenuItem("Soup", "G"),
                        })),
                        new LunchItems.Restaurant("VegetarianRestaurant", Arrays.asList(new LunchItems.MenuItem[]{
                                new LunchItems.MenuItem("Salad", "G,L,V"),
                                new LunchItems.MenuItem("Carrots", "G,L,V"),
                                new LunchItems.MenuItem("Apples", "G,L,V"),
                        })),
                        new LunchItems.Restaurant("MeatRestaurant", Arrays.asList(new LunchItems.MenuItem[]{
                                new LunchItems.MenuItem("Meat", "G"),
                                new LunchItems.MenuItem("Raw meat", "G"),
                        })),
                })),

        };
        for(LunchItems.LunchItem li : lunchItems) {
            System.out.println(li);
            for(LunchItems.Restaurant r : filterLunchItems( li.restaurants, new FilterOptions(
                    "meat", false, true, true, true, true, true, true  ,true          ))){
                System.out.println("=" + r);
                for(LunchItems.MenuItem mi : r.menu){
                    System.out.println("==" + mi);
                }
            }
        }

    }
}
