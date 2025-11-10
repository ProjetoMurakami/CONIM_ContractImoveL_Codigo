package ContractImovel.view;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ContractImovel.model.Usuario;
import ContractImovel.enums.Role;
import ContractImovel.service.usuarioService;

@Named
@ViewScoped
public class usuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private usuarioService usuarioService;

    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;

    public void salvar() {
        try {
            usuarioService.salvar(usuario);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com sucesso!", null));
            usuario = new Usuario();
            usuarios = usuarioService.listarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage(), null));
        }
    }

    public void listar() {
        usuarios = usuarioService.listarTodos();
    }

    public Role[] getRoles() {
        return Role.values();
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Usuario> getUsuarios() { return usuarios; }
}