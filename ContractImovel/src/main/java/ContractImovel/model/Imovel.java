package ContractImovel.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ContractImovel.enums.StatusImovel;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Imovel implements Serializable{
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    
    private String CEP;
    private String cidade;
    private String bairro;
    private String endereco;
    private String numero;    
    private Double valorAluguel;
    private Integer quartos;
    private Integer banheiros;
    private Double area;
    private String observacoes;

    @Enumerated(EnumType.STRING) 
    private StatusImovel statusImovel;
    
}