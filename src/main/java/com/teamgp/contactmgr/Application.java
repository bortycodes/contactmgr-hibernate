package com.teamgp.contactmgr;

import com.teamgp.contactmgr.model.Contact;
import com.teamgp.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import org.hibernate.service.ServiceRegistry;

public class Application {
    //hold a reuseable reference to a SessionFactory since we only need one
    //private static final SessionFactory sessionFactory = buildSessionFactory();

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
        System.out.println(contact);
    }
}
