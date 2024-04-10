
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
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){
        
        try{
            //read user details from db
            User user = repo.findById(id).get(); 
            //returns an obj of type optional
            //.get converts it to User type
            model.addAttribute("user", user);//make accessible to page
            //Now me must create another userDto object, add it to the model
            //add it to the page and it will used to bind to the form
            
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setAge(user.getAge());
            userDto.setMajor(user.getMajor());
            
            model.addAttribute("userDto", userDto);
            
            
        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect: /users";
        }
        
        return "users/editUser";
    }
    
    
    /*
    * Model allows data to be sent to page
    * userDto is for data of the submited form
    * Binding result obj allows us to check if form data is valid
    */
    @PostMapping("/edit")
    public String updateUser(Model model, @RequestParam int id,
            @Valid @ModelAttribute UserDto userDto, BindingResult result){
        
        try {
            
            User user = repo.findById(id).get();
            model.addAttribute("user", user);
            
            if (result.hasErrors()){
                return "users/editUser";
            }
            
            //checking for new image
            if (!userDto.getImageFile().isEmpty()){
                //delete old image
                String uploadDir = "public/images";
                Path oldImagePath = Paths.get(uploadDir + user.getImageFileName());
                
                try{
                    Files.delete(oldImagePath);
                } catch(Exception ex) {
                    System.out.println("Exception: "  + ex.getMessage());
                }
                
                //save new image
                MultipartFile image = userDto.getImageFile();
                String storageFileName = image.getOriginalFilename();
                
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                
                //update in DB
                user.setImageFileName(storageFileName);      
            } 
            
            //update the rest object in the DB
            user.setName(userDto.getName());
            user.setAge(userDto.getAge());
            user.setMajor(userDto.getMajor());

            repo.save(user);
            
        } catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
        
        return "redirect:/users";
    }
    
    
}
