package ContractImovel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ContractImovel.enums.FormasPagamento;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Pagamento implements Serializable{
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    
    private Double valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private boolean status;
    
    @Enumerated(EnumType.STRING) 
    private FormasPagamento formaDePagamento;

    @ManyToOne
    @JoinColumn(name = "contrato_locacao_id")
    private ContratoLocacao contratoLocacao;

    public String getDataPagamentoFormatada() {
        return dataPagamento != null ? dataPagamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    public String getDataVencimentoFormatada() {
        return dataVencimento != null ? dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
   
}

