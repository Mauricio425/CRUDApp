
package com.Assignment.CRUDApp.Users;

import jakarta.persistence.*;
import java.util.Date;




/**
 *
 * @author Mauricio
 */

@Entity //This class is a model that allows us to create a class in the db
@Table(name = "users") //becuase we want to call table in the db "users"
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    private String name;
    private int age;
    
    @Column(columnDefinition = "TEXT")
    private String major;
    private Date dob;
    private String imageFileName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
    
    
}
