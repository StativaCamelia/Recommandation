package com.example.Recommendation.repo;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.*;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.example.Recommendation.models.SparQLQuery;
import com.stardog.stark.io.RDFFormats;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultWriters;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Repository
public class RecommendationStardogRepo {

    private static final String url = "http://localhost:5820";
    private static final String username = "admin";
    private static final String password = "admin";
    private static final String to = "superheroes";
    private static final String NS = "http://api.stardog.com/";
    public static final String S = "PREFIX foaf:<http://xmlns.com/foaf/0.1/> " +
            "select * { ?s rdf:type foaf:Person }";

    private static int maxPool = 200;
    private static int minPool = 10;
    private static boolean reasoningType = false;
    private static long blockCapacityTime = 900;
    private static TimeUnit blockCapacityTimeUnit = TimeUnit.SECONDS;
    private static long expirationTime = 300;
    private static TimeUnit expirationTimeUnit = TimeUnit.SECONDS;

    /**
     * Creates a connection to the DBMS itself so we can perform some administrative actions.
     */
    public static void createAdminConnection() {
        try (final AdminConnection aConn = AdminConnectionConfiguration.toServer(url)
                .credentials(username, password)
                .connect()) {

            // A look at what databases are currently in Stardog
            aConn.list().forEach(item -> System.out.println(item));

            // Checks to see if the 'testDB' is in Stardog. If it is, we are
            // going to drop it so we are starting fresh
            if (aConn.list().contains(to)) {
                aConn.drop(to);
            }
            // Convenience function for creating a persistent
            // database with all the default settings.
            aConn.disk(to).create();
        }
    }

    /**
     * Now we want to create the configuration for our pool.
     *
     * @param connectionConfig the configuration for the connection pool
     * @return the newly created pool which we will use to get our Connections
     */
    private static ConnectionPool createConnectionPool(ConnectionConfiguration connectionConfig) {
        ConnectionPoolConfig poolConfig = ConnectionPoolConfig
                .using(connectionConfig)
                .minPool(minPool)
                .maxPool(maxPool)
                .expiration(expirationTime, expirationTimeUnit)
                .blockAtCapacity(blockCapacityTime, blockCapacityTimeUnit);

        return poolConfig.create();
    }

    /**
     * Obtains the Stardog connection from the connection pool
     *
     * @param connectionPool the connection pool to get our connection
     * @return Stardog Connection
     */
    public static Connection getConnection(ConnectionPool connectionPool) {
        return connectionPool.obtain();
    }

    public SelectQueryResult getRecommendation(SparQLQuery queryValue) {
        ConnectionConfiguration connectionConfig = ConnectionConfiguration
                .to(to)
                .server(url)
                .reasoning(reasoningType)
                .credentials(username, password);

        createAdminConnection();


        // creates the Stardog connection pool
        ConnectionPool connectionPool = createConnectionPool(connectionConfig);

        try (Connection connection = getConnection(connectionPool)) {
            // first start a transaction. This will generate the contents of
            // the database from the N3 file.
            connection.begin();

            // declare the transaction
            connection.add().io().format(RDFFormats.N3).stream(new FileInputStream("src/main/resources/marvel.rdf"));

            // and commit the change
            connection.commit();

            // Query the database to get our list of Marvel superheroes and print the results to the console
            SelectQuery query = connection.select(queryValue.getQuery());
            QueryResultWriters.write(query.execute(), System.out, TextTableQueryResultWriter.FORMAT);

            return query.execute();

        } catch (StardogException | IOException e) {
            e.printStackTrace();
        } finally {
            connectionPool.shutdown();
        }

        return null;
    }


}
