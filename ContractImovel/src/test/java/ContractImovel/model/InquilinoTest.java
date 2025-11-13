package ContractImovel.model;

import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InquilinoTest {

    @Test
    void testCriacaoInquilino() {
        Inquilino inquilino = new Inquilino();
        inquilino.setNome("João Silva");
        inquilino.setEmail("joao@example.com");
        inquilino.setCpf("12345678901");
        inquilino.setTelefone("11999999999");
        inquilino.setRendaMensal(5000.0);
        inquilino.setCategoria(TiposCliente.PESSOA_FISICA);

        assertNull(inquilino.getId());
        assertEquals("João Silva", inquilino.getNome());
        assertEquals("joao@example.com", inquilino.getEmail());
        assertEquals("12345678901", inquilino.getCpf());
        assertEquals("11999999999", inquilino.getTelefone());
        assertEquals(5000.0, inquilino.getRendaMensal());
        assertEquals(TiposCliente.PESSOA_FISICA, inquilino.getCategoria());
    }

    @Test
    void testEqualsAndHashCode() {
        Inquilino inquilino1 = new Inquilino();
        inquilino1.setId(1L);
        
        Inquilino inquilino2 = new Inquilino();
        inquilino2.setId(1L);
        
        Inquilino inquilino3 = new Inquilino();
        inquilino3.setId(2L);

        assertEquals(inquilino1, inquilino2);
        assertNotEquals(inquilino1, inquilino3);
        assertEquals(inquilino1.hashCode(), inquilino2.hashCode());
        assertNotEquals(inquilino1.hashCode(), inquilino3.hashCode());
    }

    @Test
    void testToString() {
        Inquilino inquilino = new Inquilino();
        inquilino.setId(1L);
        inquilino.setNome("Maria Santos");

        String toString = inquilino.toString();

        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Maria Santos"));
    }

    @Test
    void testCategoriaEnum() {
        Inquilino inquilino = new Inquilino();

        for (TiposCliente categoria : TiposCliente.values()) {
            inquilino.setCategoria(categoria);
            assertEquals(categoria, inquilino.getCategoria());
        }
    }
}