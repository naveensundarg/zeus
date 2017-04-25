package org.ethicalmachines.zeus;

import com.naveensundarg.shadow.prover.representations.formula.Formula;

/**
 * Created by naveensundarg on 4/22/17.
 */
public abstract  class EthicalTheory {


     boolean isEnabled = false;

     public abstract Formula transformFormula(Formula f);

     public   boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {

           isEnabled = enabled;
    }

}
