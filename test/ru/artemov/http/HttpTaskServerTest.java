package ru.artemov.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.artemov.http.handlers.BaseHttpHandler;
import ru.artemov.manager.Managers;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    BaseHttpHandler handler = new BaseHttpHandler(taskManager);
    Gson gson = handler.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        taskServer.createHttpServer();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTask();
        assertNotNull(tasksFromManager, "Список пуст");
        Assertions.assertEquals("task1", Optional.of(tasksFromManager.get(0).getTitle()).orElse("Имя отсутствует"), "Некорректное имя задачи");

    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlForDelete = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Задача с id 1 удалена", responseDelete.body());
        assertEquals(0, taskManager.getAllTask().size(), "Задача не удалена");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task updatedTask = new Task("upTask1", "upDesc1", Status.IN_PROGRESS, 1, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(45, ChronoUnit.MINUTES));
        String updatedTaskJson = gson.toJson(updatedTask);
        URI urlForUpdate = URI.create("http://localhost:8080/tasks");
        HttpRequest requestForUpdate = HttpRequest.newBuilder()
                .uri(urlForUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> responseUpdate = client.send(requestForUpdate, HttpResponse.BodyHandlers.ofString());
        List<Task> map = taskManager.getAllTask();
        assertEquals(201, responseUpdate.statusCode());
        assertEquals("Задача с id 1 обновлена", responseUpdate.body());
        assertEquals("upTask1", Optional.of(taskManager.getAllTask().get(0).getTitle()).orElse("Имя отсутствует"));
    }

    @Test
    public void testPriorityListTask() throws IOException, InterruptedException {
        Task task1 = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Task task2 = new Task("task2", "desc2", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Task task3 = new Task("task3", "desc3", Status.NEW, LocalDateTime.of(2023, 1, 3, 0, 0), Duration.of(30, ChronoUnit.MINUTES));


        URI url = URI.create("http://localhost:8080/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3)))
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Task firstTask = taskManager.getPrioritizedTasks().stream()
                .findFirst()
                .orElse(null);
        assert firstTask != null;
        Assertions.assertEquals("task2", Optional.of(firstTask.getTitle()).orElse("Имя отсутствует"), "Задача не на первом месте");
    }
}