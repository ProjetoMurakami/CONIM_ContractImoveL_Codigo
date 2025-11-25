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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

import ContractImovel.model.Pagamento;
import ContractImovel.enums.FormasPagamento;
import ContractImovel.model.ContratoLocacao;
import ContractImovel.service.pagamentoService;
import ContractImovel.service.contratoLocacaoService;
import ContractImovel.util.FacesUtil;
@Getter
@Setter
@Named
@ViewScoped
public class pagamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(pagamentoBean.class);

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
                }
            } else if (contratoId != null) {
                ContratoLocacao contrato = contratoService.buscarPorId(contratoId);
                if (contrato != null) {
                    if (pagamento == null) {
                        pagamento = new Pagamento();
                    }
                    pagamento.setContratoLocacao(contrato);
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
            pagamentoService.salvar(pagamento);

            pagamentos = pagamentoService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Pagamento salvo com sucesso", null));
            limpar();

        } catch (Exception e) {
            LOGGER.error("Erro ao salvar ppagamento: ", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Erro ao salvar pagamento: " + e.getMessage()));
        }
    }

    public void excluir() {
        try {
            pagamentoService.excluir(pagamento);
			pagamentos = pagamentoService.buscarTodos();

			FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                    "Inquilino excluído com sucesso."));
                   
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