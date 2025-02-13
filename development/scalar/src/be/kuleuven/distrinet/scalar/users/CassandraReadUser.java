package be.kuleuven.distrinet.scalar.users;

import be.kuleuven.distrinet.scalar.cassandra.DatastaxCassandraClient;
import be.kuleuven.distrinet.scalar.core.User;
import be.kuleuven.distrinet.scalar.core.UserPool;
import be.kuleuven.distrinet.scalar.exceptions.DataException;
import be.kuleuven.distrinet.scalar.exceptions.InternalException;
import be.kuleuven.distrinet.scalar.exceptions.RequestException;
import be.kuleuven.distrinet.scalar.requests.CassandraReadRequest;

public class CassandraReadUser extends CassandraUser {
    public CassandraReadUser(UserPool pool) {
        super(pool);
    }

    public DatastaxCassandraClient getCassandraClient() {
    	return cassandra;
    }

    @Override
    public void mainLoop() throws DataException {
        CassandraReadRequest request = new CassandraReadRequest(this);
        try {
            request.doRequest();
        } catch (RequestException e) {
            throw new InternalException(e);
        }
    }
}