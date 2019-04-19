package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 03/04/2019
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class KitLoader {

    private static Gson gson;
    private static Type lists;

    public KitLoader() {
        gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
        lists = new TypeToken<List<Kit>>(){}.getType();
    }

    public static List<Kit> deserialize(String json){
        List<Kit> kits= Collections.synchronizedList(
                gson.fromJson(json, lists)
        );

        return kits;
    }

    public static String serialize(List<Kit> kit){
        return gson.toJson(kit);
    }

}
