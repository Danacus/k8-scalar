package be.kuleuven.distrinet.scalar.users;

import be.kuleuven.distrinet.scalar.cassandra.DatastaxCassandraClient;
import be.kuleuven.distrinet.scalar.core.User;
import be.kuleuven.distrinet.scalar.core.UserPool;

public abstract class CassandraUser extends User {
    protected DatastaxCassandraClient cassandra;

    public CassandraUser(UserPool pool) {
        super(pool);
        cassandra = DatastaxCassandraClient.getInstance(super.targetUrl());
    }
}
