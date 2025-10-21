package ContractImovel.view;

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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ContractImovel.enums.TiposCliente;
import ContractImovel.model.Inquilino;
import ContractImovel.service.inquilinoService;

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
		log.info(inquilino.toString());
		inquilinoService.salvar(inquilino);
		
		FacesContext.getCurrentInstance().
        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        		"O inquilino foi cadastrado", 
        		inquilino.toString()));
		
		log.info("Inquilino: " + inquilino.toString());
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
}

