package ru.artemov.manager.history;

import ru.artemov.tasks.Task;


import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history;

    private final Map<Integer, Node<Task>> nodeById;

    public InMemoryHistoryManager() {
        this.history = new CustomLinkedList<>();
        this.nodeById = new HashMap<>();
    }


    @Override
    public void addTaskIdInHistory(Task task) {

        if (nodeById.containsKey(task.getId())) {
            remove(task.getId());
        }

        Node<Task> newNode = history.linkLast(task);
        nodeById.put(task.getId(), newNode);


    }


    @Override
    public void remove(int id) {
        if (nodeById.containsKey(id)) {
            history.removeNode(nodeById.remove(id));
            nodeById.remove(id);

        }

    }

    @Override
    public void removeAll() {
        for (Node<Task> node:nodeById.values()){
            history.removeNode(node);
        }
        nodeById.clear();

    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }


    public static final class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public Node<T> linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
            return newNode;
        }

        public List<T> getTasks() {
            List<T> list = new ArrayList<>();
            Node<T> current = head;
            while (current != null) {
                list.add(current.item);
                current = current.next;
            }
            return list;
        }

        public void removeNode(Node<T> node) {
            if (size == 1) {
                head = null;
                tail = null;
                node.item = null;
            } else if (size > 1) {
                if (node.prev == null) {
                    head = node.next;
                    node.next.prev = null;
                    node.next = null;
                    node.item = null;
                } else if (node.next == null) {
                    tail = node.prev;
                    node.prev.next = null;
                    node.prev = null;
                    node.item = null;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    node.next = null;
                    node.prev = null;
                    node.item = null;
                }
            }
            if (size != 0) {
                size--;
            }

        }


    }
}
