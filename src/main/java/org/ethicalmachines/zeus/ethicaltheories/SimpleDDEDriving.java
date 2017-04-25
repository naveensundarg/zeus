package org.ethicalmachines.zeus.ethicaltheories;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.naveensundarg.shadow.prover.core.Prover;
import com.naveensundarg.shadow.prover.core.SnarkWrapper;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;
import com.naveensundarg.shadow.prover.utils.Reader;
import com.naveensundarg.shadow.prover.utils.Sets;
import org.ethicalmachines.zeus.EthicalTheory;

import java.util.Set;

/**
 * Created by naveensundarg on 4/22/17.
 */
public class SimpleDDEDriving extends EthicalTheory {

    static ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    private static  Formula PROHIBITED;
    private static  Formula KB_1 ;
    private static  Formula KB_2 ;

    private static Formula HARMLESS_MESSAGE;
    private static Formula FALSE;

    private static Prover prover;

    static {
        prover = new SnarkWrapper();

        try {
            KB_1 = Reader.readFormulaFromString(
                    "(forall (?car ?t) " +
                            "(if (and (happens (aim-at ?car) ?t) (happens (accelerate-towards ?car) ?t)) " +
                                 "(holds (hit ?car) (+ 1 ?t))))");

             KB_2 = Reader.readFormulaFromString(
                    "(= 1 (ec car_17))");


            PROHIBITED = Reader.readFormulaFromString("(not (exists (?t ?car) (and (< (ec ?car) 2) (holds (hit ?car) ?t))))" );

            HARMLESS_MESSAGE = Reader.readFormulaFromString("(happens nothing now)");

            FALSE = Reader.readFormulaFromString("(and Z (not Z))");

        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }
    }
    private final Set<Formula> memory;

    public SimpleDDEDriving(){

        //TODO: Use Google Guava.
        memory = CollectionUtils.newEmptySet();
        memory.add(KB_1);
        memory.add(KB_2);
        memory.add(PROHIBITED);
    }



    @Override
    public Formula transformFormula(Formula f) {
        if(!isEnabled()){
            return f;
        }
        memory.add(f);


        if(prover.prove(Sets.add(memory, f), FALSE).isPresent()){

            cp.print(" INTERCEPTED A HARMFUL COMMAND " , Ansi.Attribute.BOLD, Ansi.FColor.NONE, Ansi.BColor.RED);
            cp.clear();cp.println("");
            return HARMLESS_MESSAGE;

        } else {

            return f;

        }
    }
}
