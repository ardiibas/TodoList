package id.todo.model;

/**
 * Created by Ibas on 20/04/2017.
 */

public class Todo {
    public final String id;
    public final String item;

    public Todo(String id, String item) {
        this.id = id;
        this.item = item;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Todo todo = (Todo) obj;
        return id.equals(todo.id);
    }
}
