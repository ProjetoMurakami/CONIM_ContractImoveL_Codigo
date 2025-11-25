package ContractImovel.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.persistence.PersistenceException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ContractImovel.enums.TiposCliente;
import ContractImovel.model.Inquilino;
import ContractImovel.service.inquilinoService;
import ContractImovel.util.CpfValidator;


@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class inquilinoBean implements  Serializable{

    private static final long serialVersionUID = 1L;
	
	@Inject
	private inquilinoService inquilinoService;
	private Inquilino inquilino = new Inquilino();
	private List<Inquilino> inquilinos = new ArrayList<Inquilino>();
	
	@PostConstruct
	public void inicializar() {
		log.debug("init pesquisa"); 
		this.setInquilinos(inquilinoService.buscarTodos());
		limpar();
	}
	
	public void salvar() {
		try {
			inquilinoService.salvar(inquilino); 
			this.inquilinos = inquilinoService.buscarTodos();

			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
					"Inquilino salvo com sucesso."));
			limpar();
		} catch (PersistenceException e) {
			log.error("Erro ao salvar inquilino", e);
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar",
					"Verifique os dados e o log: " + e.getMessage()));
		} catch (Exception e) {
			log.error("Erro inesperado ao salvar inquilino", e);
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Erro inesperado: " + e.getMessage()));
		}
	}

	
	public void excluir() {
		try {
			inquilinoService.excluir(inquilino);
			this.inquilinos = inquilinoService.buscarTodos();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um problema", null));
		}
	}
		
	public void limpar() {
		this.inquilino = new Inquilino();
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
}

