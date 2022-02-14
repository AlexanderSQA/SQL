package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/otus";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {

//            createCuratorTable(connection);
//            createGroupTable(connection);
//            createStudentTable(connection);

//            List<String> curators = List.of("Петров Петр Петрович", ";sdfgljkd;flg", "sdfsfdsd");
//            insertDataIntoCuratorTable(connection, curators);
//            insertDataIntoGroupTable(connection, "Группа 3", "3");
//            insertDataIntoStudentTable(connection, "Студент 15", "3", "Ж");
//
//            updateDataIntoCuratorTable(connection, "Сидоров Остап Бенедиктович", "3");
//            updateDataIntoStudentTable(connection, "Студент 14", "15");

            printAllStudentInfo(connection);
            System.out.println();
            printCountOfStudent(connection);
            System.out.println();
            printGirlsFromStudentTable(connection);
            System.out.println();
//            updateCuratorIdInGroup(connection, "3", "3");
//            updateCuratorIdInGroup(connection, "1", "4");
            printGroupWithCurator(connection);
            System.out.println();
            printStudentFromGroup(connection, "Группа 1");
        }

    }

    private static final String CREATE_CURATOR_SQL =
            "CREATE TABLE IF NOT EXISTS Curator" +
                    "(id int auto_increment primary key," +
                    " fio varchar(50))";

    private static final String CREATE_GROUP_SQL =
            "CREATE TABLE IF NOT EXISTS Groupp(id int auto_increment primary key, " +
                    "name varchar(50), " +
                    "id_curator int, " +
                    "FOREIGN KEY(id_curator) REFERENCES Curator(id))";

    private static final String CREATE_STUDENT_SQL =
            "CREATE TABLE IF NOT EXISTS Student(id int auto_increment primary key, " +
                    "name varchar(50), " +
                    "id_group int, " +
                    "sex varchar(1), " +
                    "FOREIGN KEY(id_group) REFERENCES Groupp(id))";

    private static final String SELECT_ALL_STUDENT_INFO =
            "select s.id, s.name, s.sex, g.name, c.fio from student s " +
                    "join groupp g on s.id_group = g.id " +
                    "join curator c on c.id = g.id_curator " +
                    "order by s.id";

    private static final String COUNT_OF_STUDETS =
            "select count(id) " +
                    "from student";

    private static final String SELECT_GIRLS_FROM_STUDENT =
            "select count(id) " +
                    "from student " +
                    "where sex = 'Ж'";

    private static final String INSERT_INTO_CURATOR =
            "INSERT INTO Curator (fio) VALUES(?)";

    private static final String INSERT_INTO_GROUP =
            "INSERT INTO Groupp (name, id_curator) VALUES(?, ?)";

    private static final String INSERT_INTO_STUDENT =
            "INSERT INTO Student (name, id_group, sex) VALUES(?, ?, ?)";

    private static final String UPDATE_FIO_IN_CURATOR =
            "UPDATE Curator set fio = ? where id = ?";

    private static final String UPDATE_NAME_IN_STUDENT =
            "UPDATE Student set name = ? where id = ?";

    private static final String UPDATE_CURATOR_ID_IN_GROUP =
            "update groupp set id_curator = ? where id = ?";

    private static final String SELECT_GROUP_WITH_CURATOR =
            "select g.id, g.name, g.id_curator, c.fio " +
                    "from groupp g " +
                    "join curator c on c.id = g.id_curator " +
                    "group by g.name;";

    private static final String SELECT_STUDENT_FROM_GROUP =
            "select * " +
                    "from student s " +
                    "where s.id_group = (select id from groupp where name = ?)";

    public static void createCuratorTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_CURATOR_SQL);
        }
    }

    public static void createGroupTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_GROUP_SQL);
        }
    }

    public static void createStudentTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_STUDENT_SQL);
        }
    }

    // вывод информацию о всех студентах включая название группы и имя куратора
    public static void printAllStudentInfo(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_STUDENT_INFO);

            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    System.out.println(columnName + " : " + columnValue);
                }
            }
        }
    }

    //вывод на экран количество студентов
    public static void printCountOfStudent(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(COUNT_OF_STUDETS);

            resultSet.next();
            int counter = resultSet.getInt(1);
            System.out.printf("Количество студентов : %s", counter);
        }
    }

    //вывод студенток
    public static void printGirlsFromStudentTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_GIRLS_FROM_STUDENT);

            resultSet.next();
            int counter = resultSet.getInt(1);
            System.out.printf("Количество студенток : %s", counter);
        }
    }

    public static void insertDataIntoCuratorTable(Connection connection, List<String> curators) throws SQLException {
        curators.forEach(s -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CURATOR)) {
                preparedStatement.setString(1, s);
                int insertedRowNumb = preparedStatement.executeUpdate();
                System.out.println("Inserted rows number: " + insertedRowNumb);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public static void insertDataIntoGroupTable(Connection connection, String name, String idCurator) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_GROUP)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, idCurator);
            int insertedRowNumb = preparedStatement.executeUpdate();
            System.out.println("Inserted rows number: " + insertedRowNumb);
        }
    }

    public static void insertDataIntoStudentTable(Connection connection, String name, String idGroup, String sex) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_STUDENT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, idGroup);
            preparedStatement.setString(3, sex);
            int insertedRowNumb = preparedStatement.executeUpdate();
            System.out.println("Inserted rows number: " + insertedRowNumb);
        }
    }

    public static void updateDataIntoCuratorTable(Connection connection, String newFio, String id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FIO_IN_CURATOR)) {
            preparedStatement.setString(1, newFio);
            preparedStatement.setString(2, id);
            int updatedRowsNumb = preparedStatement.executeUpdate();
            System.out.println("Updated rows: " + updatedRowsNumb);
        }
    }

    public static void updateDataIntoStudentTable(Connection connection, String newFio, String id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NAME_IN_STUDENT)) {
            preparedStatement.setString(1, newFio);
            preparedStatement.setString(2, id);
            int updatedRowsNumb = preparedStatement.executeUpdate();
            System.out.println("Updated rows: " + updatedRowsNumb);
        }
    }

    //обновить куратора в группе
    public static void updateCuratorIdInGroup(Connection connection, String newIdCurator, String idGroup) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURATOR_ID_IN_GROUP)) {
            preparedStatement.setString(1, newIdCurator);
            preparedStatement.setString(2, idGroup);
            int updatedRowsNumb = preparedStatement.executeUpdate();
            System.out.println("Updated rows: " + updatedRowsNumb);
        }
    }

    //вывод списка групп с их кураторами
    public static void printGroupWithCurator(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_GROUP_WITH_CURATOR);
            List<Student> group = new ArrayList<>();

            while (resultSet.next()) {
                int idGroup = resultSet.getInt(1);
                String studentGroup = resultSet.getString(2);
                int idCurator = resultSet.getInt(3);
                String curatorFio = resultSet.getString(4);

                Student student = new Student(idGroup, studentGroup, idCurator, curatorFio);
                group.add(student);
                group.forEach(System.out::println);
            }
        }
    }

    //вывод на экран студентов из определенной группы(поиск по имени группы)
    public static void printStudentFromGroup(Connection connection, String groupName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_FROM_GROUP)) {
            preparedStatement.setString(1, groupName);
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT_STUDENT_FROM_GROUP);
                while (resultSet.next()) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        String columnValue = resultSet.getString(i);
                        System.out.println(columnName + " : " + columnValue);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

//    public static void printStudentFromGroupOne(Connection connection, String groupName) throws SQLException {
//        try (Statement statement = connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery(SELECT_STUDENT_FROM_GROUP);
//            List<Student> studentsFromGroup = new ArrayList<>();
//
//            while (resultSet.next()) {
//                int idStudent = resultSet.getInt(1);
//                String studentName = resultSet.getString(2);
//                int idGroup = resultSet.getInt(3);
//                String sex = resultSet.getString(4);
//
//                Student student = new Student(idStudent, studentName, sex, idGroup);
//                studentsFromGroup.add(student);
//                studentsFromGroup.forEach(System.out::println);
//            }
//
//        }
    }
}



