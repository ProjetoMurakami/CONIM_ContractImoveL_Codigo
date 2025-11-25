package ContractImovel.view;

import ContractImovel.model.Fiador;
import ContractImovel.enums.TiposCliente;
import ContractImovel.service.fiadorService;
import ContractImovel.util.CpfValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.persistence.PersistenceException;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class fiadorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private fiadorService fiadorService;
    
    @Inject
    private contratoLocacaoBean contratoLocacaoBean;

    private Fiador fiador = new Fiador();
    private List<Fiador> fiadores = new ArrayList<Fiador>();
    
    @PostConstruct
    public void inicializar() {
        log.debug("Inicializando fiadorBean");
        this.setFiadores(fiadorService.buscarTodos());
        Arrays.asList(TiposCliente.values());
        limpar();
    }

    public void salvar() {
        try {
            fiadorService.salvar(fiador);
            this.fiadores = fiadorService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                    "Fiador salvo com sucesso."));
            
            // Atualiza a lista de fiadores no contratoLocacaoBean se necessário
            if (contratoLocacaoBean != null) {
                contratoLocacaoBean.atualizarListaFiadores();
            }

            limpar();

            // Fechar o dialog se estiver sendo usado
            PrimeFaces.current().executeScript("PF('editarFiadorDialogWidgetVar').hide();");

        } catch (PersistenceException e) {
            log.error("Erro ao salvar fiador", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar",
                    "Verifique os dados e o log: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Erro inesperado ao salvar fiador", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Erro inesperado: " + e.getMessage()));
        }
    }

    public void excluir() {
        try {
            fiadorService.excluir(fiador);
            this.fiadores = fiadorService.buscarTodos();
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                    "Fiador excluído com sucesso."));
                    
        } catch (Exception e) {
            log.error("Erro ao excluir fiador", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao excluir fiador", 
                    "Ocorreu um problema: " + e.getMessage()));
        }
    }

    public void limpar() {
        this.fiador = new Fiador();
    }

    public void limparFormulario() {
        limpar();
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