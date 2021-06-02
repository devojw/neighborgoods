package org.launchcode.neighborgoods.controllers;

import org.launchcode.neighborgoods.models.User;
import org.launchcode.neighborgoods.models.data.UserRepository;
import org.launchcode.neighborgoods.models.dto.LoginFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {


    @Autowired
    UserRepository userRepository;
    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("username", user.getUsername());
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Login");
            return "login";
        }

        User existingUser = userRepository.findByUsername(loginFormDTO.getUsername());

        if (existingUser == null) {
            errors.rejectValue("username", "username.isEmpty()", "Please enter a valid user name");
            model.addAttribute("title", "Login");
            return "login";
        }

        String password = loginFormDTO.getPassword();
        String verifyPassword = loginFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Login");
            return "login";
        }

        String email = loginFormDTO.getEmail();
        String verifyEmail= loginFormDTO.getVerifyEmail();
        if (!email.equals(verifyEmail)) {
            errors.rejectValue("email", "email.mismatch", "Email address does not match");
            model.addAttribute("title", "Login");
            return "login";
        }

//        User newUser = new User(loginFormDTO.getUsername(), loginFormDTO.getEmail(), loginFormDTO.getPassword());
//        userRepository.save(newUser);
//        setUserInSession(request.getSession(), newUser);



        return "redirect:/index";
    }
}
