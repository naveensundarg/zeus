package org.ethicalmachines.zeus.examples.selfdriving;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import clojure.lang.Var;
import com.naveensundarg.shadow.prover.core.proof.Justification;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.representations.formula.Predicate;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.representations.value.Variable;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;
import com.naveensundarg.shadow.prover.utils.Reader;
import com.naveensundarg.shadow.prover.utils.Sets;
import org.ethicalmachines.zeus.AbstractZeusActor;
import org.ethicalmachines.zeus.actorschemas.TheoremProverActor;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by naveensundarg on 4/20/17.
 */
public class DrivingAgent extends TheoremProverActor {

    private int time;

    static Variable direction = null;
    static Variable car = null;

    static{

        try {
            direction = (Variable) Reader.readLogicValueFromString("?d");
            car = (Variable) Reader.readLogicValueFromString("?car");


        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }
    }

    private ActorRef drivingActuator;
    public DrivingAgent(InputStream knowledgeBaseFile, ActorRef drivingActuator) {
        super(knowledgeBaseFile);
        this.time = 0;
        this.drivingActuator = drivingActuator;
    }

    @Override
    public boolean isActuator() {
        return false;
    }

    @Override
    public void process(Formula f) {

        if (f instanceof Predicate ){
            if(((Predicate) f).getName().equals("=")){
                knowledgeBase.add(f);
                return;
            }
        }

        String timeConstant = "t"+ time;

        Formula happens = null;

        try {
             happens = Reader.readFormulaFromString("(happens brake " + timeConstant + ")");
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }

        Optional<Justification> justOpt = null;

        justOpt = this.prover.prove(Sets.add(this.knowledgeBase,f), happens);

        if(justOpt.isPresent()){

            drivingActuator.tell(happens, getSelf());
        }

        try {
             happens = Reader.readFormulaFromString("(happens (steer ?d) " + timeConstant + ")");
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


        Optional<Value>  bindingsOpt = Optional.empty();
        try {
            bindingsOpt = this.prover.proveAndGetBinding(Sets.add(this.knowledgeBase,f), happens, (Variable) Reader.readLogicValueFromString("?d"));
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


        if(bindingsOpt.isPresent()){

            Map<Variable, Value> map = CollectionUtils.newMap();
            map.put(direction, bindingsOpt.get());
            drivingActuator.tell(happens.apply(map), getSelf());

        }
        try {
             happens = Reader.readFormulaFromString("(happens (aim-at ?car) " + timeConstant + ")");
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


        try {
            bindingsOpt = this.prover.proveAndGetBinding(Sets.add(this.knowledgeBase,f), happens, (Variable) Reader.readLogicValueFromString("?car"));
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


         if(bindingsOpt.isPresent()){

            Map<Variable, Value> map = CollectionUtils.newMap();
            map.put(car, bindingsOpt.get());
            drivingActuator.tell(happens.apply(map), getSelf());

        }

        try {
             happens = Reader.readFormulaFromString("(happens (accelerate-towards ?car) " + timeConstant + ")");
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


        try {
            bindingsOpt = this.prover.proveAndGetBinding(Sets.add(this.knowledgeBase,f), happens, (Variable) Reader.readLogicValueFromString("?car"));
        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }


         if(bindingsOpt.isPresent()){

            Map<Variable, Value> map = CollectionUtils.newMap();
            map.put(car, bindingsOpt.get());
            drivingActuator.tell(happens.apply(map), getSelf());

        }

        time = time + 1;
    }
}
