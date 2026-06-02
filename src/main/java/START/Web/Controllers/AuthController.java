package START.Web.Controllers;

import START.Exception.InvalidCredentialsException;
import START.Exception.RegistrationConflictException;
import START.Models.User;
import START.Services.UserService;
import START.Web.DTOs.LoginRequest;
import START.Web.DTOs.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("register");
        mv.addObject("registerRequest", new RegisterRequest());

        return mv;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("registerRequest")
                                 RegisterRequest registerRequest,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            ModelAndView mv = new ModelAndView("register");
            mv.addObject("registerRequest", registerRequest);

            return mv;
        }

        try {
            userService.register(registerRequest);

        } catch (RegistrationConflictException ex) {
            ModelAndView mv = new ModelAndView("register");

            mv.addObject("registerRequest", registerRequest);
            mv.addObject("registerError", ex.getMessage());

            return mv;
        }

        return new ModelAndView("redirect:/auth/login");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("loginRequest", new LoginRequest());

        return mv;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute("loginRequest")
                              LoginRequest loginRequest,
                              BindingResult bindingResult,
                              HttpSession session) {

        if (bindingResult.hasErrors()) {

            ModelAndView mv = new ModelAndView("login");
            mv.addObject("loginRequest", loginRequest);

            return mv;
        }

        try {

            User user = userService.login(loginRequest);

            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

        } catch (InvalidCredentialsException ex) {
            ModelAndView mv = new ModelAndView("login");

            mv.addObject("loginRequest", loginRequest);
            mv.addObject("loginError", ex.getMessage());

            return mv;
        }

        return new ModelAndView("redirect:/burgers");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {

        session.invalidate();
        return new ModelAndView("redirect:/");
    }
}
