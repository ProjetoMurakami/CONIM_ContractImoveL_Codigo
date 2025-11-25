package ContractImovel.view;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
import ContractImovel.model.*;
import ContractImovel.service.*;

@Getter
@Setter
@Named
@ViewScoped
public class contratoLocacaoBean implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(contratoLocacaoBean.class);

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

    @PostConstruct
    public void inicializar() {
        contratos = contratoLocacaoService.buscarTodos();
        imoveis = imovelService.buscarTodos();
        imoveisDisponiveis = imovelService.buscarDisponiveis();
        inquilinos = inquilinoService.buscarTodos();
        fiadores = fiadorService.buscarTodos();
        corretores = usuarioService.listarTodos();
        contrato = new ContratoLocacao();
        fiador = new Fiador();
    }

    public void salvar() {
        try {
            contratoLocacaoService.salvar(contrato);
            contratos = contratoLocacaoService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Contrato salvo com sucesso", null));            
            limpar();
        } catch (Exception e) {
            LOGGER.error("Erro ao salvar contrato: ", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Erro ao salvar contrato: " + e.getMessage(), null));
        }
    }

    public void excluir(){
        try {
            contratoLocacaoService.excluir(contrato);
            contratos = contratoLocacaoService.buscarTodos();

            FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Contrato excluído com sucesso!"));
        } catch (Exception e) {
			LOGGER.error("Erro ao excluir contrato", e);

			String msgUser = "Erro ao excluir contrato: " + e.getMessage();
			
			if (e.getCause() != null) {
				Throwable cause = e.getCause();
				while (cause != null) {
					if (cause instanceof SQLIntegrityConstraintViolationException) {
						msgUser = "Não é possível excluir este contrato, pois ele está associado a um ou mais pagamentos ativos.";
						break;
					}
					cause = cause.getCause();
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", msgUser));
		}
    }

    public void carregarPagamentosDoContrato() {
        if (contrato != null && contrato.getId() != null) {
            try {
                pagamentosDoContrato = pagamentoService.buscarPorContrato(contrato.getId());
                LOGGER.info("Carregados " + pagamentosDoContrato.size() + " pagamentos para o contrato " + contrato.getId());
            } catch (Exception e) {
                LOGGER.error("Erro ao carregar pagamentos do contrato", e);
                pagamentosDoContrato = new ArrayList<>();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                        "Erro ao carregar pagamentos: " + e.getMessage()));
            }
        } else {
            pagamentosDoContrato = new ArrayList<>();
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

    public void limpar() {
        contrato = new ContratoLocacao();
        contrato.setCaucao(null); 
        pagamentosDoContrato = new ArrayList<>();
    }

    public void setContrato(ContratoLocacao contrato) {
        try {
            if (this.contrato.getCaucao() == null) {
                this.contrato.setCaucao(false);
            }
            if (contrato != null && contrato.getId() != null) {
                this.contrato = contratoLocacaoService.buscarPorId(contrato.getId());
                carregarPagamentosDoContrato();
            } else {
                this.contrato = contrato;
                this.pagamentosDoContrato = new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao carregar contrato", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                    "Erro ao carregar contrato: " + e.getMessage()));
        }
    }

    public boolean isCaucaoSelecionado() {
        return contrato != null && Boolean.FALSE.equals(contrato.getCaucao());
    }

    public void atualizarListaFiadores() {
        try {
            fiadores = fiadorService.buscarTodos();
        } catch (Exception e) {
            LOGGER.error("Erro ao atualizar lista de fiadores", e);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Não foi possível atualizar a lista de fiadores: " + e.getMessage()));
        }
    }
    
}