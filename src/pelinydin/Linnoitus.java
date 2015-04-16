

package pelinydin;

/**
 * Erilaisia linnoitustapoja vastaavat vakioarvot.
 * Tätä käytetään tietyssä pelitilanteessa jäljellä olevien linnoitusmahdollisuuksien tallentamiseen.
 * Kaikkien neljän linnoitusmahdollisuuden tilan voi esittää yhtenä kokonaislukuna,
 * josta yksittäisen linnoitustavan voi selvittää bittitason operaatioilla.
 * Esim. jos halutaan selvittää onkon valkean kuningatarpuolen linnoitus mahdollista, 
 * käytä (luku & Linnoitus.valkeaKuningatar) != 0.
 * Jos haluat poistaa valkean kuningatarpuolen linnoitusmahdollisuuden,
 * käytä luku & (!Linnoitus.valkeaKuningatar)
 * @author markumus
 */

public class Linnoitus {
    public static final int valkeaKuningas   = 1;
    public static final int valkeaKuningatar = 2;
    public static final int mustaKuningas    = 4;
    public static final int mustaKuningatar  = 8;
    public static final int kaikki = 0xF;
}
