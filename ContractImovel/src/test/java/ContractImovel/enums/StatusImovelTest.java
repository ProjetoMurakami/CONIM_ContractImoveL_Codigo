package ContractImovel.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusImovelTest {

    @Test
    void testValoresEnum() {
        int quantidadeValores = StatusImovel.values().length;
        System.out.println("Quantidade de valores no enum: " + quantidadeValores);

        for (StatusImovel status : StatusImovel.values()) {
            System.out.println("Valor: " + status.name());
        }
        
        assertNotNull(StatusImovel.valueOf("ALUGADO"));
        assertNotNull(StatusImovel.valueOf("COMPRADO"));
        assertNotNull(StatusImovel.valueOf("INTERDITADO"));
        assertNotNull(StatusImovel.valueOf("REFORMA"));
        assertNotNull(StatusImovel.valueOf("DISPONIVEL"));
        assertNotNull(StatusImovel.valueOf("VENDIDO"));

        assertEquals(StatusImovel.DISPONIVEL, StatusImovel.valueOf("DISPONIVEL"));
        assertEquals("DISPONIVEL", StatusImovel.DISPONIVEL.name());
    }

    @Test
    void testOrdemValores() {
        assertEquals("DISPONIVEL", StatusImovel.DISPONIVEL.name());
        assertEquals("ALUGADO", StatusImovel.ALUGADO.name());
        assertEquals("COMPRADO", StatusImovel.COMPRADO.name());
        assertEquals("INTERDITADO", StatusImovel.INTERDITADO.name());
        assertEquals("DISPONIVEL", StatusImovel.DISPONIVEL.name());
        assertEquals("VENDIDO", StatusImovel.VENDIDO.name());
    }
    
    @Test
    void testTodosValoresExistem() {
        assertTrue(StatusImovel.values().length > 0);

        for (StatusImovel status : StatusImovel.values()) {
            assertNotNull(status);
            assertNotNull(status.name());
        }
    }
}