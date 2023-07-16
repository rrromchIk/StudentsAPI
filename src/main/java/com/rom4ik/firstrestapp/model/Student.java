package com.rom4ik.firstrestapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rom4ik
 */
@Data
@NoArgsConstructor
public class Student {
    private int id;
    private String group;
    private String firstName;
    private String lastName;
    private String birthday;
    private String gender;
}
