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
    @Inject
    private usuarioService usuarioService;

    private List<ContratoLocacao> contratos = new ArrayList<ContratoLocacao>();
    private List<Imovel> imoveis = new ArrayList<Imovel>();
    private List<Imovel> imoveisDisponiveis = new ArrayList<Imovel>();
    private List<Inquilino> inquilinos = new ArrayList<Inquilino>();
    private List<Fiador> fiadores = new ArrayList<Fiador>();
    private List<Pagamento> pagamentosDoContrato = new ArrayList<Pagamento>();
    private List<Usuario> corretores = new ArrayList<Usuario>();

    private ContratoLocacao contrato = new ContratoLocacao();
    private Fiador fiador = new Fiador();
    private Pagamento pagamentoSelecionado = new Pagamento(); // ✅ NOVO

    @PostConstruct
    public void inicializar() {
        carregarDados();
    }

    private void carregarDados() {
        contratos = contratoLocacaoService.buscarTodos();
        imoveis = imovelService.buscarTodos();
        imoveisDisponiveis = imovelService.buscarDisponiveis();
        inquilinos = inquilinoService.buscarTodos();
        fiadores = fiadorService.buscarTodos();
        corretores = usuarioService.listarTodos();
        contrato = new ContratoLocacao();
        fiador = new Fiador();
        pagamentoSelecionado = new Pagamento();
    }

    public void carregarPagamentosDoContrato() {
        if (contrato != null && contrato.getId() != null) {
            try {
                pagamentosDoContrato = pagamentoService.buscarPorContrato(contrato.getId());
                log.info("Carregados " + pagamentosDoContrato.size() + " pagamentos para o contrato " + contrato.getId());
            } catch (Exception e) {
                log.error("Erro ao carregar pagamentos do contrato", e);
                pagamentosDoContrato = new ArrayList<>();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                        "Erro ao carregar pagamentos: " + e.getMessage()));
            }
        } else {
            pagamentosDoContrato = new ArrayList<>();
        }
    }

    public void selecionarPagamento(Pagamento pagamento) {
        this.pagamentoSelecionado = pagamento;
        log.info("Pagamento selecionado para edição: " + pagamento.getId());
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
        pagamentosDoContrato = new ArrayList<>();
        pagamentoSelecionado = new Pagamento();
    }

    public void setContrato(ContratoLocacao contrato) {
        try {
            if (contrato != null && contrato.getId() != null) {
                this.contrato = contratoLocacaoService.buscarPorId(contrato.getId());
                carregarPagamentosDoContrato();
            } else {
                this.contrato = contrato;
                this.pagamentosDoContrato = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Erro ao carregar contrato", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                    "Erro ao carregar contrato: " + e.getMessage()));
        }
    }
}