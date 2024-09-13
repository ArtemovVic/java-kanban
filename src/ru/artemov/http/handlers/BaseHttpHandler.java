package ru.artemov.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.artemov.http.adapters.DurationAdapter;
import ru.artemov.http.adapters.LocalDateTimeAdapter;
import ru.artemov.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class BaseHttpHandler {
    public TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public void sendText(HttpExchange ex, String response, int code) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        ex.sendResponseHeaders(code, 0);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void sendIncorrectId(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        ex.sendResponseHeaders(400, 0);
        try (OutputStream os = ex.getResponseBody()) {
            os.write("id введен некорректно".getBytes(StandardCharsets.UTF_8));
        }
    }

    public void sendIncorrectMethod(HttpExchange ex) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        ex.sendResponseHeaders(400, 0);
        try (OutputStream os = ex.getResponseBody()) {
            os.write("Обработка метода не предусмотрена".getBytes(StandardCharsets.UTF_8));
        }
    }

    public Optional<Integer> getId(HttpExchange ex) {
        try {
            String[] splitPath = ex.getRequestURI().getPath().split("/");
            return Optional.of(Integer.parseInt(splitPath[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public String bodyToString(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    public Gson getGson() {
        return gson;
    }
}
