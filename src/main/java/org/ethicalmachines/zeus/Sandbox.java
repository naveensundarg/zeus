package org.ethicalmachines.zeus;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.naveensundarg.shadow.prover.utils.Reader;
import org.ethicalmachines.zeus.ethicaltheories.SimpleDDEDriving;
import org.ethicalmachines.zeus.examples.selfdriving.CarHistoryActor;
import org.ethicalmachines.zeus.examples.selfdriving.DrivingActuator;
import org.ethicalmachines.zeus.examples.selfdriving.DrivingAgent;

/**
 * Created by naveensundarg on 4/12/17.
 */
public class Sandbox {

    public static void main(String[] args) throws Exception {



        System.out.println();

        final ActorSystem system = ActorSystem.create();

        EthicalTheory ethicalTheory = new SimpleDDEDriving();

        ethicalTheory.setEnabled(tr);

        AbstractZeusActor.setEthicalTheory(new SimpleDDEDriving());


        final ActorRef akkaBot = system.actorOf(Props.create(DrivingActuator.class),
      "drivingActuator");


        final ActorRef drivingAgent = system.actorOf(Props.create(DrivingAgent.class,
                Sandbox.class.getResourceAsStream("./examples/selfdriving/drivingagent.clj"), akkaBot),
      "drivingAgent");


        final ActorRef carhistoryActor = system.actorOf(Props.create(CarHistoryActor.class, drivingAgent),
      "carhistoryActor");

        String input_t0 = "(and (holds (humans 0 1) t0) (holds (humans 1 2) t0) )";
        String input_t1 = "(and (holds (humans 0 1) t1) (holds (humans 1 0) t1) )";
        String input_t2 = "" +
                "(and  (holds (humans 0 1) t2) (holds (humans 1 0) t2)(holds (in car_17 0) t2) (= 1 (ec car_17)))";
        drivingAgent.tell(Reader.readFormulaFromString(input_t0),
                ActorRef.noSender());

        drivingAgent.tell(Reader.readFormulaFromString(input_t1),
                ActorRef.noSender());


          drivingAgent.tell(Reader.readFormulaFromString(input_t2),
                ActorRef.noSender());
    }

}
