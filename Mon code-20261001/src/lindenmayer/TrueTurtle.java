package lindenmayer;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;

public class TrueTurtle implements Turtle{

    // permet de memoriser chaque branche revenir a la postion du depart
    protected static class State {
        // position cartésienne (x, y) de la pointe du crayon
        final Point2D position;
        final double angle;

        public State(Point2D position, double angle) {
            // clone afin d'eviter que le futur deplacement n'ecrase celle
            // enregistrer  le cast (Point2D) requis car clone() renvoie un object
            this.position = (Point2D) position.clone();
            this.angle = angle;
        }
    }

    // Position courante (x, y) de la pointe du crayon
    protected Point2D position;

    // Angle d'orientation actuel en degre
    protected double angle;

    // Distance parcourue lors d'un deplacement
    protected  double unitStep;

    // Valeur de l'angle ajoute ou soustrait lors d'un virage
    protected double unitAngle;

    // Pile LIFO pour memoiriser les embrachement de l'abre
    protected Deque<State> stack ;

    public  TrueTurtle() {
        // met par defaut (0,0)
        this.position = new Point2D.Double(0.0,0.0);
        this.angle = 90.0;
        this.unitStep = 1.0;
        this.unitAngle = 0.0;
        this.stack = new ArrayDeque<>();
    }


    @Override
    public void init(Point2D position, double angle) {
        // Renitialise la postion du depart avec une copie independante
        this.position = (Point2D) position.clone();
        this.angle = angle;
        // vide l'historique de la pile pour recommencer un trace a zero
        this.stack.clear();
    }

    @Override
    public Point2D getPosition() {
        return (Point2D) this.position.clone();
    }

    @Override
    public double getAngle(){
        return this.angle;
    }

    @Override
    public void setUnits(double step, double delta) {
        this.unitStep = step;
        this.unitAngle = delta;
    }

    @Override
    public double getUnitStep() {
        return this.unitStep;
    }

    @Override
    public double getUnitAngle() {
        return this.unitAngle;
    }
    @Override
    public void draw() {
        advance();
    }

    @Override
    public void move() {
        advance();
    }

    @Override
    public void turnR() {
        // Sens horaire :
        this.angle -= this.unitAngle;
    }

    @Override
    public void turnL() {
        this.angle += this.unitAngle;
    }

    // Gestion de la pile branches
    @Override
    public void push() {
        this.stack.push(new State(this.position,this.angle));
    }
    @Override
    public void pop() {
        if (!this.stack.isEmpty()) {
            State s = this.stack.pop();
            this.position = (Point2D) s.position.clone();
            this.angle = s.angle;
        }
    }

    @Override
    public void stay() {
        // Ne modifie rien
    }

    /**
     * Calcule la nouvelle position cartésienne de la tortue en appliquant un pas unitaire
     * selon son orientation courante via les fonctions trigonométriques.
     * Convertit l'angle de degrés en radians pour les calculs.
     *
     * @return Les nouvelles coordonnées sous forme d'un objet Point2D.
     */
    protected void advance() {
        double rad = Math.toRadians(this.angle);
        double newX = this.position.getX() + this.unitStep * Math.cos(rad);
        double newY = this.position.getY() + this.unitStep * Math.sin(rad);
        this.position.setLocation(newX, newY);
    }
}




