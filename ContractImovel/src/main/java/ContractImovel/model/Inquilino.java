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

import ContractImovel.enums.TiposCliente;
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Inquilino implements Serializable{
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    private String nome;
    private String email;
    private String documento;
    private String telefone;
    private double rendaMensal;
    
    @Enumerated(EnumType.STRING) 
    private TiposCliente categoria;

}
