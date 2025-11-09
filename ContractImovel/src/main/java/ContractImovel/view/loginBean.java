package ContractImovel.view;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ContractImovel.model.Usuario;
import ContractImovel.service.usuarioService;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
@Named
@SessionScoped
public class loginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(loginBean.class.getName());

    private String username;
    private String senha;
    private Usuario usuarioLogado;

    @Inject
    private usuarioService usuarioService;

    public void login() {
        LOGGER.log(Level.INFO, "Tentando login para usuário: {0}", username);
        try {
            usuarioLogado = usuarioService.autenticar(username, senha);

            if (usuarioLogado != null) {
                LOGGER.log(Level.INFO, "Login bem-sucedido! Usuário: {0}, Role: {1}",
                        new Object[]{usuarioLogado.getUsername(), usuarioLogado.getRole()});
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogado", usuarioLogado); // <<< ADICIONE
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Login realizado com sucesso!", null));
                redirecionar("/index.xhtml");
            } else {
                LOGGER.warning("Falha no login: credenciais inválidas.");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos!", null));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao tentar login", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro interno ao tentar login!", null));
        }
    }

    public String logout() {
        LOGGER.info("Logout executado.");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    private void redirecionar(String url) {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + url);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao redirecionar", e);
        }
    }
}
