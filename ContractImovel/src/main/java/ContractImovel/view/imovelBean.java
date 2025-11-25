package ContractImovel.view;

import java.sql.SQLIntegrityConstraintViolationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import ContractImovel.enums.StatusImovel;
import ContractImovel.model.Imovel;
import ContractImovel.service.imovelService;

@Getter
@Setter
@Named
@ViewScoped
public class imovelBean implements  Serializable{

    private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(imovelBean.class);

	@Inject
	private imovelService imovelService;
	private Imovel imovel = new Imovel();
	private List<Imovel> imoveis = new ArrayList<Imovel>();
	
	@PostConstruct
	public void inicializar() {
		this.setImoveis(imovelService.buscarTodos());
		limpar();
	}
	
	public void salvar() {
		try {
			imovelService.salvar(imovel);
			imoveis = imovelService.buscarTodos();
			
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", 
					"Imóvel salvo com sucesso!"));
			limpar();
		} catch (Exception e) {
			LOGGER.error("Erro ao salvar imóvel", e);
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", 
					"Erro ao salvar imóvel: " + e.getMessage()));
		}
	}

	public void excluir() {
		try {
			imovelService.excluir(imovel);
			imoveis = imovelService.buscarTodos();
			
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Imóvel excluído com sucesso!"));
						
		} catch (Exception e) {
			LOGGER.error("Erro ao excluir imóvel", e);

			String msgUser = "Erro ao excluir imóvel: " + e.getMessage();
			
			if (e.getCause() != null) {
				Throwable cause = e.getCause();
				while (cause != null) {
					if (cause instanceof SQLIntegrityConstraintViolationException) {
						msgUser = "Não é possível excluir este imóvel, pois ele está associado a um contrato ativo.";
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
		this.imovel = new Imovel();
	}

	public SelectItem[] getStatusImovelOptions() {
		SelectItem[] options = new SelectItem[StatusImovel.values().length];
		int i = 0;
		for (StatusImovel status : StatusImovel.values()) {
			options[i++] = new SelectItem(status, status.toString());
		}
		return options;
	}
}
