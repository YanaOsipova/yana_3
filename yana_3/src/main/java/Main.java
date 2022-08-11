import dao.Dao;
import dao.impl.DAOCuratorImpl;
import dao.impl.DAOGroupImpl;
import dao.impl.DAOStudentImpl;
import dto.DTOCurator;
import dto.DTOGroup;
import dto.DTOStudent;


import java.util.List;

public class Main {
    private static final Dao<DTOGroup> groupDao = new DAOGroupImpl();
    private static final Dao<DTOStudent> studentDao = new DAOStudentImpl();
    private static final Dao<DTOCurator> curatorDao = new DAOCuratorImpl();

    private static final List<DTOCurator> curators;
    private static final List<DTOGroup> groups;
    private static final List<DTOStudent> students;

    public static void main(String[] args) {
        delete();
        createTable();
        writeData();
        printValues();
        printWomanStudents();
        updateDataForCurator();
        printGroup();
        printStudentByNameGroup();
    }

    private static void delete() {
        curatorDao.deleteAllValue();
        studentDao.deleteAllValue();
        groupDao.deleteAllValue();
    }

    private static void printStudentByNameGroup() {
        System.out.println("<-------------PRINT-STUDENT-BY-NAME-GROUP------------->");
        studentDao.findByNameGroup("first group").forEach(val -> {
            System.out.println("student id: " + val.getId() + " fio: " + val.getFio() + " sex: " + val.getSex());
            System.out.println("group id: " + val.getIdGroup().getId());
            System.out.println("<----------------------------------------------------->");
        });
    }

    private static void createTable() {
        curatorDao.createTable();
        System.out.println("<-------------------CURATOR-CREATE-------------------->");
        groupDao.createTable();
        System.out.println("<---------------------GROUP-CREATE-------------------->");
        studentDao.createTable();
        System.out.println("<-------------------STUDENT-CREATE-------------------->");
    }

    private static void writeData() {
        curatorDao.createValuesInTable(curators);
        var resultCurators = curatorDao.findAllValues();
        groups.forEach(val->{
            val.setIdCurator(resultCurators.get(0));
            resultCurators.remove(0);
        });
        groupDao.createValuesInTable(groups);
        var resultGroups = groupDao.findAllValues();
        students.forEach(val-> {
            if (students.indexOf(val) < 5) {
                val.setIdGroup(resultGroups.get(0));
            } else if (students.indexOf(val) < 10) {
                val.setIdGroup(resultGroups.get(1));
            } else {
                val.setIdGroup(resultGroups.get(2));
            }
        });
        studentDao.createValuesInTable(students);
        System.out.println("<---------------------CREATE-DATA--------------------->");
    }

    private static void printValues() {
        System.out.println("<------------------PRINT-ALL-VALUES------------------->");
        var resultStudents = studentDao.findAllValues();
        resultStudents.forEach(val -> {
            System.out.println("student id: " + val.getId() + " fio: " + val.getFio() + " sex: " + val.getSex());
            System.out.println("group id: " + val.getIdGroup().getId() + " name group: " + val.getIdGroup().getNameGroup());
            System.out.println("name curator: " + val.getIdGroup().getIdCurator().getFio());
            System.out.println("<----------------------------------------------------->");
        });
        System.out.println("<-------------------COUNT-STUDENTS-------------------->");
        System.out.println("--------------------------" + resultStudents.size() + "-------------------------->");
    }

    private static void printWomanStudents() {
        System.out.println("<-------------------WOMAN-STUDENT--------------------->");
        studentDao.findAllValues().forEach(val -> {
            if (val.getSex().equals("W")) {
                System.out.println("student id: " + val.getId() + " fio: " + val.getFio() + " sex: " + val.getSex());
                System.out.println("<----------------------------------------------------->");
            }
        });
    }

    private static void updateDataForCurator() {
        var groups = groupDao.findAllValues();
        var curators = curatorDao.findAllValues();
        groups.forEach(val -> {
            var newId = val.getIdCurator().getId() + 1;
            if (newId < 4) {
                val.setIdCurator(curators.get(newId));
            } else {
                val.setIdCurator(curators.get(1));
            }
        });
        groupDao.update(groups);
        System.out.println("<--------------------UPDATE-GROUP--------------------->");
    }

    private static void printGroup() {
        System.out.println("<-------------PRINT-GROUP-WITH-CURATOR---------------->");
        groupDao.findAllValues().forEach(val -> {
            System.out.println("group name: " + val.getNameGroup() + " name curator: " + val.getIdCurator().getFio());
            System.out.println("<----------------------------------------------------->");
        });

    }

    static {
        DTOCurator curator1 = DTOCurator.builder()
                .fio("first curator")
                .build();
        DTOCurator curator2 = DTOCurator.builder()
                .fio("second curator")
                .build();
        DTOCurator curator3 = DTOCurator.builder()
                .fio("third curator")
                .build();
        DTOCurator curator4 = DTOCurator.builder()
                .fio("fourth curator")
                .build();
        curators = List.of(curator1, curator2, curator3, curator4);

        DTOGroup group1 = DTOGroup.builder()
                .nameGroup("first group")
                .build();
        DTOGroup group2 = DTOGroup.builder()
                .nameGroup("second group")
                .build();
        DTOGroup group3 = DTOGroup.builder()
                .nameGroup("third group")
                .build();
        groups = List.of(group1, group2, group3);

        DTOStudent dtoStudent1 = DTOStudent.builder()
                .fio("first student")
                .sex("W")
                .build();
        DTOStudent dtoStudent2 = DTOStudent.builder()
                .fio("second student")
                .sex("M")
                .build();
        DTOStudent dtoStudent3 = DTOStudent.builder()
                .fio("third student")
                .sex("W")
                .build();
        DTOStudent dtoStudent4 = DTOStudent.builder()
                .fio("fourth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent5 = DTOStudent.builder()
                .fio("sixth student")
                .sex("W")
                .build();
        DTOStudent dtoStudent6 = DTOStudent.builder()
                .fio("sixth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent7 = DTOStudent.builder()
                .fio("seventh student")
                .sex("W")
                .build();
        DTOStudent dtoStudent8 = DTOStudent.builder()
                .fio("eighth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent9 = DTOStudent.builder()
                .fio("ninth student")
                .sex("W")
                .build();
        DTOStudent dtoStudent10 = DTOStudent.builder()
                .fio("tenth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent11 = DTOStudent.builder()
                .fio("eleventh student")
                .sex("W")
                .build();
        DTOStudent dtoStudent12 = DTOStudent.builder()
                .fio("twelfth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent13 = DTOStudent.builder()
                .fio("thirteenth student")
                .sex("W")
                .build();
        DTOStudent dtoStudent14 = DTOStudent.builder()
                .fio("fourteenth student")
                .sex("M")
                .build();
        DTOStudent dtoStudent15 = DTOStudent.builder()
                .fio("fifteenth student")
                .sex("W")
                .build();
        students = List.of(dtoStudent1, dtoStudent2, dtoStudent3, dtoStudent4, dtoStudent5, dtoStudent6,
                dtoStudent7, dtoStudent8, dtoStudent9, dtoStudent10, dtoStudent11, dtoStudent12, dtoStudent13,
                dtoStudent14, dtoStudent15);
    }
}
