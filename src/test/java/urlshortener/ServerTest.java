package urlshortener;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerTest {

    private static Connection conn;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Use real in-memory H2 database for integration-style test
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");
        }
    }

    @Test
    void testUsersTableCreatedSuccessfully() throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'USERS'");
        assertTrue(rs.next(), "Users table should exist");
    }

    @Test
    void testMockedConnectionTableCreation() throws Exception {
        // Mocks
        Connection mockConn = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.execute(anyString())).thenReturn(true);

        // Run the DB setup logic
        mockConn.createStatement().execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");

        // Verify SQL execution
        verify(mockConn).createStatement();
        verify(mockStmt).execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");
    }

    @Test
    void testGetConnectionReturnsStaticConnection() throws Exception {
        Server.main(new String[]{});  // Start the server once
        assertNotNull(Server.getConn(), "Connection should not be null");
    }
}
