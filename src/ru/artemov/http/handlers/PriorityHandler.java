package ru.artemov.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.artemov.manager.TaskManager;

import java.io.IOException;

public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    public PriorityHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange ex) {
        String method = ex.getRequestMethod();
        String[] split = ex.getRequestURI().getPath().split("/");
        try {
            if (method.equals("GET")) {
                if (split.length == 2) {
                    handleGetPriority(ex);
                }
            } else {
                sendIncorrectMethod(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleGetPriority(HttpExchange ex) throws IOException {
        try {
            sendText(ex, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } catch (NullPointerException e) {
            sendText(ex, "Список просмотра пуст", 404);
        }
    }
}
