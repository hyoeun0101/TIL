import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.*;

import static java.util.Comparator.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PartitioningByExam {
    public static void main(String[] args) {
        Student2[] stuArr={
            new Student2("김자바", true,  1, 1, 300),
            new Student2("나자바", false,  1, 1, 250),
            new Student2("다자바", true,  1, 1, 200),
            new Student2("라자바", true,  1, 2, 150),
            new Student2("마자바", false,  1, 2, 100),
            new Student2("바자바", true,  1, 2, 50),
            new Student2("사자바", false,  2, 1, 100),
            new Student2("아자바", true,  2, 1, 150),
            new Student2("자자바", true,  2, 1, 200),
            new Student2("차자바", false,  2, 2, 300),
            new Student2("카자바", false,  2, 2, 250),
            new Student2("타자바", false,  2, 2, 300),
            new Student2("파자바", true,  2, 3, 100)

        };

        System.out.println("1. 성별을 기준으로 분할");
        Map<Boolean,List<Student2>> stuBySex =Stream.of(stuArr).collect(partitioningBy(Student2::getIsMale));


        List<Student2> maleStudent = stuBySex.get(true);
        List<Student2> famaleStudent = stuBySex.get(false);

        for(Student2 m : maleStudent)System.out.println(m);
        System.out.println("-------");
        for(Student2 m : famaleStudent)System.out.println(m);

        System.out.println("2. 분할+통계");
        Map<Boolean, Long> stuNumBySex = Stream.of(stuArr).collect(
            partitioningBy(Student2::getIsMale,counting())); 

        System.out.println("남학생 수: "+stuNumBySex.get(true));
        System.out.println("여학생 수: "+stuNumBySex.get(false));

        System.out.println("3. 분할+통계, 성별 1등 구하기");
        //maxBy()는 반환타입이 Optional
        Map<Boolean, Optional<Student2>> topScoreBySex = Stream.of(stuArr).collect(
            partitioningBy(Student2::getIsMale, maxBy(comparingInt(Student2::getScore))));

        System.out.println("남학생 1등"+topScoreBySex.get(true).get());
        System.out.println("여학생 1등"+topScoreBySex.get(false).get());

        System.out.println("4. 3에서 comOptional 풀기");
        Map<Boolean, Student2> topScoreBySex2 = Stream.of(stuArr).collect(partitioningBy(Student2::getIsMale, collectingAndThen(maxBy(comparingInt(Student2::getScore)), Optional::get)));

        System.out.println(topScoreBySex2.get(true));

        //다중 분할
        Map<Boolean, Map<Boolean,List<Student2>>> failedStuBySex = Stream.of(stuArr).collect(partitioningBy(Student2::getIsMale,partitioningBy(s->s.getScore()<100)));
        List<Student2> failMale = failedStuBySex.get(true).get(true);
        System.out.println("failMale");

        for(Student2 m : failMale)System.out.println(m);

    }
    
}



class Student2{
    String name;
    boolean isMale;
    int hak;
    int ban;
    int score;
    Student2(String name,boolean isMale, int hak, int ban, int score){
        this.name = name;
        this.isMale = isMale;
        this.hak = hak;
        this.ban = ban;
        this.score = score;
    }
    
    String getName(){
        return name;
    }
    boolean getIsMale(){
        return isMale;
    }
    int getHak(){
        return hak;
    }
    int getBan(){
        return ban;
    }
    int getScore(){
        return score;
    }
    @Override
    public String toString(){
        return String.format("%s, %s, %d학년 %d반, %d점",name, isMale?"남":"여",hak,ban,score);
    }

    enum Level{HIGH, MID, LOW}

}