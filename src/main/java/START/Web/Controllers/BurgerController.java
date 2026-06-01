package START.Web.Controllers;

import START.Services.BurgerService;
import START.Web.DTOs.BurgerRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/burgers")
public class BurgerController {

    private final BurgerService burgerService;

    public BurgerController(BurgerService burgerService) {
        this.burgerService = burgerService;
    }

    @GetMapping()
    public ModelAndView getAllBurgers() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("burgers");
        mv.addObject("burgers", burgerService.getAllAvailableBurgers());
        mv.addObject("currentPage", "burgers");

        return mv;
    }

    @GetMapping("/create")
    public ModelAndView createBurgerPage() {

        ModelAndView mv = new ModelAndView();
        mv.addObject("burgerRequest", new BurgerRequest());
        mv.addObject("currentPage", "burgers");
        mv.setViewName("burger-create");

        return mv;
    }

    @PostMapping("/create")
    public ModelAndView createBurger(@Valid @ModelAttribute("burgerRequest")
                                     BurgerRequest burgerRequest,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            ModelAndView mv = new ModelAndView("burger-create");
            mv.addObject("burgerRequest", burgerRequest);
            mv.addObject("currentPage", "burgers");

            return mv;
        }

        burgerService.createBurger(burgerRequest);
        return new ModelAndView("redirect:/burgers");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editBurgerPage(@PathVariable UUID id) {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("burger-edit");

        mv.addObject("burgerId", id);
        mv.addObject("burgerRequest", burgerService.getBurgerRequestById(id));
        mv.addObject("currentPage", "burgers");

        return mv;
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteBurger(@PathVariable UUID id) {

        burgerService.delete(id);
        return new ModelAndView("redirect:/burgers");
    }
}
