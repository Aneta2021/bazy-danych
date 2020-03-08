package pl.szczepanski.marek.demo.databases;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import pl.szczepanski.marek.demo.databases.entities.Course;
import pl.szczepanski.marek.demo.databases.entities.Student;

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
            c.setStartDate(LocalDate.of(2020, 3, 8));
            session.persist(c);

            c = new Course();
            c.setName("code 2");
            c.setStartDate(LocalDate.of(2020, 4, 1));
            session.persist(c);

            tx.commit();
            tx = session.beginTransaction();

            Course course1 = session.find(Course.class, 1);
            System.out.println("\n course with id 1 found: " + course1.getName());
            session.evict(course1);
            course1.setName("course 1 new name");

            tx.commit();
            tx = session.beginTransaction();

            Query<Course> query = session.createQuery("from Course where name like :nameparam", Course.class);
            query.setParameter("nameparam", "code%");
            final List<Course> courseList = query.list();
            courseList.forEach(courseItem -> System.out.println("course found: " + courseItem.getName()));

            tx.commit();
            tx = session.beginTransaction();

            Course course5 = session.find(Course.class, 1);

            Student student1 = new Student();
            student1.setName("Marek");
            student1.setAddress("dom");
            student1.setCourse(course5);
            session.persist(student1);

            Student student2 = new Student();
            student2.setName("Tomek");
            session.persist(student2);
            student2.setAddress("zamek");
            student2.setCourse(course5);

            tx.commit();
            session.clear();
            tx = session.beginTransaction();

            Course course6 = session.find(Course.class, 1);
            Set<Student> course6Students = course6.getStudents();
            System.out.println("Number of students: " + course6Students.size());
            course6Students.forEach(studentItem -> System.out.println("name: " + studentItem.getName()));

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
