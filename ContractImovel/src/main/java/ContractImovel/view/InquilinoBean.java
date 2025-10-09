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
import ContractImovel.enums.TiposDeCliente;
import ContractImovel.model.Inquilino;
import ContractImovel.service.ManterInquilinoService;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class InquilinoBean implements  Serializable{

    private static final long serialVersionUID = 1L;
	
	@Inject
	private ManterInquilinoService manterIquilinoService;
	private Inquilino inquilino = new Inquilino();
	private List<Inquilino> inquilino = new ArrayList<Inquilino>();
	
	@PostConstruct
	public void inicializar() {
		log.debug("init pesquisa"); 
		this.setImoveis(ManterInquilinoService.buscarTodos());
		limpar();
	}
	
	public void salvar() {
		log.info(inquilino.toString());
		ManterInquilinoService.salvar(inquilino);
		
		FacesContext.getCurrentInstance().
        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        		"O inquilino foi cadastrado", 
        		inquilino.toString()));
		
		log.info("Inquilino: " + imovel.toString());
	}
	
	public void excluir() {
		try {
			ManterInquilinoService.excluir(ManterInquilinoService);
			this.inquilinos = ManterInquilinoService.buscarTodos();
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


