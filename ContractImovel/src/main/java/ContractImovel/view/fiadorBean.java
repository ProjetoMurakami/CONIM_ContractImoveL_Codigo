package ContractImovel.view;

import ContractImovel.model.Fiador;
import ContractImovel.enums.TiposCliente;
import ContractImovel.service.fiadorService;
import ContractImovel.util.CpfValidator;
import ContractImovel.util.FacesUtil;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter 
@Setter
@Named
@ViewScoped
public class fiadorBean implements Serializable {

    @Inject
    private fiadorService fiadorService;

   
    private Fiador fiador;

    private List<TiposCliente> tiposCliente;

    @PostConstruct
    public void init() {
        this.fiador = new Fiador();
        this.tiposCliente = Arrays.asList(TiposCliente.values());
    }

    public void salvar() {
    try {
            if (fiador.getDocumento() != null && !fiador.getDocumento().isEmpty()) {
                String cpfLimpo = CpfValidator.unformat(fiador.getDocumento());
                if (!CpfValidator.isValid(cpfLimpo)) {
                    FacesUtil.addErrorMessage("CPF inválido!");
                    return;
                }
                fiador.setDocumento(cpfLimpo);
            }
            
            fiadorService.salvar(fiador);
            FacesUtil.addInfoMessage("Fiador salvo com sucesso!");
            limparFormulario();
            
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao salvar fiador: " + e.getMessage());
        }
    }

    public void limparFormulario() {
        this.fiador = new Fiador();
        System.out.println("Formulário limpo");
    }

    public String voltar() {
        return "/cadastro/CadastroContrato.xhtml?faces-redirect=true";
    }
}