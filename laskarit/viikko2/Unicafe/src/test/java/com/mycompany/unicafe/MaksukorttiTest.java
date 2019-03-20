package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void kortinSaldoAlussaOikein() {
        assertTrue(kortti.saldo() == 10);
    }

    @Test
    public void kortilleVoiLadataRahaa() {
        kortti.lataaRahaa(25);
        assertEquals("saldo: 0.35", kortti.toString());
    }
    
    @Test
    public void negatiivisenSummanLataaminenEiMuutaSaldoa() {
        kortti.lataaRahaa(-10);
        assertEquals("saldo: 0.10", kortti.toString());
    }

    @Test
    public void kortiltaVoiOttaaRahaa() {
        kortti.otaRahaa(5);
        assertEquals("saldo: 0.05", kortti.toString());
    }

    @Test
    public void rahanOttaminenEiVieSaldoaNegatiiviseksi() {
        kortti.otaRahaa(15);
        assertEquals("saldo: 0.10", kortti.toString());
    }

    @Test
    public void rahanOttaminenPalauttaaTrueJosRahatRiittivat() {
        assertTrue(kortti.otaRahaa(5));
    }

    @Test
    public void rahanOttaminenPalauttaaFalseJosRahatEivatRiita() {
        assertTrue(kortti.otaRahaa(15)==false);
    }
}
