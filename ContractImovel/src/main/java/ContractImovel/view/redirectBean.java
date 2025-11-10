package ContractImovel.view;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class redirectBean implements Serializable {
    public void redirecionar() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
}
