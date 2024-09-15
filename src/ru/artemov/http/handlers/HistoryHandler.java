package ru.artemov.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.artemov.manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange ex) {
        String method = ex.getRequestMethod();
        String[] split = ex.getRequestURI().getPath().split("/");
        try {
            if (method.equals("GET")) {
                if (split.length == 2) {
                    handleGetHistory(ex);
                }
            } else {
                sendIncorrectMethod(ex);
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }

    public void handleGetHistory(HttpExchange ex) {
        try {
            if (!taskManager.getHistory().isEmpty()) {
                sendText(ex, gson.toJson(taskManager.getHistory()), 200);
            } else {
                sendText(ex, gson.toJson("История пуста"), 400);
            }
        } catch (IOException e) {
            System.out.println("Во время выполнения запроса произошла ошибка. Проверьте URL");
        }
    }
}
