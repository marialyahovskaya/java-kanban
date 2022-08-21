import client.KVClient;
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

        try {
            KVClient client = new KVClient("http://localhost:8078");
            client.put("test", "KVClient работает корректно");
            System.out.println(client.load("test"));
        } catch (InterruptedException | IOException e) {
            System.out.println("Ошибка при работе клиента - " + e.getMessage());
        }

    }
}
