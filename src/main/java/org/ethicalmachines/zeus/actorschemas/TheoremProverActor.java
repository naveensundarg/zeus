package org.ethicalmachines.zeus.actorschemas;

import akka.actor.AbstractActor;
import com.naveensundarg.shadow.prover.core.Prover;
import com.naveensundarg.shadow.prover.core.SnarkWrapper;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.utils.Reader;
import org.ethicalmachines.zeus.AbstractZeusActor;
import us.bpsm.edn.Keyword;
import us.bpsm.edn.parser.Parseable;
import us.bpsm.edn.parser.Parser;
import us.bpsm.edn.parser.Parsers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by naveensundarg on 4/20/17.
 */
public abstract class TheoremProverActor extends AbstractZeusActor {

    protected final Set<Formula> knowledgeBase;
    protected final Prover prover;

    public  TheoremProverActor(InputStream knowledgeBaseFile){

        Parseable parseable = Parsers.newParseable(new InputStreamReader(knowledgeBaseFile));
        Parser parser = Parsers.newParser(Parsers.defaultConfiguration());
        Map<?, ?> map  = (Map<?, ?>) parser.nextValue(parseable);

        Map<?, ?> kb  = (Map<?, ?>) map.get(Keyword.newKeyword("knowledge-base"));
        this.knowledgeBase = kb.values().stream().map(x -> {
            try {
                return Reader.readFormula(x);
            } catch (Reader.ParsingException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());

        this.prover = new SnarkWrapper();
    }


}
