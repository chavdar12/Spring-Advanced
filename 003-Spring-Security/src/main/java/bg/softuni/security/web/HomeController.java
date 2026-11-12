package bg.softuni.security.web;

import bg.softuni.security.user.UserRegisterDTO;
import bg.softuni.security.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterDTO user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && !userService.register(user)) {
            bindingResult.rejectValue("username", "taken", "This username is already taken.");
        }
        return bindingResult.hasErrors() ? "register" : "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "profile";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("usernames", userService.allUsernames());
        return "users";
    }

    @PostMapping("/admin/users/{username}/delete")
    public String delete(@PathVariable String username) {
        userService.delete(username);
        return "redirect:/admin/users";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
