package org.ethicalmachines.zeus.examples.selfdriving;

import clojure.lang.Var;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.naveensundarg.shadow.prover.core.proof.Unifier;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.representations.formula.Predicate;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.representations.value.Variable;
import com.naveensundarg.shadow.prover.utils.Reader;
import org.apache.commons.lang3.StringUtils;
import org.ethicalmachines.zeus.AbstractZeusActor;

import java.util.Map;
import java.util.Optional;

/**
 * Created by naveensundarg on 4/20/17.
 */
public class DrivingActuator extends AbstractZeusActor{

    static Formula brake = null;
    static Formula drive = null;
    static Formula aim = null;

    static Formula steer = null;
    static Variable direction = null;
    static Variable car = null;
    static Variable time = null;

    String timeString;
    static{

        try {
            brake = Reader.readFormulaFromString("(happens brake ?time)");
            drive = Reader.readFormulaFromString("(happens (steer ?direction) ?time)");
            aim   = Reader.readFormulaFromString("(happens (aim-at ?car) ?time)");
            steer   = Reader.readFormulaFromString("(happens (accelerate-towards ?car) ?time)");
            direction = (Variable) Reader.readLogicValueFromString("?direction");
            car = (Variable) Reader.readLogicValueFromString("?car");
            time = (Variable) Reader.readLogicValueFromString("?time");


        } catch (Reader.ParsingException e) {
            e.printStackTrace();
        }
    }

    static ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    @Override
    public boolean isActuator() {
        return true;
    }

    @Override
    public void process(Formula f) {

           // BEGIN ZEUS PROCESSING

        String action = "NOTHING";

        Optional<Map<Variable, Value>> ans = Unifier.unifyFormula( brake,  f);

        if(ans.isPresent()){
            action = "BRAKE" + " ";
            timeString = "" + ans.get().get(time);

        }


        ans = Unifier.unifyFormula( drive,  f);

        if(ans.isPresent()){
            action = "STEER IN DIRECTION: " + ans.get().get(direction)  + " ";
            timeString = "" + ans.get().get(time);

        }

        ans = Unifier.unifyFormula( aim,  f);
        if(ans.isPresent()){
            action = "AIM AT: " + ans.get().get(car) + " ";
            timeString = "" + ans.get().get(time);

        }

        ans = Unifier.unifyFormula( steer,  f);
        if(ans.isPresent()){
            action = "ACCELERATE TOWARDS: " + ans.get().get(car) + " ";
            timeString = "" + ans.get().get(time);

        }



        cp.clear();
        cp.print("[" + cp.getDateFormatted()+ " driving actuator" +"] "  , Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.BLUE);
        cp.clear();

        cp.print(" " + timeString + " " , Ansi.Attribute.BOLD, Ansi.FColor.BLACK, Ansi.BColor.CYAN);

        cp.print(action , Ansi.Attribute.BOLD, Ansi.FColor.BLACK, Ansi.BColor.NONE);
        cp.println("");

        cp.clear();

    }
}
