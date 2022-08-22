package manager;

import com.google.gson.*;
import manager.file.ManagerLoadException;
import manager.http.HttpTaskManager;
import manager.memory.InMemoryHistoryManager;
import util.LocalDateAdapter;

import java.time.LocalDate;

public final class Managers {

    private static final String SERVER_URL = "http://localhost:8078";

    public static TaskManager getDefault() {
        try {
            TaskManager manager;
            manager = HttpTaskManager.loadFromServer(SERVER_URL);
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