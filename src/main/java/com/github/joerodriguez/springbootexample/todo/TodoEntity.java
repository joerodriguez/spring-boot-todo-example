package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.registration.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "todos")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String name;

    @Column
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
