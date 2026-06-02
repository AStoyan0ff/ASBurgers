package START.GlobalExceptionHandler;

import START.Exception.ASBurgersException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ASBurgersException.class)
    public ModelAndView handleASBurgersException(ASBurgersException exception) {

        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", exception.getMessage());
        return mv;
    }
}
