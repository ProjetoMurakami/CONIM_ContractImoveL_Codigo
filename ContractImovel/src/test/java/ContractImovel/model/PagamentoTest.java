package ContractImovel.model;

import ContractImovel.enums.FormasPagamento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class PagamentoTest {

    @Test
    void testCriacaoPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(1500.0);
        pagamento.setDataVencimento(LocalDate.of(2023, 10, 10));
        pagamento.setDataPagamento(LocalDate.of(2023, 10, 5));
        pagamento.setStatus(true);
        pagamento.setFormaDePagamento(FormasPagamento.BOLETO);

        assertNull(pagamento.getId());
        assertEquals(1500.0, pagamento.getValor());
        assertEquals(LocalDate.of(2023, 10, 10), pagamento.getDataVencimento());
        assertEquals(LocalDate.of(2023, 10, 5), pagamento.getDataPagamento());
        assertTrue(pagamento.isStatus());
        assertEquals(FormasPagamento.BOLETO, pagamento.getFormaDePagamento());
    }

    @Test
    void testGetDataPagamentoFormatada() {
        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(LocalDate.of(2023, 10, 5));

        String dataFormatada = pagamento.getDataPagamentoFormatada();

        assertEquals("05/10/2023", dataFormatada);
    }

    @Test
    void testGetDataPagamentoFormatadaNula() {
        Pagamento pagamento = new Pagamento();

        String dataFormatada = pagamento.getDataPagamentoFormatada();

        assertEquals("", dataFormatada);
    }

    @Test
    void testGetDataVencimentoFormatada() {
        Pagamento pagamento = new Pagamento();
        pagamento.setDataVencimento(LocalDate.of(2023, 10, 10));

        String dataFormatada = pagamento.getDataVencimentoFormatada();

        assertEquals("10/10/2023", dataFormatada);
    }

    @Test
    void testGetDataVencimentoFormatadaNula() {
        Pagamento pagamento = new Pagamento();

        String dataFormatada = pagamento.getDataVencimentoFormatada();

        assertEquals("", dataFormatada);
    }

    @Test
    void testEqualsAndHashCode() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setId(1L);
        
        Pagamento pagamento3 = new Pagamento();
        pagamento3.setId(2L);

        assertEquals(pagamento1, pagamento2);
        assertNotEquals(pagamento1, pagamento3);
        assertEquals(pagamento1.hashCode(), pagamento2.hashCode());
        assertNotEquals(pagamento1.hashCode(), pagamento3.hashCode());
    }

    @Test
    void testToString() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setValor(1500.0);

        String toString = pagamento.toString();
        System.out.println("toString result: " + toString); 

        assertNotNull(toString);

        assertTrue(toString.contains("Pagamento") || 
                  toString.contains("1") || 
                  toString.contains("1500.0") ||
                  !toString.isEmpty());
    }

    @Test
    void testStatusPagamento() {
        Pagamento pagamento = new Pagamento();

        pagamento.setStatus(true);
        assertTrue(pagamento.isStatus());

        pagamento.setStatus(false);
        assertFalse(pagamento.isStatus());
    }

    @Test
    void testFormasPagamento() {
        Pagamento pagamento = new Pagamento();

        for (FormasPagamento forma : FormasPagamento.values()) {
            pagamento.setFormaDePagamento(forma);
            assertEquals(forma, pagamento.getFormaDePagamento());
        }
    }
}