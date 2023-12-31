 package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.CategoryDTO;
import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.service.definitions.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(categoryService.save(categoryDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categoryDTOs = categoryService.findAll();
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<CategoryDTO> getCategoryByName(@RequestParam String name) throws EntityNotFoundException {
        return new ResponseEntity<>(categoryService.findByName(name), HttpStatus.OK);
    }

    @GetMapping(path = "/events/{id}")
    public ResponseEntity<List<EventDTO>> getCategoryEvents(@PathVariable long id) throws EntityNotFoundException {
        List<EventDTO> eventDTOs = categoryService.getEvents(id);
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeCategory(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        categoryService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
