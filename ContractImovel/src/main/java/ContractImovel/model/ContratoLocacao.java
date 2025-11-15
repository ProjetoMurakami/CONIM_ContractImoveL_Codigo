package ContractImovel.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded= true)
@EqualsAndHashCode(callSuper= false, onlyExplicitlyIncluded= true)
@Entity
public class ContratoLocacao implements Serializable{
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private Double valorAluguel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fiador_id")
    private Fiador fiador;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Inquilino inquilino;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "corretor_id")
    private Usuario usuario;
    
}
