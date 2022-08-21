import manager.http.KVClient;
import server.KVServer;

import java.io.IOException;

public class KVServerMain {
    public static void main(String[] args) {
        try {
            new KVServer().start();
        } catch (IOException e) {
            System.out.println("Не удалось запустить KVServer - " + e.getMessage());
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
