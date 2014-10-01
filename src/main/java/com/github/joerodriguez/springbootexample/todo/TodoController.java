package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.registration.UserEntity;
import com.sun.tools.javac.comp.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todo-list")
public class TodoController {

    @Autowired
    TodoService todoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, UserEntity currentUser) {
        model.addAttribute("todos", todoService.forUser(currentUser));
        return "todo/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(
            String name,
            UserEntity currentUser,
            RedirectAttributes redirectAttributes
    ) {
        todoService.create(name, currentUser);
        redirectAttributes.addFlashAttribute("alertSuccess", "Todo created");
        return "redirect:/todo-list";
    }

    @RequestMapping(value = "/{todoId}", method = RequestMethod.DELETE)
    public String complete(
            @PathVariable long todoId,
            UserEntity currentUser,
            RedirectAttributes redirectAttributes
    ) {
        TodoEntity todo = todoService.findOne(todoId);
        todoService.delete(todo, currentUser);
        redirectAttributes.addFlashAttribute("alertSuccess", todo.getName() + " successfully completed");
        return "redirect:/todo-list";
    }

    @RequestMapping(value = "/{todoId}", method = { RequestMethod.GET} )
    public String get(
            @PathVariable long todoId,
            Model model
    ) {
        TodoEntity todo = todoService.findOne(todoId);
        model.addAttribute("todo", todo);
        return "todo/edit";
    }

    @RequestMapping(value = "/{todoId}", method = { RequestMethod.PUT} )
    public String edit(
            @PathVariable long todoId,
            String edit_name,
            UserEntity currentUser,
            RedirectAttributes redirectAttributes
    ) {
        TodoEntity todo = todoService.findOne(todoId);
        todo.setName(edit_name);
        todoService.edit(todo, currentUser);
        redirectAttributes.addFlashAttribute("alertSuccess", todo.getName() + " successfully updated");
        return "redirect:/todo-list";
    }
}
