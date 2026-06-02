package START.Web.Interceptor;

import START.Enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        boolean isLoggedIn = session != null && session.getAttribute("userId") != null;

        if (isPublicPage(uri)) {
            return true;
        }

        if (!isLoggedIn) {
            response.sendRedirect("/auth/login");
            return false;
        }

        if (isAdminPage(uri)) {
            UserRole role = (UserRole) session.getAttribute("role");

            if (role != UserRole.ADMIN) {
                response.sendRedirect("/burgers");
                return false;
            }
        }

        return true;
    }

    private boolean isPublicPage(String uri) {

        return uri.equals("/")
                || uri.startsWith("/auth")
                || uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/images")
                || uri.startsWith("/style")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".ico");
    }

    private boolean isAdminPage(String uri) {

        return uri.startsWith("/burgers/create")
                || uri.equals("/burgers/edit")
                || uri.endsWith("/edit")
                || uri.contains("/delete");
    }
}
