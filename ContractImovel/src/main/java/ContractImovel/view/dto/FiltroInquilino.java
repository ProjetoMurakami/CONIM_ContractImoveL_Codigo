package ContractImovel.view.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroInquilino implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;
}
