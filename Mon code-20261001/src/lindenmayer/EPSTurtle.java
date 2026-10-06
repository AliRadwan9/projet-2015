package lindenmayer;

import java.io.PrintStream;
import java.util.Locale;

public class EPSTurtle extends TrueTurtle {

    private final PrintStream out;

    public EPSTurtle(PrintStream out) {
        super();
        this.out = out;
    }

    public EPSTurtle() {
        this(System.out);
    }

    @Override
    public void draw() {
        super.draw();
        out.printf(Locale.US, "%.2f %.2f lineto\n", getPosition().getX(), getPosition().getY());
    }

    @Override
    public void move() {
        super.move();
        out.printf(Locale.US, "%.2f %.2f moveto\n", getPosition().getX(), getPosition().getY());
    }

    @Override
    public void push() {
        super.push();
        out.println("currentpoint stroke newpath moveto");
    }

    @Override
    public void pop() {
        super.pop();
        out.printf(Locale.US, "stroke %.2f %.2f newpath moveto\n", getPosition().getX(), getPosition().getY());
    }
}