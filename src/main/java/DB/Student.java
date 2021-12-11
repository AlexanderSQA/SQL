package DB;

public class Student {
    private int id;
    private String name;
    private String sex;
    private int studentGroupId;
    private String studentsGroupName;
    private int studentCuratorId;
    private String studentsCurator;



    public Student(int id, String name, String sex, String studentsGroupName, String studentsCurator) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.studentsGroupName = studentsGroupName;
        this.studentsCurator = studentsCurator;
    }

    public Student(int id, String name, String sex, String studentsGroupName) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.studentsGroupName = studentsGroupName;
    }

    public Student() {}

    public Student(int studentGroupId, String studentsGroupName, int studentCuratorId, String studentsCurator) {
        this.studentGroupId = studentGroupId;
        this.studentsGroupName = studentsGroupName;
        this.studentCuratorId = studentCuratorId;
        this.studentsCurator = studentsCurator;
    }

    public Student(int id, String name, String sex, int studentGroupId) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.studentGroupId = studentGroupId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentGroupId=" + studentGroupId +
                ", studentsGroupName='" + studentsGroupName + '\'' +
                ", studentCuratorId=" + studentCuratorId +
                ", studentsCurator='" + studentsCurator + '\'' +
                '}';
    }
}

