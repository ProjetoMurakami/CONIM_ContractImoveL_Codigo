package ContractImovel.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import ContractImovel.model.*;
import ContractImovel.service.*;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class contratoLocacaoBean implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Inject
    private contratoLocacaoService contratoLocacaoService;
    @Inject
    private imovelService imovelService;
    @Inject
    private pagamentoService pagamentoService;
    @Inject
    private inquilinoService inquilinoService;
    @Inject
    private fiadorService fiadorService;

    private List<ContratoLocacao> contratos = new ArrayList<ContratoLocacao>();
    private List<Imovel> imoveis = new ArrayList<Imovel>();
    private List<Imovel> imoveisDisponiveis = new ArrayList<Imovel>();
    private List<Inquilino> inquilinos = new ArrayList<Inquilino>();
    private List<Fiador> fiadores = new ArrayList<Fiador>();

    private ContratoLocacao contrato = new ContratoLocacao();
    private Fiador fiador = new Fiador();

    @PostConstruct
    public void inicializar() {
        contratos = contratoLocacaoService.buscarTodos();
        imoveis = imovelService.buscarTodos();
        imoveisDisponiveis = imovelService.buscarDisponiveis();
        inquilinos = inquilinoService.buscarTodos();
        fiadores = fiadorService.buscarTodos();
        contrato = new ContratoLocacao();
        fiador = new Fiador();
    }

    public void salvar() {
        try {
            log.info("Iniciando o salvamento do contrato: " + contrato);

            contratoLocacaoService.salvar(contrato);
            log.info("Contrato salvo com sucesso no banco.");

            contratos = contratoLocacaoService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Contrato salvo com sucesso", null));            
            limpar();

        } catch (Exception e) {
            log.error("Erro ao salvar contrato: ", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro ao salvar contrato: " + e.getMessage(), null));
        }
    }

    public void salvarFiador() {
        try {
            fiadorService.salvar(fiador);
            fiadores = fiadorService.buscarTodos();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Fiador salvo com sucesso", null));
            fiador = new Fiador();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro ao salvar fiador: " + e.getMessage(), null));
        }
    }

    public void abrirDialogFiador() {
        PrimeFaces.current().executeScript("PF('dialogCadastroFiador').show()");
    }

    public void limpar() {
        contrato = new ContratoLocacao();
    }

    public void setContrato(ContratoLocacao contrato) {
    try {
        if (contrato != null && contrato.getId() != null) {
            this.contrato = contratoLocacaoService.buscarPorId(contrato.getId());
        } else {
            this.contrato = contrato;
        }
    } catch (Exception e) {
        log.error("Erro ao carregar contrato", e);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao carregar contrato: " + e.getMessage()));
    }
}
}
