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
import ContractImovel.enums.StatusImovel;
import ContractImovel.model.Imovel;
import ContractImovel.service.imovelService;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class imovelBean implements  Serializable{

    private static final long serialVersionUID = 1L;
	
	@Inject
	private imovelService manterImovelService;
	private Imovel imovel = new Imovel();
	private List<Imovel> imoveis = new ArrayList<Imovel>();
	
	@PostConstruct
	public void inicializar() {
		log.debug("init pesquisa"); 
		this.setImoveis(manterImovelService.buscarTodos());
		limpar();
	}
	
	public void salvar() {
		log.info(imovel.toString());
		manterImovelService.salvar(imovel);
		
		FacesContext.getCurrentInstance().
        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        		"O imovel foi gravado com sucesso!", 
        		imovel.toString()));
		
		log.info("aluno: " + imovel.toString());
	}
	
	public void excluir() {
		try {
			manterImovelService.excluir(imovel);
			this.imoveis = manterImovelService.buscarTodos();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um problema", null));
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
