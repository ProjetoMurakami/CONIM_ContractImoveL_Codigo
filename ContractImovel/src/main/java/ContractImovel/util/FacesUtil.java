package ContractImovel.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FacesUtil {

    public static void addInfoMessage(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", message);
    }

    public static void addWarnMessage(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, "Aviso", message);
    }

    public static void addErrorMessage(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, "Erro", message);
    }

    public static void addErrorMessage(String summary, String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, message);
    }

    private static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                   .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void redirect(String page) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            String contextPath = request.getContextPath();
            
            facesContext.getExternalContext()
                       .redirect(contextPath + page);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao redirecionar para: " + page, e);
        }
    }

    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance()
                          .getExternalContext()
                          .getRequestParameterMap()
                          .get(name);
    }

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }
}