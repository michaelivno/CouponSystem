package ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import CouponSystemException.GeneralCouponSystemException;
/**
 * This class (Single tone Class) represent "ConnectionPool" that add/give/get/remove and close the connection whit Data Base (Apache derby).
 * 
 * @author michael
 *
 */
public class ConnectionPool {

	private static ConnectionPool instance = null;
	private static Set<Connection> connections = new HashSet<>();
	private static Set<Connection> UsedConnections = new HashSet<>();
	private static boolean ServerOpen = true;
	private static final int MAX_CONNECTION = 10;
	private static String url = "jdbc:derby://localhost:1527/CouponSystemDB2;";

	/**
	 * The constructor of the class is Private Because its single ton class and you might build the constructor whit "getInstanse".
	 * @throws GeneralCouponSystemException
	 */
	private ConnectionPool() throws GeneralCouponSystemException {
		initialize();
		addConnection();
	}

	/**
	 * Initialize method Made to give the name of the DRIVER to the Connection.
	 * @throws GeneralCouponSystemException
	 */
	private void initialize() throws GeneralCouponSystemException {

		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");

		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException("Driver Name error!");
			throw message;
		}

	}

	 /**
	  * addConnection method add (10 connections) the MAX number of connection that we choose,
	  * to the ConnectionPool(connections) = Collection Of Connection.
	  * and duplicate the (10 connections) to the UseConnections Collection, That help up to close all the connection in one time.
	  * 
	  * @throws GeneralCouponSystemException
	  */
	private void addConnection() throws GeneralCouponSystemException {
		if (connections.size() < MAX_CONNECTION) {
			while (connections.size() < MAX_CONNECTION) {

				try {

					connections.add(DriverManager.getConnection(url));
					UsedConnections.add(DriverManager.getConnection(url));

				} catch (Exception e) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Server Couldn't Load, Please Try Again Later");
					throw message;
				}
			}

		}

	}
/**
 * getInstance is the only Public Method That run the constructor of the class (Single ton class).
 * @return ConnectionPool (Class Constructor).
 * @throws GeneralCouponSystemException
 */
	public static ConnectionPool getInstance() throws GeneralCouponSystemException {
		if (ServerOpen) {

			if (instance == null) {

				instance = new ConnectionPool();
				return instance;
			}
			return instance;
		} else {
			GeneralCouponSystemException message = new GeneralCouponSystemException("DataBase Temporary Closed.");
			throw message;
		}
	}

	/**
	 * the method getConnection is searching for open connection in the collection and give it to the client, if
	 * it no free connection in the ConnectionPool Collection it will wait 60 second and try again. if you get connection it will move the specified connection to
	 * use connection pool.
	 * @return Connection - only whit this connection you can "speak" whit the DATA BASE. 
	 * @throws GeneralCouponSystemException
	 */
	public synchronized Connection getConnections() throws GeneralCouponSystemException {

		Iterator<Connection> conIter = connections.iterator();
		Connection con = null;
		if (conIter.hasNext()) {
			con = conIter.next();

			connections.remove(con);
		} else if (connections.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"No Connection Currently, Try Later.");
				throw message;
			}

		}
		return (con);
	}
/**
 * This returnConnection method get connection that in use and return it into ConnectionPool.
 * and notify the wait of getConnection Method that is a free connection in the ConnectionPool  Collection.
 * @param connection - connection that at use.
 * @throws GeneralCouponSystemException
 */
	public synchronized void returnConnection(Connection connection) throws GeneralCouponSystemException {

		if (connection != null) {
			try {
				connections.add(connection);
				notify();

			} catch (Exception e) {
				GeneralCouponSystemException message = new GeneralCouponSystemException();
				throw message;
			}
		}

	}
/**
 * closeAllConnection Method is void method that close all the connection whit the DATA BASE, free connection and in use connection.
 * @throws GeneralCouponSystemException
 */
	public synchronized void closeAllConnection() throws GeneralCouponSystemException {
		if (UsedConnections.isEmpty() && connections.isEmpty()) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Connection Cant Be Closed, Or already Closed.");
			throw message;
		}
		Iterator<Connection> usIter = UsedConnections.iterator();
		Iterator<Connection> conIter = connections.iterator();

		if (usIter.hasNext() || conIter.hasNext()) {

			connections.clear();
			UsedConnections.clear();
			ServerOpen = false;

		}
	}

}
