package com.teamgp.contactmgr;

import com.teamgp.contactmgr.model.Contact;
import com.teamgp.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;

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

        save(contact);

        //display a list of contacts
        for(Contact c: fetchAllContacts()){
            System.out.println(c);
        }

        //display a list of contact using Java Stream
        fetchAllContacts().stream().forEach(System.out::println);
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

    private static void save(Contact contact){
        //open session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //use the session to save the contact
        session.save(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }
}
