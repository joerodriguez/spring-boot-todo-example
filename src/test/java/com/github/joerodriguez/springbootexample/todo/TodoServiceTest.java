package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.Application;
import com.github.joerodriguez.springbootexample.registration.UserEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class TodoServiceTest {

    @InjectMocks
    TodoService todoService;

    @Mock
    TodoRepository todoRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testForUser() throws Exception {
        UserEntity user = new UserEntity();
        todoService.forUser(user);
        verify(todoRepository).findAllByUser(user);
    }

    @Test
    public void testCreate() throws Exception {
        UserEntity user = new UserEntity();

        todoService.create("cut grass", user);

        ArgumentCaptor<TodoEntity> todoEntityArgumentCaptor = ArgumentCaptor.forClass(TodoEntity.class);
        verify(todoRepository).save(todoEntityArgumentCaptor.capture());

        TodoEntity todo = todoEntityArgumentCaptor.getValue();
        assertThat(todo.getName(), equalTo("cut grass"));
        assertThat(todo.getUser(), equalTo(user));
    }

    @Test
    public void testFindOne() throws Exception {
        todoService.findOne(123L);
        verify(todoRepository).findOne(123L);
    }

    @Test
    public void testMarkAsComplete() throws Exception {
        UserEntity bob = new UserEntity();
        bob.setId(1);
        UserEntity sally = new UserEntity();
        sally.setId(2);
        TodoEntity bobTodo = new TodoEntity();
        bobTodo.setUser(bob);

        todoService.delete(bobTodo, sally);
        verify(todoRepository, never()).delete(bobTodo);
        todoService.delete(bobTodo, bob);
        verify(todoRepository).delete(bobTodo);
    }

    @Ignore
    public void testEdit() throws Exception {
        UserEntity bob = new UserEntity();
        bob.setId(1);
        UserEntity sally = new UserEntity();
        sally.setId(2);
        TodoEntity bobTodo = new TodoEntity();
        bobTodo.setUser(bob);

        todoService.edit(bobTodo, sally);
        verify(todoRepository, never()).save(bobTodo);
        todoService.delete(bobTodo, bob);
        verify(todoRepository).save(bobTodo);
    }
}
