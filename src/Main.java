import ru.artemov.manager.ManagerSaveException;
import ru.artemov.manager.Managers;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Main {

    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Task task2 = new Task("task2", "desc2", Status.NEW, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        SubTask subTask11 = new SubTask("SubTask1", "descSub1", Status.NEW, epic1, LocalDateTime.of(2023, 1, 5, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask12 = new SubTask("SubTask2", "descSub2", Status.NEW, epic1, LocalDateTime.of(2023, 1, 4, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask13 = new SubTask("SubTask3", "descSub3", Status.NEW, epic1, LocalDateTime.of(2023, 1, 3, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Epic epic2 = new Epic("Epic2", "descrEpic2");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask11);
        taskManager.createSubtask(subTask12);
        taskManager.createSubtask(subTask13);
        taskManager.createEpic(epic2);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("_________________________________");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("_________________________________");

        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getEpicById(3);
        taskManager.getTaskById(2);
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(2);
        System.out.println(taskManager.getHistory());

        taskManager.getSubTaskById(4);
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());

        taskManager.deleteEpicById(3);

        System.out.println(taskManager.getHistory());


    }
}
