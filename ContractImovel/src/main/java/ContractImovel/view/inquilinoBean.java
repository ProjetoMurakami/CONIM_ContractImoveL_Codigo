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
			String cpfUnformatado = CpfValidator.unformat(inquilino.getDocumento());
			if (!CpfValidator.isValid(cpfUnformatado)) {
				FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF inválido",
						"Informe um CPF válido (11 dígitos)."));
				return; 
			}

			inquilino.setDocumento(CpfValidator.format(cpfUnformatado));

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

	public List<TiposCliente> getTiposCliente() {
		return Arrays.asList(TiposCliente.values());
	}
}

