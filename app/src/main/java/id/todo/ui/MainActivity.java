package id.todo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import id.todo.app.R;
import id.todo.app.TodoApplication;
import id.todo.model.Todo;
import id.todo.repository.TodoRepository;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageButton buttonAdd;
    private ListView listView;
    private Toolbar toolbar;
    private ArrayAdapter<Todo> todoArrayAdapter;
    private List<Todo> todos;
    private TodoApplication app;
    private TodoRepository repo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (TodoApplication)getApplication();
        
        app.getRepository().open();
        repo = app.getRepository();
        todos = repo.findAll();
        
        setupView();
    }

    private void setupView() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonAdd = (ImageButton) findViewById(R.id.fab);
        listView= (ListView) findViewById(R.id.listview);
        todoArrayAdapter = new ArrayAdapter<Todo>(this,android.R.layout.simple_list_item_1, todos){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setText(getItem(position).item);
                return view;
            }
        };

//        setSupportActionBar(toolbar);
        listView.setAdapter(todoArrayAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,
                        AddItemActivity.class), 212);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentData = new Intent(MainActivity.this, AddItemActivity.class);
                intentData.putExtra("item",
                        ((Todo) adapterView.getItemAtPosition(i)).item);
                intentData.putExtra("id", ((Todo) adapterView.getItemAtPosition(i)).id);
                startActivityForResult(intentData, 213);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view,
                                           final int position, long l) {
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("warning");
                alert.setMessage("are you sure to delete it?");
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        processToDelete(((Todo) adapterView.getItemAtPosition(position)));
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });
    }

    void processToDelete(Todo todoItem) {
        repo.delete(todoItem);
        todos.remove(todoItem);
        todoArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&& requestCode==212){
//create new todo object
            Todo newTodo = new
                    Todo(UUID.randomUUID().toString(),data.getStringExtra("item"));
//insert todo object into database
            repo.insert(newTodo);
//update the UI
            todos.add(newTodo);
            todoArrayAdapter.notifyDataSetChanged();
        }
        if(resultCode==RESULT_OK && requestCode==213){
//create new todo object
            Todo todoUpdated = new
                    Todo(data.getStringExtra("id"),data.getStringExtra("item"));
//update the existing todo item
            repo.update(todoUpdated);
//get existing todo item from listview then update the value based on index
            if(todos.contains(todoUpdated)){
                todos.set(todos.indexOf(todoUpdated),todoUpdated);
                todoArrayAdapter.notifyDataSetChanged();
            }
        }
    }
}
