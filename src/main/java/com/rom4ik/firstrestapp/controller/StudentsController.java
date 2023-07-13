package com.rom4ik.firstrestapp.controller;

import com.rom4ik.firstrestapp.dao.StudentDAO;
import com.rom4ik.firstrestapp.exception.StudentCRUDException;
import com.rom4ik.firstrestapp.exception.StudentNotFoundException;
import com.rom4ik.firstrestapp.model.Student;
import com.rom4ik.firstrestapp.response.StudentErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rom4ik
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StudentsController {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentsController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @GetMapping("/students")
    public List<Student> getAll() {
        return studentDAO.findAll();
    }

    @GetMapping("/students/{id}")
    public Student getById(@PathVariable int id) {
        return studentDAO.findById(id);
    }

    @PostMapping("/students")
    public ResponseEntity<HttpStatus> createStudent(@RequestBody @Valid Student student,
                                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new StudentCRUDException(getErrorMessages(bindingResult));
        }
        studentDAO.create(student);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable int id) {
        studentDAO.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<HttpStatus> updateStudent(@RequestBody @Valid Student student,
                                                    BindingResult bindingResult,
                                                    @PathVariable int id) {
        if(bindingResult.hasErrors()) {
            throw new StudentCRUDException(getErrorMessages(bindingResult));
        }
        studentDAO.update(student, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse(
                "Student with such id not found"
        );
        return new ResponseEntity<>(studentErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<StudentErrorResponse> handleException(StudentCRUDException exc) {
        StudentErrorResponse studentErrorResponse = new StudentErrorResponse(
                exc.getMessage()
        );
        return new ResponseEntity<>(studentErrorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getErrorMessages(BindingResult bindingResult) {
        StringBuilder errMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for(FieldError fieldError : errors) {
            errMsg.append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage()).append(";");
        }
        return errMsg.toString();
    }
}
