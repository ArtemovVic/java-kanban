package ru.artemov.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.Task;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager taskManager) {
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
                        handleGetTasksResponse(ex);
                    } else {
                        if (getId(ex).isPresent()) {
                            handleGetTaskByIdResponse(ex, getId(ex).get());
                        } else {
                            sendIncorrectId(ex);
                        }
                    }
                    break;
                case "DELETE":
                    if (getId(ex).isPresent()) {
                        handleDeleteTask(ex, getId(ex).get());
                    } else {
                        sendIncorrectId(ex);
                    }
                    break;
                case "POST":
                    handlePostTask(ex);
                    break;
                default:
                    sendIncorrectMethod(ex);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetTasksResponse(HttpExchange ex) {
        try {
            if (taskManager.getAllTask().isEmpty()) {
                sendText(ex, "Список задач пуст", 404);
            } else {
                sendText(ex, gson.toJson(taskManager.getAllTask()), 200);
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetTaskByIdResponse(HttpExchange ex, int id) throws IOException {
        if (taskManager.getTaskById(id) == null) {
            sendText(ex, "Задача с таким id не найдена", 404);
        } else {
            sendText(ex, gson.toJson(taskManager.getTaskById(id)), 200);
        }
    }

    public void handleDeleteTask(HttpExchange ex, int id) throws IOException {
        if (taskManager.getTaskById(id) == null) {
            sendText(ex, "Задача с таким id не найдена", 404);
        } else {
            taskManager.deleteTaskById(id);
            sendText(ex, "Задача с id " + id + " удалена", 200);
        }
    }

    public void handlePostTask(HttpExchange ex) {
        try {
            String exchange = bodyToString(ex);
            if (exchange.isEmpty()) {
                sendText(ex, "Ничего не передано", 400);
            } else {
                Task task = gson.fromJson(exchange, Task.class);
                System.out.println(task);
                Optional<Integer> id = Optional.of(task.getId());
                if (taskManager.checkIntersectionOfTime(task)) {
                    sendText(ex, "Задача не добавлена, так как имеет наложение по времени", 406);
                } else if (id.get() == 0) {
                    taskManager.createTask(task);
                    sendText(ex, "Задача добавлена", 201);
                } else {
                    taskManager.updateTaskById(task.getId(), task);
                    sendText(ex, "Задача с id " + id.get() + " обновлена", 201);
                }
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }
}

