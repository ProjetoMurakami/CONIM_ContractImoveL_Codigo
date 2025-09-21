package ContractImovel.view.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroImovel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String endereco;
    private String tipo;
    private String status; // você pode mapear para StatusImovel depois, se preferir
}
