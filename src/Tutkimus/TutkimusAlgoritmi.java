/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tutkimus;

import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import tekoaly.ArviointiFunktio;
import tekoaly.HakuAlgoritmi;


public interface TutkimusAlgoritmi extends HakuAlgoritmi{
    public HakuPuu tutki(ShakkiPeli peli, ArviointiFunktio arviointi);

    @Override
    public default ShakkiSiirto haku(ShakkiPeli peli, ArviointiFunktio arviointi) {
        return null;
    }
}
