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
import ContractImovel.enums.FormasPagamento;
import ContractImovel.model.Pagamento;
import ContractImovel.service.pagamentoService;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class pagamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private pagamentoService pagamentoService;
    
    private Pagamento pagamento = new Pagamento();
    private List<Pagamento> pagamentos = new ArrayList<Pagamento>();
    
    @PostConstruct
    public void inicializar() {
        log.debug("init pesquisa pagamentos"); 
        this.setPagamentos(pagamentoService.listarTodos());
        limpar();
    }
    
    public void salvar() {
        log.info(pagamento.toString());
        pagamentoService.salvar(pagamento);
        
        this.pagamentos = pagamentoService.listarTodos();
        
        FacesContext.getCurrentInstance()
            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "O pagamento foi gravado com sucesso!", 
                pagamento.toString()));
        
        log.info("pagamento salvo: " + pagamento.toString());
        limpar(); 
    }
    
    public void excluir() {
        try {
            pagamentoService.excluir(pagamento);
            this.pagamentos = pagamentoService.listarTodos();
            limpar();
            
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Pagamento excluído com sucesso!", null));
                    
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um problema ao excluir o pagamento", null));
        }
    }
    
    public void excluirPorId(Long id) {
        try {
            pagamentoService.excluirPorId(id);
            this.pagamentos = pagamentoService.listarTodos();
            
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Pagamento excluído com sucesso!", null));
                    
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um problema ao excluir o pagamento", null));
        }
    }
    
    public void limpar() {
        this.pagamento = new Pagamento();
    }
    
    public void selecionarPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
    
    public void buscarPorId() {
        if (pagamento.getId() != null) {
            try {
                this.pagamento = pagamentoService.buscarPorId(pagamento.getId());
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Pagamento encontrado!", null));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Pagamento não encontrado com o ID: " + pagamento.getId(), null));
                limpar();
            }
        }
    }

    public List<FormasPagamento> getFormasPagamento() {
        return Arrays.asList(FormasPagamento.values());
    }

    
}