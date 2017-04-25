package org.ethicalmachines.zeus.examples.selfdriving;

import akka.actor.ActorRef;
import akka.routing.Broadcast;
import com.naveensundarg.shadow.prover.core.SnarkWrapper;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.utils.Reader;
import org.ethicalmachines.zeus.AbstractZeusActor;

/**
 * Created by naveensundarg on 4/24/17.
 */
public class CarHistoryActor extends AbstractZeusActor {

    private static  Formula KB_1 ;

    static {

        try {

             KB_1 = Reader.readFormulaFromString(
                    "(= 1 (ec car_17))");

        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }
    }

    CarHistoryActor(ActorRef drivingAgent){

        drivingAgent.tell(KB_1, getSelf());


    }
    @Override
    public boolean isActuator() {
        return false;
    }

    @Override
    public void process(Formula f) {

    }
}
