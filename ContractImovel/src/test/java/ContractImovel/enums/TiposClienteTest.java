package ContractImovel.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TiposClienteTest {

    @Test
    void testValoresEnum() {
        int quantidadeValores = TiposCliente.values().length;
        System.out.println("Quantidade de valores em TiposCliente: " + quantidadeValores);
        
        for (TiposCliente tipo : TiposCliente.values()) {
            System.out.println("Tipo: " + tipo.name());
        }

        assertDoesNotThrow(() -> {
            TiposCliente.valueOf("PESSOA_FISICA");
            TiposCliente.valueOf("PESSOA_JURIDICA");
        });
    }

    @Test
    void testOrdemValores() {

        assertTrue(TiposCliente.values().length > 0);

        for (TiposCliente tipo : TiposCliente.values()) {
            assertEquals(tipo, TiposCliente.valueOf(tipo.name()));
            assertEquals(tipo.name(), tipo.toString());
        }
    }
}