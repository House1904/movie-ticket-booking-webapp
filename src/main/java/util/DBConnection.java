package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBConnection {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ProjectLoad");

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}
