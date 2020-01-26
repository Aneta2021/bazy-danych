package pl.szczepanski.marek.demo.databases;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import pl.szczepanski.marek.demo.databases.entities.Course;
import pl.szczepanski.marek.demo.databases.entities.Student;
import pl.szczepanski.marek.demo.databases.entities.Teacher;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class TestCode {

    private final SessionFactory hibernateFactory;

    public TestCode(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @PostConstruct
    public void testHibernateCode() {
        System.out.printf("\n\n---------------------\nTest code started\n\n");

        Transaction tx = null;
        try (Session session = hibernateFactory.openSession()) {
            tx = session.beginTransaction();

            Course c = new Course();
            c.setName("code 1");
            c.setStartDate(LocalDate.of(2020, 04, 13));
            session.persist(c);

            c = new Course();
            c.setName("code 2");
            c.setStartDate(LocalDate.of(2020, 02, 24));
            session.persist(c);

            tx.commit();

            tx = session.beginTransaction();
            final Course course3 = session.find(Course.class, 1);
            System.out.println("\nFound course " + course3.getName());
            session.evict(course3);
            course3.setName("Course new name");

            tx.commit();

            tx = session.beginTransaction();

            final Query<Course> query = session.createQuery(
                    "from Course where name like :nameparam", Course.class);
            query.setParameter("nameparam", "code%");
            final List<Course> list = query.list();
            list.forEach(course -> System.out.println("course name found: " + course.getName()));

            tx.commit();
            tx = session.beginTransaction();

            final Course course5 = session.find(Course.class, 1);

            Student s1 = new Student();
            s1.setName("Marek");
            s1.setAddress("Domek");
            s1.setCourse(course5);
            session.persist(s1);

            Student s2 = new Student();
            s2.setName("Tomek");
            s2.setAddress("Inny domek");
            s2.setCourse(course5);
            session.persist(s2);

            tx.commit();
            tx = session.beginTransaction();

            final Course course6 = session.find(Course.class, 1);
            final Set<Student> students = course6.getStudents();
            System.out.println("Number of students: "
                    + students.size());
            students.forEach(student -> {
                System.out.println("name: " + student.getName() +
                        " course: " + student.getCourse().getName());
            });

            tx.commit();
            tx = session.beginTransaction();

            final Course course7 = session.find(Course.class, 1);
            Teacher t1 = new Teacher();
            t1.setName("Wojtek");
            t1.setSubject("Java");
            t1.getCourses().add(course7);
            session.persist(t1);

            Teacher t2 = new Teacher();
            t2.setName("Franek");
            t2.setSubject("Java");
            t2.getCourses().add(course7);
            session.persist(t2);

            session.flush();

            System.out.println("-=List teachers=-");
            course7.getTeachers().forEach(
                    teacher -> System.out.println("teacher: " + teacher.getName())
            );

            tx.commit();
        } catch (Exception ex) {
            if (tx != null && !tx.getRollbackOnly()) {
                tx.rollback();
            }
            throw ex;
        }


        System.out.printf("\n---------------------\nTest code completed\n\n");

    }
}
