package manager;

import com.google.gson.*;
import manager.file.FileBackedTaskManager;
import manager.file.ManagerLoadException;
import manager.memory.InMemoryHistoryManager;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Type;


public final class Managers {

    private static final String DB_FILENAME = "data/data.txt";

    public static TaskManager getDefault() {
        try {
            TaskManager manager;
            manager = FileBackedTaskManager.loadFromFile(new File(DB_FILENAME));
            return manager;
        } catch (ManagerLoadException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
