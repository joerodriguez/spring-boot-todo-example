package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.registration.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    public Iterable<TodoEntity> forUser(UserEntity user) {
        return todoRepository.findAllByUser(user);
    }

    public TodoEntity create(String name, UserEntity user) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setName(name);
        todoEntity.setUser(user);

        return todoRepository.save(todoEntity);
    }

    public TodoEntity findOne(long id) {
        return todoRepository.findOne(id);
    }

    public TodoEntity delete(TodoEntity todo, UserEntity user) {
        if (todo.getUser().getId() == user.getId())
            todoRepository.delete(todo);
        return todo;
    }

    public TodoEntity edit(TodoEntity todo, UserEntity user) {
        if (todo.getUser().getId() == user.getId())
            todoRepository.save(todo);
        return todo;
    }
}
