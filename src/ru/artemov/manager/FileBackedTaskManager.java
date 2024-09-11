package ru.artemov.manager;

import ru.artemov.manager.history.HistoryManager;
import ru.artemov.tasks.Epic;
import ru.artemov.tasks.Status;
import ru.artemov.tasks.SubTask;
import ru.artemov.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory());
    private final File taskManagerFile;

    public static void main(String[] args) throws ManagerSaveException {

        Task task1 = new Task("task1", "desc1", Status.NEW, LocalDateTime.of(2023, 1, 1, 0, 0), Duration.of(34, ChronoUnit.MINUTES));
        Task task2 = new Task("task2", "desc2", Status.NEW, LocalDateTime.of(2023, 1, 2, 0, 0), Duration.of(3, ChronoUnit.MINUTES));
        Epic epic = new Epic("Epic1", "descrEpic1");
        SubTask subTask1 = new SubTask("SubTask1", "descSub1", Status.NEW, epic, LocalDateTime.of(2023, 1, 3, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask2 = new SubTask("SubTask2", "descSub2", Status.NEW, epic, LocalDateTime.of(2023, 1, 4, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        SubTask subTask3 = new SubTask("SubTask3", "descSub3", Status.NEW, epic, LocalDateTime.of(2023, 1, 5, 0, 0), Duration.of(30, ChronoUnit.MINUTES));
        Epic epic2 = new Epic("Epic2", "descrEpic2");

        SubTask subTask35 = new SubTask("subTask35", "Description", Status.IN_PROGRESS, epic2, LocalDateTime.of(2023, 1, 7, 0, 0), Duration.of(30, ChronoUnit.MINUTES));

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
        System.out.println(fileBackedTaskManager.getHistory());

        fileBackedTaskManager.loadFromFile();
        System.out.println("Load");

        System.out.println(fileBackedTaskManager.getAllTask());
        System.out.println(fileBackedTaskManager.getAllEpic());
        System.out.println(fileBackedTaskManager.getAllSubTask());


    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        this.taskManagerFile = new File("TaskManagerFile.txt");
    }

    void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter(taskManagerFile, false);) {
            writer.append("id,type,name,status,description,epic,startTime,duration,endTime" + "\n");

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

    public void loadFromFile() throws ManagerSaveException {
        load();
    }

    public void load() throws ManagerSaveException {
        fileBackedTaskManager.tasksById.clear();
        fileBackedTaskManager.subTaskById.clear();
        fileBackedTaskManager.epicById.clear();
        fileBackedTaskManager.historyManager.removeAll();

        try (BufferedReader br = new BufferedReader(new FileReader(fileBackedTaskManager.taskManagerFile, StandardCharsets.UTF_8));) {

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
                            new Task(params[2], params[4], Status.valueOf(params[3]), parsedId, LocalDateTime.parse(params[5]), Duration.parse(params[6])));
                    break;
                case "EPIC":
                    fileBackedTaskManager.epicById.put(parsedId, new Epic(params[2], params[4], parsedId));
                    break;
                case "SUBTASK":
                    SubTask sbFromFile = new SubTask(params[2], params[4],
                            Status.valueOf(params[3]), fileBackedTaskManager.epicById
                            .get(Integer.parseInt(params[5])), parsedId, LocalDateTime.parse(params[6]), Duration.parse(params[7]));
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
        return String.format("%d,%S,%s,%S,%s,%s,%s,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration(), task.getEndTime());
    }

    private String toString(SubTask task) {
        return String.format("%d,%S,%s,%S,%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription(), task.getEpic().getId(), task.getStartTime(), task.getDuration(), task.getEndTime());
    }

    private String toString(Epic task) {
        return String.format("%d,%S,%s,%S,%s,%s,%s,%s", task.getId(), task.getType(), task.getTitle(), task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration(), task.getEndTime());
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
