package START.GlobalExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ModelAndView handleIllegalArgument(RuntimeException exception) {

        ModelAndView mv = new ModelAndView("error");

        mv.addObject("message", exception.getMessage());
        return mv;
    }
}
