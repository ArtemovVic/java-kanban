package ru.artemov.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.Epic;

import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange ex) {
        String method = ex.getRequestMethod();
        String[] split = ex.getRequestURI().getPath().split("/");
        try {
            switch (method) {
                case "GET":
                    if (split.length == 2) {
                        handleGetEpicsResponse(ex);
                    } else {
                        if (getId(ex).isPresent()) {
                            handleGetEpicByIdResponse(ex, getId(ex).get());
                        } else {
                            sendIncorrectId(ex);
                        }
                    }
                    break;
                case "DELETE":
                    if (getId(ex).isPresent()) {
                        handleDeleteEpic(ex, getId(ex).get());
                    } else {
                        sendIncorrectId(ex);
                    }
                    break;
                case "POST":
                    handlePostEpic(ex);
                    break;
                default:
                    sendIncorrectMethod(ex);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetEpicsResponse(HttpExchange ex) {
        try {
            if (taskManager.getAllEpic().isEmpty()) {
                sendText(ex, "Список эпиков пуст", 404);
            } else {
                sendText(ex, gson.toJson(taskManager.getAllEpic()), 200);
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetEpicByIdResponse(HttpExchange ex, int id) throws IOException {
        if (taskManager.getEpicById(id) == null) {
            sendText(ex, "Эпик с таким id не найден", 404);
        } else {
            sendText(ex, gson.toJson(taskManager.getEpicById(id)), 200);
        }
    }

    public void handleDeleteEpic(HttpExchange ex, int id) throws IOException {
        if (taskManager.getEpicById(id) == null) {
            sendText(ex, "Эпик с таким id не найден", 404);
        } else {
            taskManager.deleteEpicById(id);
            sendText(ex, "Эпик с id " + id + " удален", 200);
        }
    }

    public void handlePostEpic(HttpExchange ex) {
        try {
            String exchange = bodyToString(ex);
            if (exchange.isEmpty()) {
                sendText(ex, "Ничего не передано", 400);
            } else {
                Epic epic = gson.fromJson(exchange, Epic.class);
                Optional<Integer> id = Optional.of(epic.getId());
                if (id.get() == 0) {
                    taskManager.createEpic(epic);
                    sendText(ex, "Эпик добавлен", 201);
                } else {
                    taskManager.updateEpicById(epic.getId(), epic);
                    sendText(ex, "Эпик с id " + id.get() + " обновлен", 201);
                }
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }
}
