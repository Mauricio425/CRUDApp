
package com.Assignment.CRUDApp.Users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mauricio
 * 
 * The purpose of UserDto (Data transfer object) is to act as a temporary
 * object (User) so that we can update previous User with the new User 
 */
public class UserDto {
    @NotEmpty(message = "Name Required")
    private String name;
    
    @Min (0)
    private int age;
    
    @NotEmpty(message = "Major Required")
    private String major;
    
    private MultipartFile imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
    
    
}
