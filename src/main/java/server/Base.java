package server;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Base {
    Map<String, String> base= new HashMap<>();
    public void writeToBase() throws FileNotFoundException {
        Gson gson=new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Path path = Paths.get("db.json");
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException ex) {
            System.out.println("Can not find file");
        }
        if(bytes!=null) {
            String json1 = new String(bytes, StandardCharsets.UTF_8);
            base = gson.fromJson(json1, type);
        }
    }

    String set(String a,String temp) {
        base.put(a,temp);
        return "OK";
    }

    String get(String a) {
        if(base.containsKey(a))
            return base.get(a);
        else {
            return "ERROR";
        }
    }

    String delete(String a) {
        if (base.containsKey(a)) {
            base.remove(a);
            return "OK";
        }
        else {
            return "ERROR";
        }
    }

    public void update() {
        String jsonStr = new Gson().toJson(base);
        try(FileWriter writer = new FileWriter("db.json", false))
        {
            writer.write(jsonStr);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
