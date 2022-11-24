import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ex{
    public static void main(String[] args) {
        IntStream intStream = new Random().ints(1,46).distinct().limit(6);
        // OptionalInt max = intStream.peek(i->System.out.println(i)).reduce(Integer::max);
        // System.out.println(max.getAsInt());

        System.out.println();

        Optional<Integer> max1 = intStream.boxed().collect(Collectors.reducing(Integer::max));

        System.out.println(max1);
        }
}
