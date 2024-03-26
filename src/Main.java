import ru.artemov.manager.InMemoryTaskManager;
import ru.artemov.manager.Managers;
import ru.artemov.manager.TaskManager;
import ru.artemov.tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "desc1", Status.NEW);
        Task task2 = new Task("task2", "desc2", Status.NEW);
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        SubTask subTask11 = new SubTask("SubTask1", "descSub1", Status.NEW, epic1);
        SubTask subTask12 = new SubTask("SubTask2", "descSub2", Status.NEW, epic1);
        Epic epic2 = new Epic("Epic2", "descrEpic2");
        SubTask subTask21 = new SubTask("SubTask11", "descSub11", Status.NEW, epic2);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subTask11);
        taskManager.createSubtask(subTask12);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subTask21);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("----------update--------------");
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        System.out.println(taskManager.getHistory());
        taskManager.printHistory();

        Task updateTask2 = new Task("task2", "desc2", Status.IN_PROGRESS);
        taskManager.updateTaskById(2, updateTask2);
        SubTask updateSubTask11 = new SubTask("SubTask1", "descSub1", Status.IN_PROGRESS, epic1);
        taskManager.updateSubtask(4, updateSubTask11);

        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(6);
        System.out.println(taskManager.getHistory());
        taskManager.printHistory();

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("Список подзадач Эпика " + taskManager.getAllSubTaskOfEpic(3));
        System.out.println("----------------delete--------");


        taskManager.deleteSubtaskById(4);
        taskManager.deleteEpicById(6);

        System.out.println("Список подзадач Эпика " + taskManager.getAllSubTaskOfEpic(3));
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());


        taskManager.getTaskById(2);
        taskManager.getEpicById(3);




        System.out.println("_________________________");
        System.out.println(taskManager.getHistory());
        taskManager.printHistory();
    }
}
