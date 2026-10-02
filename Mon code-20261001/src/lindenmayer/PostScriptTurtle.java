package lindenmayer;

import java.awt.geom.Point2D;
import java.util.Locale;

public class PostScriptTurtle extends TrueTurtle{

    // attribut pour le calcul de la BoundingBox
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    public PostScriptTurtle() {
        super();
        writerHeder();
    }

    private void writerHeder() {
        // En-tete obligatoire pour le  EPS
        System.out.println("%!PS-Adobe-3.0 EPSF-3.0");
        System.out.println("%%BoundingBox: (atend)");
        System.out.println("%%EndComments");

        // recuperation de la position initiale et initialiser le chemin
        Point2D pos = getPosition();
        this.minX = this.maxX = pos.getX();
        this.minY = this.maxY = pos.getY();

        // locale.US pour la conventiion americain sans virgule 2f pour
        // 2 chiffre apres la virgule pour evider le probleme d'arrondi
        System.out.printf(Locale.US,"newpath %.2f %.2f moveto%n", pos.getX(),pos.getY());

    }


    // permet de bouger la tortue sans trace
    @Override
    public void move() {
        super.move();
        Point2D pos = getPosition();
        updateBounds(pos.getX(),pos.getY());
        System.out.printf(Locale.US,"%.2f %.2f moveto%n",pos.getX(),pos.getY());
    }
    // bouge la torture a une position en laissant des trace
    @Override
    public void draw() {
        super.draw();
        Point2D pos = getPosition();
        updateBounds(pos.getX(),pos.getY());
        System.out.printf(Locale.US,"%.2f %.2f lineto%n",pos.getX(),pos.getY());
    }


    private void updateBounds(double x, double y) {
        minX = Math.min(minX,x);
        maxX = Math.max(maxX,x);
        minY = Math.min(minY,y);
        maxY = Math.max(maxY,y);
    }

    @Override
    public void push() {
        super.push(); // sauvegarde la position courrante sur la pile
        System.out.println("currentpoint stroke newpath moveto");
    }

    @Override
    public void pop() {
        super.pop(); // restaure la postion et angle depuis la pile Java
        Point2D pos = getPosition(); // Recupere la position restauree
        System.out.printf(Locale.US,"stroke %.2f %.2f newpath moveto%n" ,pos.getX(),pos.getY());
    }


    public void close() {
        System.out.println("stroke");
        System.out.println("%%Trailer");
        System.out.printf(Locale.US,"%%%%BoundingBox: %d %d %d %d%n", (int) Math.floor(minX),(int) Math.floor(minY), (int) Math.ceil(maxX),(int) Math.ceil(maxY));
    }



}
