package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;

    private final HashMap<Integer, Node> nodes = new HashMap<>();

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            taskList.add(current.data);
            current = current.next;
        }
        return taskList;
    }

    public void add(Task task) {
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node newNode = linkLast(task);
        nodes.put(task.getId(), newNode);
    }

    public void remove(int id) {
        Node nodeToRemove = nodes.get(id);
        if (nodeToRemove == null) {
            return;
        }
        removeNode(nodeToRemove);
        nodes.remove(id);
    }

    private void removeNode(Node nodeToRemove) {
        if (nodeToRemove.prev == null && nodeToRemove.next == null) {
            head = null;
            tail = null;
        } else if (nodeToRemove.prev == null) {
            head = nodeToRemove.next;
            nodeToRemove.next.prev = null;
        } else if (nodeToRemove.next == null) {
            tail = nodeToRemove.prev;
            nodeToRemove.prev.next = null;
        } else {
            nodeToRemove.next.prev = nodeToRemove.prev;
            nodeToRemove.prev.next = nodeToRemove.next;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node {

        private Task data;
        private Node prev;
        private Node next;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}