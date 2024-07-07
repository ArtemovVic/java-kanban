package ru.artemov.manager;

import ru.artemov.manager.history.HistoryManager;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory());


    public static void main(String[] args) throws ManagerSaveException {


        /*Task task1 = new Task("task1", "Description101", Status.NEW);
        Task task2 = new Task("task2", "Description102", Status.NEW);
        Task task3 = new Task("task2", "Description102", Status.NEW);
        Task task4 = new Task("task2", "Description102", Status.NEW);
        Task task5 = new Task("task2", "Description102", Status.NEW);
        Epic epic = new Epic("epic1", "Description111");
        SubTask subTask1 = new SubTask("subTask1", "Description", Status.NEW, epic);
        SubTask subTask2 = new SubTask("subTask2", "Description", Status.NEW, epic);
        SubTask subTask3 = new SubTask("subTask3", "Description", Status.NEW, epic);
        Epic epic2 = new Epic("epic2", "Description111");

        SubTask subTask35 = new SubTask("subTask35", "Description", Status.IN_PROGRESS, epic2);

        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createTask(task2);

        fileBackedTaskManager.createEpic(epic);

        fileBackedTaskManager.createSubtask(subTask1);
        fileBackedTaskManager.createSubtask(subTask2);
        fileBackedTaskManager.createSubtask(subTask3);

        fileBackedTaskManager.createEpic(epic2);
        fileBackedTaskManager.createSubtask(subTask35);

        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpic());
        System.out.println(fileBackedTaskManager.getAllSubTask());


        System.out.println("____________");

        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getTaskById(2);
        fileBackedTaskManager.getTaskById(2);
        fileBackedTaskManager.getTaskById(1);
        System.out.println("history ");
        System.out.println(fileBackedTaskManager.getHistory());
        System.out.println("history ");
        fileBackedTaskManager.deleteTaskById(1);
        fileBackedTaskManager.getEpicById(3);
        fileBackedTaskManager.getSubTaskById(4);
        System.out.println("history ");
        System.out.println(fileBackedTaskManager.getHistory());
        System.out.println("history ");
        fileBackedTaskManager.deleteEpicById(3);

        System.out.println("____________");
        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpic());
        System.out.println(fileBackedTaskManager.getAllSubTask());
        System.out.println(fileBackedTaskManager.getHistory());*/


        /*loadFromFile();
        System.out.println("Load");

        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpic());
        System.out.println(fileBackedTaskManager.getAllSubTask());*/

        fileBackedTaskManager.save();


    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);

    }

    public void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter("123.txt", false);) {
            writer.append("id,type,name,status,description,epic" + "\n");

            for (Task task : tasksById.values()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic task : epicById.values()) {
                writer.write(toString(task) + "\n");
            }
            for (SubTask task : subTaskById.values()) {
                writer.write(toString(task) + "\n");
            }

            writer.append(" " + "\n");


        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static void loadFromFile() throws ManagerSaveException {
        load();
    }

    public static void load() throws ManagerSaveException {
        fileBackedTaskManager.tasksById.clear();
        fileBackedTaskManager.subTaskById.clear();
        fileBackedTaskManager.epicById.clear();
        fileBackedTaskManager.historyManager.removeAll();

        try (BufferedReader br = new BufferedReader(new FileReader("123.txt", StandardCharsets.UTF_8));) {

            List<String> list = new ArrayList<>();
            while (br.ready()) {
                list.add(br.readLine());
            }

            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).isBlank()) {
                    break;
                }
                taskFromString(list.get(i));
            }


        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private static void taskFromString(String value) {
        String[] params = value.split(",");
        try {
            int parsedId = Integer.parseInt(params[0]);


            switch (params[1]) {
                case "TASK":
                    fileBackedTaskManager.tasksById.put(parsedId,
                            new Task(params[2], params[4], Status.valueOf(params[3]), parsedId));
                    break;
                case "EPIC":
                    fileBackedTaskManager.epicById.put(parsedId, new Epic(params[2], params[4], parsedId));
                    break;
                case "SUBTASK":
                    SubTask sbFromFile = new SubTask(params[2], params[4],
                            Status.valueOf(params[3]), fileBackedTaskManager.epicById.
                            get(Integer.parseInt(params[5])), parsedId);
                    sbFromFile.getEpic().addSubTask(sbFromFile);
                    fileBackedTaskManager.subTaskById.put(parsedId, sbFromFile);

                    break;

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Недостаточно введенных параметров о задаче. Проверьте содержимое файла.");
            throw new ArrayIndexOutOfBoundsException();
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат введенных данных.");
            e.printStackTrace();
        }
    }


    private String toString(Task task) {
        return String.format("%d,%S,%s,%S,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription());
    }

    private String toString(SubTask task) {
        return String.format("%d,%S,%s,%S,%s,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription(), task.getEpic().getId());
    }

    private String toString(Epic task) {
        return String.format("%d,%S,%s,%S,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription());
    }

    @Override
    public int createTask(Task task) throws ManagerSaveException {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createSubtask(SubTask subTask) throws ManagerSaveException {
        int id = super.createSubtask(subTask);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) throws ManagerSaveException {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void deleteAllTask() throws ManagerSaveException {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task result = super.getTaskById(id);
        save();
        return result;
    }

    @Override
    public void updateTaskById(int id, Task task) throws ManagerSaveException {
        super.updateTaskById(id, task);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllSubTask() throws ManagerSaveException {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public SubTask getSubTaskById(int id) throws ManagerSaveException {
        SubTask result = super.getSubTaskById(id);
        save();
        return result;
    }

    @Override
    public void updateSubtask(int id, SubTask subTask) throws ManagerSaveException {
        super.updateSubtask(id, subTask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllEpic() throws ManagerSaveException {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic result = super.getEpicById(id);
        save();
        return result;
    }

    @Override
    public void updateEpicById(int id, Epic epic) throws ManagerSaveException {
        super.updateEpicById(id, epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }
}
