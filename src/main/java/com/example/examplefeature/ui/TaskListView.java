package com.example.examplefeature.ui;

import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Task List")
class TaskListView extends VerticalLayout {

    private final TaskService taskService;

    TaskListView(TaskService taskService) {
        this.taskService = taskService;
        Span text = new Span("Hello");
        add(text);


    }

}
