package ContractImovel.view;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import ContractImovel.model.Usuario;
import ContractImovel.enums.Role;
import ContractImovel.service.usuarioService;
import ContractImovel.util.CpfValidator;
import ContractImovel.util.FacesUtil;

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
                usuarios = usuarioService.listarTodos();
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
			String msgUser = "Erro ao excluir Usuário: " + e.getMessage();
			
			if (e.getCause() != null) {
				Throwable cause = e.getCause();
				while (cause != null) {
					if (cause instanceof SQLIntegrityConstraintViolationException) {
						msgUser = "Não é possível excluir este usuário, pois ele está associado a um contrato ativo.";
						break;
					}
					cause = cause.getCause();
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", msgUser));
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