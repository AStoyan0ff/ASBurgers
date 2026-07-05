package START.Web.Controllers;

import START.Services.BurgerService;
import START.Web.DTOs.BurgerRequest;
import START.Web.DTOs.BurgerResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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

    @GetMapping("/edit")
    public ModelAndView editBurgerPage(@RequestParam(required = false) UUID burgerId) {
        List<BurgerResponse> allBurgers = burgerService.getAllBurgersForAdmin();

        if (burgerId == null && !allBurgers.isEmpty()) {
            burgerId = allBurgers.getFirst().getId();
        }

        BurgerRequest burgerRequest = burgerId != null
                ? burgerService.getBurgerRequestById(burgerId)
                : new BurgerRequest();

        return buildEditView(burgerId, burgerRequest, allBurgers, null);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView legacyEditRedirect(@PathVariable UUID id) {
        return new ModelAndView("redirect:/burgers/edit?burgerId=" + id);
    }

    @PostMapping("/edit")
    public ModelAndView editBurger(@RequestParam UUID burgerId,
                                   @Valid @ModelAttribute("burgerRequest")
                                   BurgerRequest burgerRequest,
                                   BindingResult bindingResult) {

        List<BurgerResponse> allBurgers = burgerService.getAllBurgersForAdmin();

        if (bindingResult.hasErrors()) {
            return buildEditView(burgerId, burgerRequest, allBurgers, null);
        }

        burgerService.updateBurger(burgerId, burgerRequest);
        return new ModelAndView("redirect:/burgers");
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteBurger(@PathVariable UUID id) {

        burgerService.delete(id);
        return new ModelAndView("redirect:/burgers");
    }

    private ModelAndView buildEditView(UUID burgerId,
                                       BurgerRequest burgerRequest,
                                       List<BurgerResponse> allBurgers,
                                       String editError) {

        ModelAndView mv = new ModelAndView("burger-edit");

        mv.addObject("burgerId", burgerId);
        mv.addObject("burgerRequest", burgerRequest);
        mv.addObject("allBurgers", allBurgers);
        mv.addObject("currentPage", "burgers");

        if (editError != null) {
            mv.addObject("editError", editError);
        }

        return mv;
    }
}
