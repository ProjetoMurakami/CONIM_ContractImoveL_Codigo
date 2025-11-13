package ContractImovel.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    @Test
    void testCpfValido() {
        assertTrue(CpfValidator.isValid("52998224725"));
        assertTrue(CpfValidator.isValid("11144477735")); 
    }

    @Test
    void testCpfInvalido() {
        assertFalse(CpfValidator.isValid("12345678901")); 
        assertFalse(CpfValidator.isValid("00000000000")); 
        assertFalse(CpfValidator.isValid("11111111111")); 
        assertFalse(CpfValidator.isValid("123")); 
        assertFalse(CpfValidator.isValid("123456789012"));
        assertFalse(CpfValidator.isValid(null));
        assertFalse(CpfValidator.isValid("")); 
        assertFalse(CpfValidator.isValid("ABC45678901")); 
    }

    @Test
    void testFormat() {
        assertEquals("529.982.247-25", CpfValidator.format("52998224725"));
        assertEquals("111.444.777-35", CpfValidator.format("11144477735"));
        
        assertNull(CpfValidator.format(null));
        assertEquals("123", CpfValidator.format("123")); 
        assertEquals("1234567890", CpfValidator.format("1234567890")); 
    }

    @Test
    void testUnformat() {
        assertEquals("52998224725", CpfValidator.unformat("529.982.247-25"));
        assertEquals("11144477735", CpfValidator.unformat("111.444.777-35"));
        assertEquals("12345678901", CpfValidator.unformat("123.456.789-01"));
        assertEquals("12345678901", CpfValidator.unformat("12345678901")); 

        assertNull(CpfValidator.unformat(null));
        assertEquals("123", CpfValidator.unformat("123")); 
        assertEquals("123", CpfValidator.unformat("1.2.3"));
    }

    @Test
    void testCpfComFormatacao() {
        assertFalse(CpfValidator.isValid("529.982.247-25")); 
        assertFalse(CpfValidator.isValid("111.444.777-35")); 
    }
}