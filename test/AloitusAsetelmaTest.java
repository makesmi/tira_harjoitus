/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import static pelinydin.NappulaTyyppi.*;
import static pelinydin.Väri.*;
import org.junit.BeforeClass;
import org.junit.Test;
import pelinydin.AloitusAsetelma;
import pelinydin.Linnoitus;
import pelinydin.NappulaTyyppi;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.Väri;

/**
 *
 * @author risto_000
 */
public class AloitusAsetelmaTest {
    
    public AloitusAsetelmaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    @Test
    public void lauta(){
        ShakkiLauta lauta = AloitusAsetelma.haeLauta();
        
        NappulaTyyppi tyypit[][] = {{TORNI, RATSU, LÄHETTI, KUNINGATAR, KUNINGAS, LÄHETTI, RATSU, TORNI}, 
            {SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS, SOTILAS},
            {TORNI, RATSU, LÄHETTI, KUNINGATAR, KUNINGAS, LÄHETTI, RATSU, TORNI} 
        };
        
        boolean värit[][] = {
            {VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN},
            {VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN, VALKOINEN},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false, false},
            {MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA},
            {MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA, MUSTA}};
        
        for(int y = 0; y < 8; y++)
            for(int x = 0; x < 0; x++){
                if(y >= 2 && y <= 5){
                    Assert.assertNull(lauta.haeRuutu(x, y));
                }else{
                    assert(lauta.haeRuutu(x, y).tyyppi == tyypit[y][x]);
                    assert(lauta.haeRuutu(x, y).väri == värit[y][x]);
                }
            }
    }
    
    @Test
    public void tila(){
        PeliTila tila = AloitusAsetelma.haeTila();
        assert(tila.siirto == null);
        assert(tila.syötyNappula == null);
        assert(tila.ohestaLyöntiSarake == -1);
        assert(tila.ohestaLyönti == false);
        assert(tila.korotus == false);
        assert(tila.linnoitusMahdollisuudet == Linnoitus.kaikki);
        assert(tila.siirtoNumero == 0);
        assert(tila.vuoro() == Väri.VALKOINEN);
        
    }
}
