package ContractImovel.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final String[] EXCLUDED_PATHS = {
        "/login.xhtml",
        "/javax.faces.resource/",
        "/resources/",
        "/css/",
        "/js/",
        "/images/"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        boolean excluded = false;
        for (String s : EXCLUDED_PATHS) {
            if (path.contains(s)) {
                excluded = true;
                break;
            }
        }

        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("usuarioLogado") : null;

        if (!excluded && user == null) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}
