package com.teamgp.contactmgr;

import com.teamgp.contactmgr.model.Contact;
import com.teamgp.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.jaxb.SourceType;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;

public class Application {
    //hold a reuseable reference to a SessionFactory since we only need one
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        //create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Chris", "Ramacciotti")
                .withEmail("rama@java.com")
                .withPhone(773464347L)
                .build();

        int id = save(contact);

        //display a list of contacts before the update
        /*for(Contact c: fetchAllContacts()){
            System.out.println(c);
        }*/

        //display a list of contact using Java Stream before the update
        System.out.printf("%n%nBefore the update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //get the persisted contact
        Contact c = findContactById(id);

        //update the contact
        c.setFirstName("rodora");
        c.setLastName("X");
        c.setEmail("rodorax@gma.com");
        c.setPhone(442568889);

        //persist the changes
        System.out.printf("%n%nUpdating...%n%n");
        update(c);
        System.out.printf("%n%nUpdate complete.%n%n");

        //display a list of contact using Java Stream after the update
        System.out.printf("%n%nAfter the update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //delete the contact
        System.out.printf("%n%nDeleting contact...%n%n");
        delete(c);
        System.out.printf("%n%nContact Deleted!%n%n");

        //display a list of contact using Java Stream before the update
        System.out.printf("%n%nAfter the update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static void delete(Contact contact){
        //open session
        Session session = sessionFactory.openSession();

        //begin the transaction
        session.beginTransaction();

        //use session to delete the contact
        session.delete(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    private static Contact findContactById(int id){
        //open a session
        Session session = sessionFactory.openSession();

        //retrieve the persistent object or null if it is not found
        Contact contact = session.get(Contact.class, id);

        //close the session
        session.close();

        //return Contact object
        return contact;
    }

    private static void update(Contact contact){
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //use the session to update the contact
        session.update(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){
        //open session
        Session session = sessionFactory.openSession();

        //create a criteria object
        Criteria criteria = session.createCriteria(Contact.class);

        //get a list of Contact objects according to the Criteria object
        List<Contact> contacts = criteria.list();

        //close session
        session.close();

        return contacts;
    }

    private static int save(Contact contact){
        //open session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //use the session to save the contact
        int id = (int)session.save(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();

        return id;
    }
}
