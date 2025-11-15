package ContractImovel.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ContractImovel.enums.TiposCliente;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Fiador implements Serializable{
    @EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    private String nome;
    private String documento;
    private String email;
    private String telefone;
    private String CEP;
    private String bairro;
    private String endereco;
    private String numero;
    private double rendaMensal;
    private String observacoes;
    
    @Enumerated(EnumType.STRING) 
    private TiposCliente categoria;
}
