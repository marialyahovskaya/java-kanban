import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer().start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер - " + e.getMessage());
        }

    }
}
