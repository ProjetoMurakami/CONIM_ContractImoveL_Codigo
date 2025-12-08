package ContractImovel.view;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;

import ContractImovel.model.Fiador;
import ContractImovel.enums.TiposCliente;
import ContractImovel.service.fiadorService;
import ContractImovel.util.CpfValidator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@ViewScoped
public class fiadorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(fiadorBean.class);


    @Inject
    private fiadorService fiadorService;
    
    @Inject
    private contratoLocacaoBean contratoLocacaoBean;

    private Fiador fiador = new Fiador();
    private List<Fiador> fiadores = new ArrayList<Fiador>();
    
    @PostConstruct
    public void inicializar() {
        this.setFiadores(fiadorService.buscarTodos());
        Arrays.asList(TiposCliente.values());
        limpar();
    }

    public void salvar() {
        try {
            fiadorService.salvar(fiador);
            fiadores = fiadorService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                    "Fiador salvo com sucesso."));
            if (contratoLocacaoBean != null) {
                contratoLocacaoBean.atualizarListaFiadores();
            }
            limpar();

            PrimeFaces.current().executeScript("PF('editarFiadorDialogWidgetVar').hide();");
        } catch (Exception e) {
            LOGGER.error("Erro ao salvar fiador", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Erro inesperado: " + e.getMessage()));
        }
    }

    public void excluir() {
        try {
            fiadorService.excluir(fiador);
            fiadores = fiadorService.buscarTodos();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                    "Fiador excluído com sucesso."));
                    
        } catch (Exception e) {
			LOGGER.error("Erro ao excluir Fiador", e);

			String msgUser = "Erro ao excluir Fiador: " + e.getMessage();
			
			if (e.getCause() != null) {
				Throwable cause = e.getCause();
				while (cause != null) {
					if (cause instanceof SQLIntegrityConstraintViolationException) {
						msgUser = "Não é possível excluir este Fiador, pois ele está associado a um contrato ativo.";
						break;
					}
					cause = cause.getCause();
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", msgUser));
		}
    }

    public void limpar() {
        fiador = new Fiador();
    }

    public void validarDocumento(FacesContext ctx, UIComponent comp, Object value) {
		String doc = CpfValidator.unformat(value.toString());

		if (doc.length() == 11) {
			if (!CpfValidator.isValidCPF(doc)) {
				throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"CPF inválido!", null));
			}
		} else if (doc.length() == 14) {
			if (!CpfValidator.isValidCNPJ(doc)) {
				throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"CNPJ inválido!", null));
			}
		} else {
			throw new ValidatorException(new FacesMessage(
				FacesMessage.SEVERITY_ERROR,
				"Documento deve conter 11 (CPF) ou 14 dígitos (CNPJ).", null));
		}
	}

	public String labelCategoria(TiposCliente categoria) {
		if (categoria == null) return "";
		switch (categoria) {
			case PESSOA_FISICA: return "Pessoa Física";
			case PESSOA_JURIDICA: return "Pessoa Jurídica";
			default: return categoria.name();
		}
	}

    public List<TiposCliente> getTiposCliente() {
        return Arrays.asList(TiposCliente.values());
    }
    
    public String voltar() {
        return "/cadastro/CadastroContrato.xhtml?faces-redirect=true";
    }
}