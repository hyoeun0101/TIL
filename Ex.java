import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class Ex{
    public static void main(String[] args) {
        String[] lineArr = {
            "zBB SSS sss rrr xdf dgwr sfsg",
            "asdf dfg wer dfg wer"
        };
        Stream<String> lineStream = Arrays.stream(lineArr);

        
        // lineStream.flatMap(line->Stream.of(line.split(" +")))//공백
        //             .map(String::toLowerCase)
        //             .distinct()
        //             .sorted()
        //             .forEach(System.out::println);


    }
}
