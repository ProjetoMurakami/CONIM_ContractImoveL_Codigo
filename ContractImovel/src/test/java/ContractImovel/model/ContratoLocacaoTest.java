package ContractImovel.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class ContratoLocacaoTest {

    @Test
    void testCriacaoContratoLocacao() {
        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setDataInicio(LocalDate.of(2023, 1, 1));
        contrato.setDataFinal(LocalDate.of(2024, 1, 1));
        contrato.setValorAluguel(1500.0);

        Imovel imovel = new Imovel();
        imovel.setId(1L);
        contrato.setImovel(imovel);

        Inquilino inquilino = new Inquilino();
        inquilino.setId(1L);
        contrato.setInquilino(inquilino);

        Fiador fiador = new Fiador();
        fiador.setId(1L);
        contrato.setFiador(fiador);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        contrato.setUsuario(usuario);

        List<Pagamento> pagamentos = new ArrayList<>();
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamentos.add(pagamento);
        contrato.setPagamentos(pagamentos);

        assertNull(contrato.getId());
        assertEquals(LocalDate.of(2023, 1, 1), contrato.getDataInicio());
        assertEquals(LocalDate.of(2024, 1, 1), contrato.getDataFinal());
        assertEquals(1500.0, contrato.getValorAluguel());
        assertEquals(imovel, contrato.getImovel());
        assertEquals(inquilino, contrato.getInquilino());
        assertEquals(fiador, contrato.getFiador());
        assertEquals(usuario, contrato.getUsuario());
        assertEquals(1, contrato.getPagamentos().size());
    }

    @Test
    void testEqualsAndHashCode() {
        ContratoLocacao contrato1 = new ContratoLocacao();
        contrato1.setId(1L);
        
        ContratoLocacao contrato2 = new ContratoLocacao();
        contrato2.setId(1L);
        
        ContratoLocacao contrato3 = new ContratoLocacao();
        contrato3.setId(2L);

        assertEquals(contrato1, contrato2);
        assertNotEquals(contrato1, contrato3);
        assertEquals(contrato1.hashCode(), contrato2.hashCode());
        assertNotEquals(contrato1.hashCode(), contrato3.hashCode());
    }

    @Test
    void testToString() {
        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setId(1L);
        contrato.setValorAluguel(1500.0);

        String toString = contrato.toString();

        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("1500.0"));
    }

    @Test
    void testContratoSemFiador() {
        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setFiador(null);

        assertNull(contrato.getFiador());
    }

    @Test
    void testContratoSemUsuario() {
        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setUsuario(null);

        assertNull(contrato.getUsuario());
    }

    @Test
    void testContratoSemPagamentos() {
        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setPagamentos(new ArrayList<>());

        assertNotNull(contrato.getPagamentos());
        assertTrue(contrato.getPagamentos().isEmpty());
    }
}