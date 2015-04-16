

package kayttoliittyma;

import Tutkimus.MinMaxAlfaBetaTutkimus;
import Tutkimus.MinMaxTutkimus;
import Tutkimus.TutkimusAlgoritmi;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import tekoaly.HakuAlgoritmi;
import tekoaly.MinMax;
import tekoaly.MinMaxAlfaBeta;



public class AlgoritmiValitsin extends JPanel{
    private final HashMap<String, Function<Integer, TutkimusAlgoritmi>> tutkimusAlgoritmit = new HashMap<>();
    private final HashMap<String, Function<Integer, HakuAlgoritmi>> hakuAlgoritmit = new HashMap<>();
    private final JComboBox<String> algoritmiKompo = new JComboBox<>();
    private final JComboBox<Integer> syvyysKompo = new JComboBox<>();
    
    
    private final int pieninSyvyys = 2;
    private final int suurinSyvyys = 6;
    
    public AlgoritmiValitsin(){
        setLayout(new BorderLayout());
        add(algoritmiKompo, BorderLayout.NORTH);
        add(syvyysKompo, BorderLayout.SOUTH);
        
        tutkimusAlgoritmit.put("minmax", syvyys -> new MinMaxTutkimus(syvyys));
        hakuAlgoritmit.put("minmax", syvyys -> new MinMax(syvyys));
        algoritmiKompo.addItem("minmax");
        tutkimusAlgoritmit.put("alfabeta", syvyys -> new MinMaxAlfaBetaTutkimus(syvyys));
        hakuAlgoritmit.put("alfabeta", syvyys -> new MinMaxAlfaBeta(syvyys));
        algoritmiKompo.addItem("alfabeta");
        algoritmiKompo.setSelectedItem("minmax");

        for(int syvyys = pieninSyvyys; syvyys <= suurinSyvyys; syvyys++){
            syvyysKompo.addItem(syvyys);
        }
        syvyysKompo.setSelectedItem(3);
    }
    
    private int syvyys(){
        return (Integer)syvyysKompo.getSelectedItem();
    }
    
    public HakuAlgoritmi haeHakuAlgoritmi(){
        return hakuAlgoritmit.get((String)algoritmiKompo.getSelectedItem()).apply(syvyys());
    }
    
    public TutkimusAlgoritmi haeTutkimusAlgoritmi(){
        return tutkimusAlgoritmit.get((String)algoritmiKompo.getSelectedItem()).apply(syvyys());
    }
}

