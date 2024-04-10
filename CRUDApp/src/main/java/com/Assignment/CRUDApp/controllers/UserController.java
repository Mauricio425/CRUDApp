
package com.Assignment.CRUDApp.controllers;

import com.Assignment.CRUDApp.Users.User;
import com.Assignment.CRUDApp.Users.UserDto;
import com.Assignment.CRUDApp.services.UserRepository;
import jakarta.validation.Valid;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mauricio
 */

@Controller
//Becuase we need access to this controller at the url that ends with /users
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository repo; //Uses the interface?
    
    //this method will allow us to read the users from the database
    //We use getMapping b/c it should be available using the http get mapping
    //this means it will be available at "/users" or "/users/"
    @GetMapping({"", "/"})
    public String showUserList (Model model) {
        List<User> users = repo.findAll(Sort.by(Sort.Direction.DESC,"id")); //newest first
        model.addAttribute("users", users);
        return "users/users";
        //we need to create the "users.html" in the "users" folder which
        //should be available in the "templates" folder        
    }
        
    @GetMapping("/create")
    public String showCreatePage(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "users/createUser";
    }
    
    /*
    Method required an object of type UserDto which is the 
    object type bound to the form
    
    out object userDto will be filled using form data
    
    @valid validates data
    
    BindingResult allows us to check if there is an validation error
        with the data available in userDto
    */
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserDto userDto, //userDto is the user object from the form
            BindingResult result){
        
        //We need to check if an image file was submitted manually
        if(userDto.getImageFile().isEmpty()){
            result.addError(new FieldError("userDto", "imageFile", "Image file is required"));
        }
        
        if(result.hasErrors()){
            return "users/CreateUser";
        }
        
        //storing object in data base
        //save image file 38:00 explanation
        MultipartFile image = userDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = image.getOriginalFilename();
        
        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            
            try(InputStream inputStream = image.getInputStream()){
                Files.copy (inputStream, Paths.get (uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            
        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
        
        //storing object in db
        User user = new User();
        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setMajor(userDto.getMajor());
        user.setDob(createdAt);
        user.setImageFileName(storageFileName);
        
        //using the repo we will save the db
        repo.save(user);
        
        return "redirect:/users"; //after submit, redirect to users page
    } //stoppes on 40:45
    
}
