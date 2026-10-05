package lindenmayer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileReader;
import java.util.Locale;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar lindenmayer.jar <fichier.json> <rounds>");
            System.exit(1);
        }

        String jsonPath = args[0];
        int rounds = Integer.parseInt(args[1]);

        try {
            JSONObject obj = new JSONObject(new JSONTokener(new FileReader(jsonPath)));
            EPSTurtle turtle = new EPSTurtle(System.out);
            LSystem lsystem = new LSystem();

            // Initialisation de la grammaire et des paramètres
            lsystem.initFromJson(obj, turtle);

            // En-tête EPS
            System.out.println("%!PS-Adobe-3.0 EPSF-3.0");
            System.out.printf("%%%%Title: (%s)\n", jsonPath);
            System.out.println("%%Creator: (lindenmayer.Main)");
            System.out.println("%%BoundingBox: (atend)");
            System.out.println("%%EndComments");

            // Initialiser le chemin PostScript à la position initiale
            Point2D startPos = turtle.getPosition();
            System.out.printf(Locale.US, "newpath %.2f %.2f moveto\n", startPos.getX(), startPos.getY());

            // Exécution du L-système
            Rectangle2D bbox = lsystem.tell(turtle, lsystem.getAxiom(), rounds);

            // Fermeture du tracé final
            System.out.println("stroke");

            // Trailer EPS avec BoundingBox englobant
            int minX = (int) Math.floor(bbox.getMinX());
            int minY = (int) Math.floor(bbox.getMinY());
            int maxX = (int) Math.ceil(bbox.getMaxX());
            int maxY = (int) Math.ceil(bbox.getMaxY());

            System.out.println("%%Trailer");
            System.out.printf("%%%%BoundingBox: %d %d %d %d\n", minX, minY, maxX, maxY);
            System.out.println("%%EOF");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}