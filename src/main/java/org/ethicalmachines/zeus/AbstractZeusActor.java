package org.ethicalmachines.zeus;

import akka.actor.AbstractActor;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by naveensundarg on 4/20/17.
 */
public abstract class AbstractZeusActor extends AbstractActor {


    static ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    public static EthicalTheory getEthicalTheory() {
        return ETHICAL_THEORY;
    }

    public static void setEthicalTheory(EthicalTheory ethicalTheory) {
        ETHICAL_THEORY = ethicalTheory;
    }

    private static  EthicalTheory ETHICAL_THEORY;


    private static AtomicBoolean ENABLE_ETHICAL_LAYER  = new AtomicBoolean(true);
    @Override
    public final Receive createReceive() {
        return receiveBuilder()
                .match(Formula.class, this::_process_)
                .build();
    }


    private final void _process_(Formula f){

        // BEGIN ZEUS PROCESSING
        cp.clear();
        cp.print("[" + cp.getDateFormatted()+ " zeus" +"] ", Ansi.Attribute.BOLD, Ansi.FColor.CYAN, Ansi.BColor.BLACK);
        cp.print(" received Message by " + StringUtils.rightPad(getSelf().path().name(), 15) + " \t",  Ansi.Attribute.BOLD, Ansi.FColor.BLACK, Ansi.BColor.YELLOW);
        cp.print(" " +  f + " ", Ansi.Attribute.LIGHT, Ansi.FColor.BLACK, Ansi.BColor.GREEN);

        cp.clear();

        System.out.println("");
        // END ZEUS PROCESSING

        if(this.isActuator()){

            process(ETHICAL_THEORY.transformFormula(f));

        } else {

           process(f);
        }


    }

    public abstract  boolean isActuator();

    public abstract void process(Formula f);


}
