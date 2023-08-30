package com.example.crudoperation.controller;

import org.springframework.ui.Model;
import com.example.crudoperation.entity.User;
import com.example.crudoperation.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(org.springframework.ui.Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "posts/user-singup-form";
    }
    @GetMapping("/LoginPage")
    public String loginPage(){
        return "posts/login-form";
    }
    @PostMapping("/register") // Add this annotation
    public String saveUserDetails(@ModelAttribute("user") User user,@RequestParam("role") String role, @RequestParam("password") String password,Model model){
        User users = userService.findByName(user.getUsername());
        if(users!=null){
            model.addAttribute("error","user name already present try with different one");
            return "posts/user-singup-form";
        }
        else{
            user.setEnabled(1);
            user.setRole("ROLE_"+role);
            user.setPassword("{noop}" + password);
            userService.save(user);
            return "redirect:/users/LoginPage";
        }
    }
}
