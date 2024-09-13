package ru.artemov.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.SubTask;

import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandler(TaskManager taskManager) {
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
                        handleGetSubtasksResponse(ex);
                    } else {
                        if (getId(ex).isPresent()) {
                            handleGetSubtaskByIdResponse(ex, getId(ex).get());
                        } else {
                            sendIncorrectId(ex);
                        }
                    }
                    break;
                case "DELETE":
                    if (getId(ex).isPresent()) {
                        handleDeleteSubtask(ex, getId(ex).get());
                    } else {
                        sendIncorrectId(ex);
                    }
                    break;
                case "POST":
                    handlePostSubtask(ex);
                    break;
                default:
                    sendIncorrectMethod(ex);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetSubtasksResponse(HttpExchange ex) {
        try {
            if (taskManager.getAllSubTask().isEmpty()) {
                sendText(ex, "Список подзадач пуст", 404);
            } else {
                sendText(ex, gson.toJson(taskManager.getAllSubTask()), 200);
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetSubtaskByIdResponse(HttpExchange ex, int id) throws IOException {
        if (taskManager.getSubTaskById(id) == null) {
            sendText(ex, "Подзадача с таким id не найдена", 404);
        } else {
            sendText(ex, gson.toJson(taskManager.getSubTaskById(id)), 200);
        }
    }

    public void handleDeleteSubtask(HttpExchange ex, int id) throws IOException {
        if (taskManager.getSubTaskById(id) == null) {
            sendText(ex, "Подзадача с таким id не найдена", 404);
        } else {
            taskManager.deleteSubtaskById(id);
            sendText(ex, "Подзадача с id " + id + " удалена", 200);
        }
    }

    public void handlePostSubtask(HttpExchange ex) {
        try {
            String exchange = bodyToString(ex);
            if (exchange.isEmpty()) {
                sendText(ex, "Ничего не передано", 400);
            } else {
                SubTask subtask = gson.fromJson(exchange, SubTask.class);
                Optional<Integer> id = Optional.of(subtask.getId());
                if (taskManager.checkIntersectionOfTime(subtask)) {
                    sendText(ex, "Подзадача не добавлена, так как имеет наложение по времени", 406);
                } else if (id.get() == 0) {
                    taskManager.createSubtask(subtask);
                    sendText(ex, "Подзадача добавлена", 201);
                } else {
                    taskManager.updateSubtask(subtask.getId(), subtask);
                    sendText(ex, "Подзадача с id " + id.get() + " обновлена", 201);
                }
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }
}
