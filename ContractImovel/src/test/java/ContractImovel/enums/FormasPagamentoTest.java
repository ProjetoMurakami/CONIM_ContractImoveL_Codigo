package ContractImovel.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormasPagamentoTest {

    @Test
    void testValoresEnum() {
        assertEquals(7, FormasPagamento.values().length);
        assertNotNull(FormasPagamento.valueOf("PIX"));
        assertNotNull(FormasPagamento.valueOf("TED"));
        assertNotNull(FormasPagamento.valueOf("DOC"));
        assertNotNull(FormasPagamento.valueOf("BOLETO"));
        assertNotNull(FormasPagamento.valueOf("CHEQUE"));
        assertNotNull(FormasPagamento.valueOf("CARTÃO_DE_CRÉDITO"));
        assertNotNull(FormasPagamento.valueOf("CARTÃO_DE_DÉBITO"));
    }

    @Test
    void testOrdemValores() {
        assertEquals("PIX", FormasPagamento.PIX.name());
        assertEquals("TED", FormasPagamento.TED.name());
        assertEquals("DOC", FormasPagamento.DOC.name());
        assertEquals("BOLETO", FormasPagamento.BOLETO.name());
        assertEquals("CHEQUE", FormasPagamento.CHEQUE.name());
        assertEquals("CARTÃO_DE_CRÉDITO", FormasPagamento.CARTÃO_DE_CRÉDITO.name());
        assertEquals("CARTÃO_DE_DÉBITO", FormasPagamento.CARTÃO_DE_DÉBITO.name());
    }

    @Test
    void testToString() {
        assertEquals("PIX", FormasPagamento.PIX.toString());
        assertEquals("BOLETO", FormasPagamento.BOLETO.toString());
    }
}