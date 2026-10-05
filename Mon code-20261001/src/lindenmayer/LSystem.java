package lindenmayer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class LSystem extends AbstractLSystem {

    private final Map<Character, Symbol> symbolPool;
    private final Map<Symbol, String> actionNames;
    private final Map<Symbol, List<List<Symbol>>> rules;
    private List<Symbol> axiomSequence;

    public LSystem() {
        super();
        this.symbolPool = new HashMap<>();
        this.actionNames = new HashMap<>();
        this.rules = new HashMap<>();
        this.axiomSequence = new ArrayList<>();
    }

    public LSystem(long seed) {
        super(seed);
        this.symbolPool = new HashMap<>();
        this.actionNames = new HashMap<>();
        this.rules = new HashMap<>();
        this.axiomSequence = new ArrayList<>();
    }

    protected Symbol getOrCreateSymbol(char c) {
        return symbolPool.computeIfAbsent(c, Symbol::new);
    }

    @Override
    public Symbol setAction(char sym, String action) {
        Symbol s = getOrCreateSymbol(sym);
        actionNames.put(s, action);
        return s;
    }

    @Override
    public void setAxiom(String str) {
        axiomSequence = new ArrayList<>();
        for (char c : str.toCharArray()) {
            axiomSequence.add(getOrCreateSymbol(c));
        }
    }

    @Override
    public void addRule(char sym, String expansion) {
        Symbol lhs = getOrCreateSymbol(sym);
        List<Symbol> rhs = new ArrayList<>();
        for (char c : expansion.toCharArray()) {
            rhs.add(getOrCreateSymbol(c));
        }
        rules.computeIfAbsent(lhs, k -> new ArrayList<>()).add(rhs);
    }

    @Override
    public Iterator<Symbol> getAxiom() {
        return axiomSequence.iterator();
    }

    @Override
    public Iterator<Symbol> rewrite(Symbol sym) {
        List<List<Symbol>> expansions = rules.get(sym);
        if (expansions == null || expansions.isEmpty()) {
            return null;
        }
        List<Symbol> chosen = this.rndElement(expansions);
        return chosen.iterator();
    }

    @Override
    public void tell(Turtle turtle, Symbol sym) {
        String action = actionNames.get(sym);
        if (action != null) {
            turtle.action(action).run();
        }
    }

    @Override
    public Rectangle2D tell(Turtle turtle, Iterator<Symbol> seq, int rounds) {
        Point2D startPos = turtle.getPosition();
        Rectangle2D.Double bbox = new Rectangle2D.Double(startPos.getX(), startPos.getY(), 0.0, 0.0);

        while (seq.hasNext()) {
            Symbol sym = seq.next();
            processSymbol(turtle, sym, rounds, bbox);
        }
        return bbox;
    }

    private void processSymbol(Turtle turtle, Symbol sym, int rounds, Rectangle2D.Double bbox) {
        if (rounds == 0) {
            tell(turtle, sym);
            bbox.add(turtle.getPosition());
        } else {
            Iterator<Symbol> expansion = rewrite(sym);
            if (expansion == null) {
                // Symbole terminal : exécution directe au niveau courant
                tell(turtle, sym);
                bbox.add(turtle.getPosition());
            } else {
                while (expansion.hasNext()) {
                    processSymbol(turtle, expansion.next(), rounds - 1, bbox);
                }
            }
        }
    }

    @Override
    public void initFromJson(JSONObject obj, Turtle turtle) {
        // Actions
        if (obj.has("actions")) {
            JSONObject acts = obj.getJSONObject("actions");
            for (String key : acts.keySet()) {
                if (!key.isEmpty()) {
                    setAction(key.charAt(0), acts.getString(key));
                }
            }
        }

        // Axiome
        if (obj.has("axiom")) {
            setAxiom(obj.getString("axiom"));
        }

        // Règles
        if (obj.has("rules")) {
            JSONObject rulesObj = obj.getJSONObject("rules");
            for (String key : rulesObj.keySet()) {
                if (!key.isEmpty()) {
                    char lhs = key.charAt(0);
                    JSONArray expArray = rulesObj.getJSONArray(key);
                    for (int i = 0; i < expArray.length(); i++) {
                        addRule(lhs, expArray.getString(i));
                    }
                }
            }
        }

        // Paramètres de la tortue
        if (obj.has("parameters")) {
            JSONObject params = obj.getJSONObject("parameters");
            double step = params.optDouble("step", 1.0);
            double angle = params.optDouble("angle", 0.0);
            turtle.setUnits(step, angle);

            if (params.has("start")) {
                JSONArray start = params.getJSONArray("start");
                double x = start.getDouble(0);
                double y = start.getDouble(1);
                double theta = start.getDouble(2);
                turtle.init(new Point2D.Double(x, y), theta);
            }
        }
    }
}