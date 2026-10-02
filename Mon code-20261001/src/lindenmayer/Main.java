package lindenmayer;

public class Main {
    public static void main(String[] args) {
        if(args.length < 2) {
            System.err.println("Sorry you need to put two argument .json and iterations");
        }

        String JsonPath = args[0];
        int iterations = Integer.parseInt(args[1]);


    }
}
