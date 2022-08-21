import server.HttpTaskServer;

import java.io.IOException;

public class TaskServerMain {
    public static void main(String[] args) {
        try {
            new HttpTaskServer().start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер - " + e.getMessage());
        }
    }
}
