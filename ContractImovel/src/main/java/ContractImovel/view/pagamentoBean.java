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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import ContractImovel.model.Pagamento;
import ContractImovel.enums.FormasPagamento;
import ContractImovel.model.ContratoLocacao;
import ContractImovel.service.pagamentoService;
import ContractImovel.service.contratoLocacaoService;
import ContractImovel.util.FacesUtil;
@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class pagamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private pagamentoService pagamentoService;
    
    @Inject
    private contratoLocacaoService contratoService;

    private Pagamento pagamento = new Pagamento();
    private Long contratoId;
    private List<ContratoLocacao> contratos = new ArrayList<ContratoLocacao>();
    private List<Pagamento> pagamentos = new ArrayList<Pagamento>();

    @PostConstruct
    public void inicializar() {
        log.debug("init pesquisa");
        pagamentos = pagamentoService.buscarTodos();
        contratos = contratoService.buscarTodos();
        this.setPagamentos(pagamentos);
        this.setContratos(contratos);
        limpar();
    }

    public void onPreRender() {
        try {
            if (pagamento != null && pagamento.getId() != null) {
                Pagamento pagamentoExistente = pagamentoService.buscarPorId(pagamento.getId());
                if (pagamentoExistente != null) {
                    this.pagamento = pagamentoExistente;
                    System.out.println("Pagamento carregado: " + pagamentoExistente);
                    System.out.println("Contrato do pagamento: " + (pagamentoExistente.getContratoLocacao() != null ? 
                        pagamentoExistente.getContratoLocacao().getId() : "null"));
                }
            } else if (contratoId != null) {
                ContratoLocacao contrato = contratoService.buscarPorId(contratoId);
                if (contrato != null) {
                    if (pagamento == null) {
                        pagamento = new Pagamento();
                    }
                    pagamento.setContratoLocacao(contrato);
                    System.out.println("Contrato definido para novo pagamento: " + contrato.getId());
                }
            } else {
                if (pagamento == null) {
                    pagamento = new Pagamento();
                }
            }
            
            carregarPagamentos();
            
        } catch (Exception e) {
            System.err.println("Erro no onPreRender: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarPagamentos() {
        try {
            if (contratoId != null) {
                this.pagamentos = pagamentoService.buscarPorContrato(contratoId);
            } else {
                this.pagamentos = pagamentoService.buscarTodos();
            }
            System.out.println("Pagamentos carregados: " + pagamentos.size());
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao carregar pagamentos: " + e.getMessage());
            this.pagamentos = new ArrayList<>();
        }
    }

    public void salvar() {
        try {
            log.info("Iniciando o salvamento de pagamento: " + pagamento);
            
            pagamentoService.salvar(pagamento);
            log.info("Pagamento salvo com sucesso");

            pagamentos = pagamentoService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Pagamento salvo com sucesso", null));
            limpar();

        } catch (Exception e) {
            log.error("Erro ao salvar ppagamento: ", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Erro ao salvar pagamento: " + e.getMessage()));
        }
    }

    public void excluir() {
        try {
            if (pagamento != null && pagamento.getId() != null) {
                pagamentoService.excluir(pagamento);
                this.pagamentos = pagamentoService.buscarTodos(); 
            } else {
                FacesUtil.addErrorMessage("Nenhum pagamento selecionado para exclusão.");
            }
        } catch (Exception e) {
            e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um problema", null));
        }
    }

    public void limpar(){
        pagamento = new Pagamento();
    }
    
    public List<FormasPagamento> getFormasPagamento() {
		return Arrays.asList(FormasPagamento.values());
	}
}