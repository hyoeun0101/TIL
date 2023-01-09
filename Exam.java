import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Exam{
    public static void main(String[] args) {

        IntStream intStream = new Random().ints(1,46).limit(6);
        intStream.sorted().forEach(System.out::println);

        


    }
}