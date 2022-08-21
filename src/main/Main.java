import manager.Managers;
import manager.TaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer();
            server.start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер - " + e.getMessage());
        }

    }
}
