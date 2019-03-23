package com.mycompany.unicafe;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class KassapaateTest {

    Kassapaate kassapaate;
    Maksukortti maksukortti;

    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        maksukortti = new Maksukortti(800);
    }

    @Test
    public void luotuKassapaateOlemassa() {
        assertTrue(kassapaate != null);
    }

    @Test
    public void kassapaatteenSaldoAlussaOikein() {
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }

    @Test
    public void alussaEdullisiaLounaitaMyytyNolla() {
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
    }

    @Test
    public void alussaMaukkaitaLounaitaMyytyNolla() {
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
    }

    @Test
    public void edullisenLounaanOstoKateisellaKasvattaaKassanRahamaaraa() {
        kassapaate.syoEdullisesti(240);
        assertTrue(kassapaate.kassassaRahaa() == 100240);
    }

    @Test
    public void maukkaanLounaanOstoKateisellaKasvattaaKassanRahamaaraa() {
        kassapaate.syoMaukkaasti(400);
        assertTrue(kassapaate.kassassaRahaa() == 100400);
    }

    @Test
    public void edullisenLounaanOstoKasvattaaMyytyjenLounaidenMaaraa() {
        kassapaate.syoEdullisesti(480);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);
    }

    @Test
    public void maukkaanLounaanOstoKasvattaaMyytyjenLounaidenMaaraa() {
        kassapaate.syoMaukkaasti(800);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }

    @Test
    public void edullisenLounaanOstoEiOnnistuLiianPienellaSummalla() {
        kassapaate.syoEdullisesti(239);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }

    @Test
    public void maukkaanLounaanOstoEiOnnistuLiianPienellaSummalla() {
        kassapaate.syoMaukkaasti(399);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }

    @Test
    public void edullisenLounaanOstonEpaonnistuessaPalautuuOikeaRahamaara() {
        int palautus = kassapaate.syoEdullisesti(100);
        assertTrue(palautus == 100);
    }

    @Test
    public void maukkaanLounaanOstonEpaonnistuessaPalautuuOikeaRahamaara() {
        int palautus = kassapaate.syoMaukkaasti(-350);
        assertTrue(palautus == -350);
    }

    @Test
    public void edullisenLounaanOstonEpaonnistuessaLounaidenMaaraEiMuutu() {
        kassapaate.syoEdullisesti(239);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
    }

    @Test
    public void maukkaanLounaanOstonEpaonnistuessaLounaidenMaaraEiMuutu() {
        kassapaate.syoMaukkaasti(10);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
    }

    @Test
    public void edullisenLounaanOstoKortillaEiVaikutaKassapaatteenRahamaaraan() {
        kassapaate.syoEdullisesti(maksukortti);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }

    @Test
    public void maukkaanLounaanOstoKortillaEiVaikutaKassapaatteenRahamaaraan() {
        kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }

    @Test
    public void edullisenLounaanKorttiostonOnnistuessaPalautuuTrue() {
        boolean tulos = kassapaate.syoEdullisesti(maksukortti);
        assertTrue(tulos == true);
    }

    @Test
    public void maukkaanLounaanKorttiostonOnnistuessaPalautuuTrue() {
        boolean tulos = kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(tulos == true);
    }

    @Test
    public void edullisenLounaanOstoVahentaaKortiltaOikeanSumman() {
        kassapaate.syoEdullisesti(maksukortti);
        assertEquals("saldo: 5.60", maksukortti.toString());
    }

    @Test
    public void maukkaanLounaanOstoVahentaaKortiltaOikeanSumman() {
        kassapaate.syoMaukkaasti(maksukortti);
        assertEquals("saldo: 4.00", maksukortti.toString());
    }

    @Test
    public void edullisenLounaanOstoKortillaKasvattaaMyytyjenLounaidenMaaraa() {
        kassapaate.syoEdullisesti(maksukortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);
    }

    @Test
    public void maukkaanLounaanOstoKortillaKasvattaaMyytyjenLounaidenMaaraa() {
        kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }

    @Test
    public void edullisenLounaanKorttiostonEpaonnistuessaPalautuuFalse() {
        maksukortti.otaRahaa(800);
        boolean tulos = kassapaate.syoEdullisesti(maksukortti);
        assertTrue(tulos == false);
    }

    @Test
    public void maukkaanLounaanKorttiostonEpaonnistuessaPalautuuFalse() {
        maksukortti.otaRahaa(500);
        boolean tulos = kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(tulos == false);
    }

    @Test
    public void edullisenLounaanOstonEpaonnistuessaKortinRahamaaraEiMuutu() {
        maksukortti.otaRahaa(600);
        kassapaate.syoEdullisesti(maksukortti);
        assertEquals("saldo: 2.00", maksukortti.toString());
    }

    @Test
    public void maukkaanLounaanOstonEpaonnistuessaKortinRahamaaraEiMuutu() {
        maksukortti.otaRahaa(600);
        kassapaate.syoMaukkaasti(maksukortti);
        assertEquals("saldo: 2.00", maksukortti.toString());
    }

    @Test
    public void edullisenLounaanKorttiostonEpaonnistuessaMyytyjenLounaidenMaaraEiMuutu() {
        maksukortti.otaRahaa(600);
        kassapaate.syoEdullisesti(maksukortti);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
    }

    @Test
    public void maukkaanLounaanKorttiostonEpaonnistuessaMyytyjenLounaidenMaaraEiMuutu() {
        maksukortti.otaRahaa(600);
        kassapaate.syoMaukkaasti(maksukortti);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kortilleLatausKasvattaaKortinRahamaaraa() {
        kassapaate.lataaRahaaKortille(maksukortti, 99);
        assertEquals("saldo: 8.99", maksukortti.toString());
    }
    
    @Test
    public void kortilleLatausKasvattaaKassanRahamaaraa() {
        kassapaate.lataaRahaaKortille(maksukortti, 99);
        assertTrue(kassapaate.kassassaRahaa()==100099);
    }
    
    @Test
    public void negatiivisenSummanLatausEiVaikutaKassanRahamaaraan() {
        kassapaate.lataaRahaaKortille(maksukortti, -10);
        assertTrue(kassapaate.kassassaRahaa() == 100000); 
    }

}
