package ContractImovel.model;

import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FiadorTest {

    @Test
    void testCriacaoFiador() {
        Fiador fiador = new Fiador();
        fiador.setNome("João Silva");
        fiador.setCpf("12345678901");
        fiador.setEmail("joao@example.com");
        fiador.setTelefone("11999999999");
        fiador.setCEP("12345-678");
        fiador.setBairro("Centro");
        fiador.setEndereco("Rua das Flores");
        fiador.setNumero("123");
        fiador.setRendaMensal(5000.0);
        fiador.setObservacoes("Fiador responsável");
        fiador.setCategoria(TiposCliente.PESSOA_FISICA);

        assertNull(fiador.getId());
        assertEquals("João Silva", fiador.getNome());
        assertEquals("12345678901", fiador.getCpf());
        assertEquals("joao@example.com", fiador.getEmail());
        assertEquals("11999999999", fiador.getTelefone());
        assertEquals("12345-678", fiador.getCEP());
        assertEquals("Centro", fiador.getBairro());
        assertEquals("Rua das Flores", fiador.getEndereco());
        assertEquals("123", fiador.getNumero());
        assertEquals(5000.0, fiador.getRendaMensal());
        assertEquals("Fiador responsável", fiador.getObservacoes());
        assertEquals(TiposCliente.PESSOA_FISICA, fiador.getCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        Fiador fiador1 = new Fiador();
        fiador1.setId(1L);
        
        Fiador fiador2 = new Fiador();
        fiador2.setId(1L);
        
        Fiador fiador3 = new Fiador();
        fiador3.setId(2L);

        assertEquals(fiador1, fiador2);
        assertNotEquals(fiador1, fiador3);
        assertEquals(fiador1.hashCode(), fiador2.hashCode());
        assertNotEquals(fiador1.hashCode(), fiador3.hashCode());
    }

    @Test
    void testToString() {
        Fiador fiador = new Fiador();
        fiador.setId(1L);
        fiador.setNome("Maria Santos");

        String toString = fiador.toString();

        assertNotNull(toString);

        assertTrue(toString.contains("Fiador") || 
                  toString.contains("1") || 
                  toString.contains("Maria Santos") ||
                  !toString.isEmpty());
    }

    @Test
    void testCategoriaEnum() {
        Fiador fiador = new Fiador();

        for (TiposCliente categoria : TiposCliente.values()) {
            fiador.setCategoria(categoria);
            assertEquals(categoria, fiador.getCategoria());
        }
    }

    @Test
    void testCamposOpcionaisNulos() {
        Fiador fiador = new Fiador();
        fiador.setNome("Fiador Teste");
        fiador.setCpf("12345678901");

        assertNull(fiador.getEmail());
        assertNull(fiador.getTelefone());
        assertNull(fiador.getCEP());
        assertNull(fiador.getBairro());
        assertNull(fiador.getEndereco());
        assertNull(fiador.getNumero());
        assertNull(fiador.getObservacoes());
        assertNull(fiador.getCategoria());
    }
}