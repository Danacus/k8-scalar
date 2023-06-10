package be.kuleuven.distrinet.scalar.users;

import be.kuleuven.distrinet.scalar.cassandra.DatastaxCassandraClient;
import be.kuleuven.distrinet.scalar.core.User;
import be.kuleuven.distrinet.scalar.core.UserPool;
import be.kuleuven.distrinet.scalar.exceptions.DataException;
import be.kuleuven.distrinet.scalar.exceptions.RequestException;

public class CassandraAdminInitUser extends CassandraUser {

    public CassandraAdminInitUser(UserPool pool) {
        super(pool);
        cassandra.createSchema();
    }

    @Override
    public void mainLoop() throws DataException, RequestException {

    }
}
