import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class GroupingByExam {
    public static void main(String[] args) {
        Student[] stuArr={
            new Student("김자바", true,  1, 1, 300),
            new Student("나자바", false,  1, 1, 250),
            new Student("다자바", true,  1, 1, 200),
            new Student("라자바", true,  1, 2, 150),
            new Student("마자바", false,  1, 2, 100),
            new Student("바자바", true,  1, 2, 50),
            new Student("사자바", false,  2, 1, 100),
            new Student("아자바", true,  2, 1, 150),
            new Student("자자바", true,  2, 1, 200),
            new Student("차자바", false,  2, 2, 300),
            new Student("카자바", false,  2, 2, 250),
            new Student("타자바", false,  2, 2, 300),
            new Student("파자바", true,  2, 3, 100)
        };

        System.out.println("1. 반별로 단순 그룹화");

        Map<Integer, List<Student>> stuByBan = Stream.of(stuArr).collect(groupingBy(Student::getBan));
        // Map<Integer, List<Student>> stuByBan = Stream.of(stuArr).collect(groupingBy(Student::getBan, toList()));와 같음

        for(Integer ban : stuByBan.keySet()){
            System.out.println(ban+"반");
            for(Student s : stuByBan.get(ban)){
                System.out.println(s);
            }
        }

        System.out.println("2. 성적별로 그룹화");
        Map<Student.Level,List<Student>> stuByLevel = Stream.of(stuArr).collect(
            groupingBy(s->{
                if(s.getScore() >=200)return Student.Level.HIGH;
                else if(s.getScore() >= 100)return Student.Level.MID;
                else return Student.Level.LOW;
            }));

            TreeSet<Student.Level> keySet = new TreeSet<>(stuByLevel.keySet());
            for(Student.Level key : keySet){
                System.out.printf("[%s]",key);
                
                for(Student s:stuByLevel.get(key))
                    System.out.println(s);
                System.out.println();
            }

        System.out.println("단순 그룹화+통계 (성적별 학생수)");
        Map<Student.Level,Long> stuCntByLevel = Stream.of(stuArr).collect(
            groupingBy(s->{
                if(s.getScore() >=200)return Student.Level.HIGH;
                else if(s.getScore() >= 100)return Student.Level.MID;
                else return Student.Level.LOW;
            },counting()));


        for(Student.Level key : stuCntByLevel.keySet()){
            System.out.printf("[%s]- %d명\n",key,stuCntByLevel.get(key));
        }

        System.out.println("다중그룹화 (학년별, 반별)");

        Map<Integer, Map<Integer,List<Student>>> s= Stream.of(stuArr).collect(groupingBy(Student::getHak,groupingBy(Student::getBan)));
        for(Map<Integer,List<Student>> hak: s.values()){
            for(List<Student> ban : hak.values()){
                for(Student student : ban){
                    System.out.println(student);
                }

            }
        }
    }
}



class Student{
    String name;
    boolean isMale;
    int hak;
    int ban;
    int score;
    Student(String name,boolean isMale, int hak, int ban, int score){
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