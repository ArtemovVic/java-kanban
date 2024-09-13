package ru.artemov.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.artemov.http.handlers.*;
import ru.artemov.manager.ManagerSaveException;
import ru.artemov.manager.Managers;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public TaskManager taskManager;
    public HttpServer httpServer;

    public static void main(String[] args) throws ManagerSaveException, IOException, InterruptedException {

        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        BaseHttpHandler handler = new BaseHttpHandler(taskManager);
        Gson gson = handler.getGson();
        taskServer.createHttpServer();
        taskServer.start();

        Task task = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(taskManager.getAllTask());

        URI urlForDelete = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());
        System.out.println(taskManager.getAllTask());

        taskServer.stop();


    }

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;

    }

    public void createHttpServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));

    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HTTP-сервер остановлен");
    }
}
