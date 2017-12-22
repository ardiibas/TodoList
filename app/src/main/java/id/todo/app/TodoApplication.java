package id.todo.app;

import android.app.Application;

import id.todo.repository.LocalDatabaseConfiguration;
import id.todo.repository.TodoRepository;

/**
 * Created by Ibas on 21/04/2017.
 */

public class TodoApplication extends Application {

    private TodoRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        LocalDatabaseConfiguration localDatabaseConfiguration = new
                LocalDatabaseConfiguration(this, LocalDatabaseConfiguration.DATABASE_NAME,
                null, LocalDatabaseConfiguration.DATABASE_VERSION);
        repository = new TodoRepository(localDatabaseConfiguration);
    }

    public TodoRepository getRepository() {
        return  repository;
    }
}
