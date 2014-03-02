package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.registration.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<TodoEntity, Long> {
    Iterable<TodoEntity> findAllByUser(UserEntity user);
}
