package com.example.todojavaflutterapp.Task;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/add")
    public Task addTask(@Valid @RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping
    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id){
        boolean exist = taskRepository.existsById(id);
        if (exist){
            Task entity = taskRepository.getReferenceById(id);
            boolean done = entity.isDone();
            entity.setDone(!done);
            taskRepository.save(entity);
            return new ResponseEntity<>("Task is updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not found",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        return taskRepository.findById(id)
                .map(entity -> {
                    taskRepository.delete(entity);
                    return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND));
    }
}

