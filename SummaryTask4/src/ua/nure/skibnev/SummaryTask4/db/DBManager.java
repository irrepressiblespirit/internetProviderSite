package ua.nure.skibnev.SummaryTask4.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.exception.DBException;
import ua.nure.skibnev.SummaryTask4.exception.Messages;
import ua.nure.skibnev.SummaryTask4.db.Fields;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Tariff;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.db.entity.UserFullInformation;



public class DBManager {
	private static final Logger LOG = Logger.getLogger(DBManager.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static DBManager instance;

	public static synchronized DBManager getInstance() throws DBException {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private DBManager() throws DBException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			// ST4DB - the name of data source
			ds = (DataSource) envContext.lookup("jdbc/st4db");
			//LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			//LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			System.out.println("DBManager#constr: "+ex.getMessage());
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
	}

	private DataSource ds;
	
	public Connection getConnection() throws DBException {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException ex) {
			//LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			System.out.println("DBManager()#getConnection()"+ex.getErrorCode()+ex.getMessage());
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
	}
	// //////////////////////////////////////////////////////////
		// SQL queries
		// //////////////////////////////////////////////////////////

		private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
		private static final String SQL_FIND_USER_BY_LASTNAME="SELECT * from users where fullInformation_id in (SELECT id FROM fullInformation WHERE last_name=?)";
		private static final String SQL_INSERT_INTO_FULLINF="INSERT INTO fullInformation VALUES(DEFAULT,?,?,?,?,?)";
		private static final String SQL_SELECT_ID_FROM_FULLINF="SELECT id FROM fullInformation WHERE telephone=?";
		private static final String SQL_SELECT_STATUSES="SELECT name FROM statuses WHERE id=?";
		private static final String SQL_SELECT_RATES="SELECT name,price FROM rates WHERE id=?";
		private static final String SQL_SELECT_ROLE="SELECT name FROM roles WHERE id=?";
		private static final String SQL_SELECT_ALL="SELECT * from fullInformation";
		private static final String SQL_INSERT_INTO_USERS="INSERT INTO users VALUES(DEFAULT,?,?,?,?,?,?,?)";
		private static final String SQL_SELECT_ALL_USERS="SELECT * FROM users";
		private static final String SQL_SELECT_FULLUSER_FROM_USER="SELECT first_name,last_name,telephone,address,email FROM fullInformation WHERE id=?";
		private static final String SQL_SELECT_USERS_LOGINS="SELECT login FROM users";
		private static final String SQL_UPDATE_USERS_STATUS="UPDATE users SET statuses_id=? WHERE login=?";
		private static final String SQL_SELECT_SERVICES_ID="SELECT * FROM rates_services";
		private static final String SQL_INSERT_RATE="INSERT INTO rates VALUES(default,?,?)";
		private static final String SQL_SELECT_RATE_ID="SELECT id FROM rates WHERE name=?";
		private static final String SQL_INSERT_RATES_SERVICES="INSERT INTO rates_services VALUES(?,?)";
		private static final String SQL_SELECT_RATES_NAME="SELECT name FROM rates";
		private static final String SQL_DELETE_RATE="DELETE FROM rates WHERE id=?";
		private static final String SQL_DELETE_RATE_SERVICES="DELETE FROM rates_services WHERE rates_id=?";
		private static final String SQL_UPDATE_RATES="UPDATE rates SET name=?,price=? WHERE id=?";
		private static final String SQL_UPDATE_RATES_ID="UPDATE users SET rates_id=? WHERE rates_id=?";
		private static final String SQL_SELECT_SERV="SELECT services_id FROM rates_services WHERE rates_id=?";
		private static final String SQL_UPDATE_USERS_COUNT="UPDATE users SET personal_count=? WHERE login=?";
		private static final String SQL_SELECT_USERS_BY_ROLE="SELECT * from users where roles_id=?";
		// //////////////////////////////////////////////////////////
		// DB util methods
		// //////////////////////////////////////////////////////////

		/**
		 * Closes a connection.
		 * 
		 * @param con
		 *            Connection to be closed.
		 */
		private void close(Connection con) {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
				}
			}
		}

		/**
		 * Closes a statement object.
		 */
		private void close(Statement stmt) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
					LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
				}
			}
		}

		/**
		 * Closes a result set object.
		 */
		private void close(ResultSet rs) {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
				}
			}
		}

		/**
		 * Closes resources.
		 */
		private void close(Connection con, Statement stmt, ResultSet rs) {
			close(rs);
			close(stmt);
			close(con);
		}
		/**
		 * Returns a user with the given login.
		 * 
		 * @param login
		 *            User login.
		 * @return User entity.
		 * @throws DBException
		 */
		public User findUserByLogin(String login) throws DBException {
			User user = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection con = null;
			try {
				con = getConnection();
				pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
				pstmt.setString(1, login);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					user = extractUser(rs);
				}
				if(user==null){
					throw new SQLException();
				}
				con.commit();
			} catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
			} finally {
				close(con, pstmt, rs);
			}
			return user;
		}
		
		/**
		 * Rollbacks a connection.
		 * 
		 * @param con
		 *            Connection to be rollbacked.
		 */
		private void rollback(Connection con) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex) {
					LOG.error("Cannot rollback transaction", ex);
				}
			}
		}
		/**
		 * Extracts a user entity from the result set.
		 * 
		 * @param rs
		 *            Result set from which a user entity will be extracted.
		 * @return User entity
		 */
		private User extractUser(ResultSet rs) throws SQLException {
			User user = new User();
			user.setId(rs.getInt(Fields.ENTITY_ID));
			user.setLogin(rs.getString(Fields.USER_LOGIN));
			user.setPassword(rs.getString(Fields.USER_PASSWORD));
			user.setFullInformationId(rs.getInt(Fields.USER_FULLINFORMATION_ID));
			user.setCount(rs.getInt(Fields.USER_COUNT));
			user.setStatusesId(rs.getInt(Fields.USER_STATUSES_ID));
			user.setRatesId(rs.getInt(Fields.USER_RATES_ID));
			user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
			return user;
		}
		public void addUserFullInf(UserFullInformation ufi) throws DBException{
			Connection con = null;
			PreparedStatement pstmt = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_INSERT_INTO_FULLINF);
				pstmt.setString(1, ufi.getFirstName());
				pstmt.setString(2, ufi.getLastName());
				pstmt.setString(3, ufi.getTelephone());
				pstmt.setString(4, ufi.getAddress());
				pstmt.setString(5, ufi.getEmail());
				int row=pstmt.executeUpdate();
				LOG.debug("Insert in row ==>"+row);
			}catch(SQLException e){
				rollback(con);
				LOG.error("error in method addUserFullInf() "+e.getErrorCode()+" "+e.getMessage());
				throw new DBException();
			}finally{
				close(con);
				close(pstmt);
			}
			
		}
		public void addUser(User user) throws DBException{
			Connection con = null;
			PreparedStatement pstmt = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_INSERT_INTO_USERS);
				pstmt.setString(1, user.getLogin());
				pstmt.setString(2, user.getPassword());
				pstmt.setInt(3,user.getFullInformationId());
				pstmt.setInt(4, user.getCount());
				pstmt.setInt(5, user.getStatusesId());
				pstmt.setInt(6, user.getRatesId());
				pstmt.setInt(7, user.getRoleId());
				int row=pstmt.executeUpdate();
				LOG.debug("Insert in row ==>"+row);
			}catch(SQLException e){
				rollback(con);
				LOG.error("error in method addUserFullInf() "+e.getErrorCode()+" "+e.getMessage());
				throw new DBException();
			}finally{
				close(con);
				close(pstmt);
			}
		}
		public int getIdFromFullInf(UserFullInformation ufi) throws DBException{
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			int id=0;
			try{
				con=getConnection();
				ps=con.prepareStatement(SQL_SELECT_ID_FROM_FULLINF);
				ps.setString(1, ufi.getTelephone());
				rs=ps.executeQuery();
				if(rs.next()){
					id=rs.getInt(1);
				}
				if(id==0){
					throw new SQLException();
				}
			}catch(SQLException e){
				rollback(con);
				LOG.error("error in method getId"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_NOT_FIND_ID,e);
			}finally{
				close(con, ps, rs);
			}
			return id;
		}
		public List<UserFullInformation> selectAllUserFullInf() throws DBException{
			Connection con=null;
			Statement st=null;
			ResultSet rs=null;
			UserFullInformation fuser=null;
			List<UserFullInformation> list=new LinkedList<>();
			try{
				con=getConnection();
				st=con.createStatement();
				rs=st.executeQuery(SQL_SELECT_ALL);
				while(rs.next()){
					fuser=new UserFullInformation();
					fuser.setFirstName(rs.getString(2));
					fuser.setLastName(rs.getString(3));
					fuser.setTelephone(rs.getString(4));
					fuser.setAddress(rs.getString(5));
					fuser.setEmail(rs.getString(6));
					LOG.debug("UserFullInformation ==>"+fuser.toString());
					list.add(fuser);
					LOG.debug("UserFullInformation in list ==>"+list.toString());
				}
				if(list.size()==0){
					throw new SQLException(); 
				}
				con.commit();
			}catch(SQLException e){
				rollback(con);
				LOG.error("error in method getId"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB,e);
			}finally{
				close(con, st, rs);
			}
			LOG.debug("List of fuser after return from method ==>"+list);
			return list;
		}
		public List<User> selectAllFromUser() throws DBException{
			Connection con=null;
			Statement st=null;
			ResultSet rs=null;
			User user=null;
			List<User> list=new LinkedList<User>();
			try{
				con=getConnection();
				st=con.createStatement();
				rs=st.executeQuery(SQL_SELECT_ALL_USERS);
				while(rs.next()){
					user=new User();
					user.setId(rs.getInt(1));
					user.setLogin(rs.getString(2));
					user.setPassword(rs.getString(3));
					user.setFullInformationId(rs.getInt(4));
					user.setCount(rs.getInt(5));
					user.setStatusesId(rs.getInt(6));
					user.setRatesId(rs.getInt(7));
					user.setRoleId(rs.getInt(8));
					LOG.debug("User ==>"+user.toString());
					list.add(user);
					LOG.debug("User in list ==>"+list.toString());
				}
				if(list.size()==0){
					throw new SQLException(); 
				}
			}catch(SQLException e){
				rollback(con);
				LOG.error("error in method getId"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB,e);
			}finally{
				close(con, st, rs);
			}
			LOG.debug("List of user after return from method ==>"+list);
			return list;
		}
		public FullUser getFullUserStatus(User user,UserFullInformation ufi) throws DBException{
			FullUser fuser=new FullUser();
			Connection con=null;
			PreparedStatement pr=null;
			ResultSet set=null;
			
			try{
			fuser.setLogin(user.getLogin());
			fuser.setPassword(user.getPassword());
			fuser.setFirstName(ufi.getFirstName());
			fuser.setLastName(ufi.getLastName());
			fuser.setTelephone(ufi.getTelephone());
			fuser.setAddress(ufi.getAddress());
			fuser.setEmail(ufi.getEmail());
			fuser.setCount(user.getCount());
			con=getConnection();
			pr=con.prepareStatement(SQL_SELECT_STATUSES);
			pr.setInt(1, user.getStatusesId());
			set=pr.executeQuery();
			set.next();
			fuser.setStatus(set.getString(1));
			LOG.debug("status in fuser: "+fuser.getStatus());
			if(fuser.getStatus().equals("")){
				throw new SQLException();
			}
			}catch(SQLException e){
				rollback(con);
				LOG.error("In method getFullUserSatatus==>"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB,e);
			}finally{
				
				close(con, pr, set);
			}
			return fuser;
		}
		public FullUser getFullUserRate(User user,FullUser fuser) throws DBException{
			Connection con=null;
			PreparedStatement p=null;
			ResultSet s=null;
			try{
				con=getConnection();
				p=con.prepareStatement(SQL_SELECT_RATES);
				p.setInt(1, user.getRatesId());
				s=p.executeQuery();
				StringBuilder sb=new StringBuilder();
				while(s.next()){
					sb.append(s.getString(1)+" price: ");
					sb.append(s.getInt(2));
				}
				fuser.setRates(sb.toString());
				LOG.debug("rate in fuser: "+fuser.getRates());
				if(fuser.getRates().equals("")){
					throw new SQLException();
				}
			}catch(SQLException e){
				rollback(con);
				LOG.error("In method getFullUserRate==>"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB,e);
			}finally{
				
				close(con, p, s);
			}
			return fuser;
		}
		public FullUser getFullUserRole(User user,FullUser fuser) throws DBException{
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try{
				con=getConnection();
				ps=con.prepareStatement(SQL_SELECT_ROLE);
				ps.setInt(1, user.getRoleId());
				rs=ps.executeQuery();
				rs.next();
				fuser.setRole(rs.getString(1));
				LOG.debug("role in fuser: "+fuser.getRole());
				if(fuser.getRole().equals("")){
					throw new SQLException();
				}
			}catch(SQLException e){
				rollback(con);
				LOG.error("In method getFullUserRole==>"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB,e);
			}finally{
				
				close(con, ps, rs);
			}
			return fuser;
		}
		public UserFullInformation getFullUserFromUser(User user) throws DBException{
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			UserFullInformation fuser=new UserFullInformation();
			try{
				con=getConnection();
				ps=con.prepareStatement(SQL_SELECT_FULLUSER_FROM_USER);
				ps.setInt(1,user.getFullInformationId());
				rs=ps.executeQuery();
				while(rs.next()){
					fuser.setFirstName(rs.getString(1));
					fuser.setLastName(rs.getString(2));
					fuser.setTelephone(rs.getString(3));
					fuser.setAddress(rs.getString(4));
					fuser.setEmail(rs.getString(5));
				}
				
			}catch(SQLException e){
				rollback(con);
				LOG.error("In method getFullUserRole==>"+e.getErrorCode()+" "+e.getMessage());
				throw new DBException();
			}finally{
				
				close(con, ps, rs);
			}
			return fuser;
		}
		public List<User> findUserByLastName(String lastName) throws DBException{
			List<User> list=new LinkedList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection con = null;
			try {
				con = getConnection();
				pstmt = con.prepareStatement(SQL_FIND_USER_BY_LASTNAME);
				pstmt.setString(1, lastName);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					list.add(extractUser(rs));
				}
				if(list.size()==0){
					throw new SQLException();
				}
				con.commit();
			} catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LASTNAME, ex);
			} finally {
				close(con, pstmt, rs);
			}
			return list;
		}
		public List<String> getUsersLogins() throws DBException{
			Statement stm=null;
			ResultSet rs = null;
			Connection con = null;
			List<String> list=new LinkedList<>();
			try{
				con = getConnection();
				stm=con.createStatement();
				rs=stm.executeQuery(SQL_SELECT_USERS_LOGINS);
				while(rs.next()){
					list.add(rs.getString(1));
				}
				if(list.size()==0){
					throw new SQLException();
				}
				con.commit();
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, stm, rs);
			}
			return list;
		}
		public void setUserStatus(User user,int status) throws DBException{
			PreparedStatement pstmt = null;
			Connection con = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_UPDATE_USERS_STATUS);
				pstmt.setInt(1,status);
				pstmt.setString(2, user.getLogin());
				pstmt.executeUpdate();
				con.commit();
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LASTNAME, ex);
			} finally {
				close(con);
				close(pstmt);
			}
		}
		public List<Integer> getRateId() throws DBException{
			Statement stm=null;
			ResultSet rs = null;
			Connection con = null;
			List<Integer> list=new ArrayList<>();
			try{
				con=getConnection();
				stm=con.createStatement();
				rs=stm.executeQuery(SQL_SELECT_SERVICES_ID);
				while(rs.next()){
					int id=rs.getInt(1);
					LOG.debug("Service name ==>"+id);
					list.add(id);
				}
				if(list.size()==0){
					throw new SQLException();
				}
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, stm, rs);
			}
			return list;
		}
		public List<Integer> getServicesId() throws DBException{
			Statement stm=null;
			ResultSet rs = null;
			Connection con = null;
			List<Integer> list=new ArrayList<>();
			try{
				con=getConnection();
				stm=con.createStatement();
				rs=stm.executeQuery(SQL_SELECT_SERVICES_ID);
				while(rs.next()){
					int id=rs.getInt(2);
					LOG.debug("Service name ==>"+id);
					list.add(id);
				}
				if(list.size()==0){
					throw new SQLException();
				}
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, stm, rs);
			}
			return list;
		}
		public void insertNewRate(Tariff tariff) throws DBException{
			PreparedStatement pstmt = null;
			Connection con = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_INSERT_RATE);
				pstmt.setString(1, tariff.getName());
				pstmt.setInt(2, tariff.getPrice());
				pstmt.executeUpdate();
				LOG.debug("New rate in a table");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(pstmt);
				close(con);
			}
		}
		public int getRateId(Tariff tariff) throws DBException{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection con = null;
			int id=0;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_SELECT_RATE_ID);
				pstmt.setString(1, tariff.getName());
				rs=pstmt.executeQuery();
				if(rs.next()){
					id=rs.getInt(1);
				}
				LOG.debug("New rate id ==>"+id);
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con,pstmt,rs);
			}
			return id;
		}
		public void setRatesAndServices(int r_id,int serv_id) throws DBException{
			PreparedStatement pstmt = null;
			Connection con = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_INSERT_RATES_SERVICES);
				pstmt.setInt(1, r_id);
				pstmt.setInt(2, serv_id);
				pstmt.executeUpdate();
				LOG.debug("Set datas in table rates_services by id "+r_id+" services id "+serv_id);
				con.commit();
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(pstmt);
				close(con);
			}
		}
		public List<String> selectRatesName() throws DBException{
			Statement st = null;
			Connection con = null;
			ResultSet rs=null;
			List<String> list=new ArrayList<>();
			try{
				con=getConnection();
				st=con.createStatement();
				rs=st.executeQuery(SQL_SELECT_RATES_NAME);
				while(rs.next()){
					list.add(rs.getString(1));
				}
				if(list.size()==0){
					throw new SQLException();
				}
				LOG.debug("Set datas in table rates_services");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con,st,rs);
			}
			return list;
		}
		public void deleteTariff(int id) throws DBException{
			PreparedStatement pstmt = null;
			Connection con = null;
			try{
				LOG.debug("In delete tariff method");
				con=getConnection();
				pstmt=con.prepareStatement(SQL_DELETE_RATE);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				LOG.debug("after delete tariff method");
				con.commit();
			}catch (SQLException ex) {
				rollback(con);
				LOG.error(ex.getMessage());
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(pstmt);
				close(con);
			}
		}
		public void deleteTariffAndServices(int id) throws DBException{
			PreparedStatement pstmt = null;
			Connection con = null;
			try{
				con=getConnection();
				pstmt=con.prepareStatement(SQL_DELETE_RATE_SERVICES);
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				LOG.debug("Set datas in table rates_services");
				con.commit();
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(pstmt);
				close(con);
			}
		}
		public void updateRates(int id,Tariff tariff) throws DBException{
			PreparedStatement st = null;
			Connection con = null;
			try{
				con=getConnection();
				st=con.prepareStatement(SQL_UPDATE_RATES);
				st.setString(1, tariff.getName());
				st.setInt(2, tariff.getPrice());
				st.setInt(3, id);
				st.execute();
				LOG.debug("Set datas in table rates_services name "+tariff.getName()+" price "+tariff.getPrice());
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(st);
				close(con);
			}
		}
		
		public void updateRatesIdInUsers(int oldId,int newId) throws DBException{
			PreparedStatement st = null;
			Connection con = null;
			try{
				con=getConnection();
				st=con.prepareStatement(SQL_UPDATE_RATES_ID);
				st.setInt(1, newId);
				st.setInt(2, oldId);
				st.execute();
				LOG.debug("Update datas in table users by id ");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(st);
				close(con);
			}
		}
		
		public List<Integer> selectServId(int rates_id) throws DBException{
			PreparedStatement st = null;
			Connection con = null;
			ResultSet rs=null;
			List<Integer> list=new ArrayList<>();
			try{
				con=getConnection();
				st=con.prepareStatement(SQL_SELECT_SERV);
				st.setInt(1, rates_id);
				rs=st.executeQuery();
				while(rs.next()){
					list.add(rs.getInt(1));
				}
				if(list.size()==0){
					throw new SQLException();
				}
				LOG.debug("Set datas in table rates_services");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, st, rs);
			}
			return list;
		}
		public void updateUserCount(int count,String login) throws DBException{
			PreparedStatement st = null;
			Connection con = null;
			try{
				con=getConnection();
				st=con.prepareStatement(SQL_UPDATE_USERS_COUNT);
				st.setInt(1,count);
				st.setString(2,login);
				st.executeUpdate();
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(st);
				close(con);
			}
		}
		public List<User> getUsersByRole(int role) throws DBException{
			PreparedStatement st = null;
			Connection con = null;
			ResultSet rs=null;
			User user=null;
			List<User> list=new ArrayList<>();
			try{
				con=getConnection();
				st=con.prepareStatement(SQL_SELECT_USERS_BY_ROLE);
				st.setInt(1, role);
				rs=st.executeQuery();
				while(rs.next()){
					list.add(extractUser(rs));
				}
				if(list.size()==0){
					throw new SQLException();
				}
				LOG.debug("Set datas in table rates_services");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, st, rs);
			}
			return list;
		}
		public List<User> joinByPrice() throws DBException{
			Statement st = null;
			Connection con = null;
			ResultSet rs=null;
			User user=null;
			List<User> list=new ArrayList<>();
			try{
				con=getConnection();
				st=con.createStatement();
				rs=st.executeQuery("SELECT us.login, rat.price FORM users us,rates rat WHERE us.rates_id=rat.id ORDER BY rat.price DESC");
				while(rs.next()){
					user=new User();
					user.setLogin(rs.getString(1));
					user.setRatesId(rs.getInt(2));
					list.add(user);
				}
				if(list.size()==0){
					throw new SQLException();
				}
				LOG.debug("Set datas in table rates_services");
			}catch (SQLException ex) {
				rollback(con);
				throw new DBException(Messages.ERR_CANNOT_FIND_DATAS_IN_DB, ex);
			} finally {
				close(con, st, rs);
			}
			return list;
		}
}
