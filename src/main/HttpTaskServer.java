import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.StartTimeOverlapException;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;
    private final Gson gson;

    private int requestCounter = 1;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        manager = Managers.getDefault();
        gson = Managers.getGson();
        server.createContext("/tasks/task", this::routeTaskRequests);
        server.createContext("/tasks/epic", this::routeEpicRequests);
        server.createContext("/tasks/subtask", this::routeSubtaskRequests);
    }


    public void routeTaskRequests(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        System.out.println(requestCounter++ + ": received " + method + " " + path + " request with " + query + " query");

        switch (method) {
            case "GET" -> {
                if (path.equals("/tasks/task") || path.equals("/tasks/task/")) {
                    if (query == null) {
                        getTasks(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            getTask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "POST" -> {
                if (path.equals("/tasks/task") || path.equals("/tasks/task/")) {
                    if (query == null) {
                        addTask(httpExchange, Task.class);
                    } else {
                        if (params.get("id") != null) {
                            updateTask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "DELETE" -> {
                if (path.equals("/tasks/task") || path.equals("/tasks/task/")) {
                    if (query == null) {
                        deleteTasks(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            deleteTask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);

        }
        httpExchange.close();
    }

    public void routeEpicRequests(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        System.out.println(requestCounter++ + ": received " + method + " " + path + " request with " + query + " query");

        switch (method) {
            case "GET" -> {
                if (path.equals("/tasks/epic") || path.equals("/tasks/epic/")) {
                    if (query == null) {
                        getEpics(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            getEpic(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "POST" -> {
                if (path.equals("/tasks/epic") || path.equals("/tasks/epic/")) {
                    if (query == null) {
                        addTask(httpExchange, Epic.class);
                    } else {
                        if (params.get("id") != null) {
                            updateEpic(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "DELETE" -> {
                if (path.equals("/tasks/epic") || path.equals("/tasks/epic/")) {
                    if (query == null) {
                        deleteEpics(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            deleteEpic(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);

        }
        httpExchange.close();
    }

    public void routeSubtaskRequests(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        System.out.println(requestCounter++ + ": received " + method + " " + path + " request with " + query + " query");

        switch (method) {
            case "GET" -> {
                if (path.equals("/tasks/subtask") || path.equals("/tasks/subtask/")) {
                    if (query == null) {
                        getSubtasks(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            getSubtask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "POST" -> {
                if (path.equals("/tasks/subtask") || path.equals("/tasks/subtask/")) {
                    if (query == null) {
                        addTask(httpExchange, Subtask.class);
                    } else {
                        if (params.get("id") != null) {
                            updateSubtask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            case "DELETE" -> {
                if (path.equals("/tasks/subtask") || path.equals("/tasks/subtask/")) {
                    if (query == null) {
                        deleteSubtasks(httpExchange);
                    } else {
                        if (params.get("id") != null) {
                            deleteSubtask(httpExchange, Integer.parseInt(params.get("id")));
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(400, 0);
                }
            }
            default -> httpExchange.sendResponseHeaders(400, 0);
        }
        httpExchange.close();
    }

    public void getTasks(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, gson.toJson(manager.getTasks()));
    }

    public void getTask(HttpExchange httpExchange, int id) throws IOException {
        Task t = manager.getTask(id);
        if (t != null) {
            sendText(httpExchange, gson.toJson(t));
        } else {
            httpExchange.sendResponseHeaders(404, 0);
        }
    }

    private void addTask(HttpExchange httpExchange, Class<? extends Task> classOfTask) throws IOException {
        Task task = gson.fromJson(readBody(httpExchange), classOfTask);
        try {
            int id = manager.addTask(task);
            System.out.println("Created " + task.getType().toString().toLowerCase() + " with id " + id);
            sendText(httpExchange, gson.toJson(new CreationResponse(id)), 201);
        } catch (StartTimeOverlapException e) {
            sendError(httpExchange, e.getMessage(), 409);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    private void updateTask(HttpExchange httpExchange, int id) throws IOException {
        Task task = gson.fromJson(readBody(httpExchange), Task.class);
        if (id != task.getId()) {
            sendError(httpExchange, "id в параметрах и объекте не совпадают", 400);
            return;
        }
        try {
            manager.updateTask(task);
            System.out.println("Updated task with id " + id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (StartTimeOverlapException e) {
            sendError(httpExchange, e.getMessage(), 409);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteTasks(HttpExchange httpExchange) throws IOException {
        try {
            manager.deleteTasks();
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteTask(HttpExchange httpExchange, int id) throws IOException {
        try {
            manager.deleteTask(id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void getEpics(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, gson.toJson(manager.getEpics()));
    }


    public void getEpic(HttpExchange httpExchange, int id) throws IOException {
        try {
            Epic e = manager.getEpic(id);
            if (e != null) {
                sendText(httpExchange, gson.toJson(e));
            } else {
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    private void updateEpic(HttpExchange httpExchange, int id) throws IOException {
        try {
            Epic epic = gson.fromJson(readBody(httpExchange), Epic.class);
            if (id != epic.getId()) {
                sendError(httpExchange, "id в параметрах и объекте не совпадают", 400);
                return;
            }
            manager.updateEpic(epic);
            System.out.println("Updated epic with id " + id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteEpics(HttpExchange httpExchange) throws IOException {
        try {
            manager.deleteEpics();
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteEpic(HttpExchange httpExchange, int id) throws IOException {
        try {
            manager.deleteEpic(id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void getSubtasks(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, gson.toJson(manager.getSubtasks()));
    }

    public void getSubtask(HttpExchange httpExchange, int id) throws IOException {
        Subtask s = manager.getSubtask(id);
        if (s != null) {
            sendText(httpExchange, gson.toJson(s));
        } else {
            httpExchange.sendResponseHeaders(404, 0);
        }
    }

    private void updateSubtask(HttpExchange httpExchange, int id) throws IOException {
        try {
            Subtask subtask = gson.fromJson(readBody(httpExchange), Subtask.class);
            if (id != subtask.getId()) {
                sendError(httpExchange, "id в параметрах и объекте не совпадают", 400);
                return;
            }
            manager.updateSubtask(subtask);
            System.out.println("Updated subtask with id " + id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            manager.deleteSubtasks();
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void deleteSubtask(HttpExchange httpExchange, int id) throws IOException {
        try {
            manager.deleteSubtask(id);
            httpExchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    protected void sendText(HttpExchange httpExchange, String json) throws IOException {
        sendText(httpExchange, json, 200);
    }

    protected void sendError(HttpExchange httpExchange, String errorMessage, int rCode) throws IOException {
        String json = gson.toJson(new ErrorMessage(errorMessage));
        sendText(httpExchange, json, rCode);
    }

    protected void sendText(HttpExchange httpExchange, String json, int rCode) throws IOException {
        byte[] resp = json.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(rCode, resp.length);
        httpExchange.getResponseBody().write(resp);
    }

    protected String readBody(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}

class ErrorMessage {
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class CreationResponse {
    private int id;

    public CreationResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}