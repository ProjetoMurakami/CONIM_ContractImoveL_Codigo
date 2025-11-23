package ContractImovel.view;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ContractImovel.model.Usuario;
import ContractImovel.enums.Role;
import ContractImovel.service.usuarioService;
import ContractImovel.util.CpfValidator;
import ContractImovel.util.FacesUtil;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class usuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private usuarioService usuarioService;

    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;

    @PostConstruct
    public void inicializar() {
        log.debug("init pesquisa");
        usuarios = usuarioService.listarTodos();
        this.setUsuarios(usuarios);
    }

    public void salvar() {
        try {
            if(usuario.getCpf() != null && !usuario.getCpf().isEmpty()){
                String cpfLimpo = CpfValidator.unformat(usuario.getCpf());
                if(!CpfValidator.isValid(cpfLimpo)){
                    FacesUtil.addErrorMessage("CPF inválido!");
                    return;
                }
                usuario.setCpf(cpfLimpo);
            }
            usuarioService.salvar(usuario);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com sucesso!", null));
            limpar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage(), null));
        }
    }

    public void excluir() {
        try {
            if (usuario != null && usuario.getId() != null) {
                usuarioService.excluir(usuario);
                this.usuarios = usuarioService.listarTodos(); 
            } else {
                FacesUtil.addErrorMessage("Nenhum Usuario selecionado para exclusão.");
            }
        } catch (Exception e) {
            e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um problema", null));
        }
    }

    public List<Usuario> listar() {
        usuarios = usuarioService.listarTodos();
        return usuarios;
    }

    public Role[] getRoles() {
        return Role.values();
    }

    public void limpar() {
		this.usuario = new Usuario();
	}
}