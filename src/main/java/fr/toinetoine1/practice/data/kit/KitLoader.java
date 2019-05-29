package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 03/04/2019
*/

import com.google.gson.reflect.TypeToken;
import fr.toinetoine1.practice.utils.GsonFactory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class KitLoader {

    private static Type lists = new TypeToken<List<Kit>>(){}.getType();

    public static List<Kit> deserialize(String json) {
        return GsonFactory.getCompactGson().fromJson(json, lists);
    }

    public static String serialize(List<Kit> kit) {
        return GsonFactory.getCompactGson().toJson(kit);
    }

}
