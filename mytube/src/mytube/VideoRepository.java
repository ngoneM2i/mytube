package mytube;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VideoRepository {

	private Video mapResultSetToVideo(ResultSet rs) throws SQLException {
		Video v = new Video();
		v.setId(rs.getInt("id")); // ou rs.getInt(1)
		v.setTitle(rs.getString("title")); // ou rs.getInt(2)
		v.setSource(rs.getString("source"));
		v.setDescription(rs.getString("description"));
		v.setComment(rs.getString("comment"));
		v.setDatePub(rs.getDate("date"));
		v.setNbLikes(rs.getInt("nblikes"));
		v.setNbDisLikes(rs.getInt("nbDisLikes"));
		v.setNbVues(rs.getInt("nbVues"));
		
		return v;
	 }
	
	public List<Video> findAllVideos() {
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytube?serverTimezone=UTC", "root", "rootroot")) {
			
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery("SELECT * FROM video");
			
			List<Video> videos = new ArrayList<>();
			
			while(rs.next()) {			
				Video v = mapResultSetToVideo(rs);
				videos.add(v);
			}
			
	 return videos;
			
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Fetch a user by its id.
	 * @param id the user id
	 * @return the user corresponding to the id or null if no user was found
	 * @throws RuntimeException if a problem occurs.
	 */
	public Video findVideoById(int id) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mystube?serverTimezone=UTC", "root", "rootroot")) {

			PreparedStatement pstmt = 
					conn.prepareStatement("SELECT * FROM video WHERE id = ?");
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if(! rs.next()) { 
				return null; 
			}
			
			return mapResultSetToVideo(rs);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	List<Video> findTrending() {
	List<Video> videos = findAllVideos();
    List<Video> trending = new ArrayList<>();
     for (Video v : videos) {
    	 if(v.getNbVues() == 10)
    		 trending.add(v);
     }
     return trending;
    }
       

	 List<Video> findRecommended() {
		 List<Video> videos = findAllVideos();
		 List<Video> recommended = new ArrayList<>();
	     for (Video v : videos) {
	    	 if(v.getNbLikes()== 10 && v == findTrending())
	    		 recommended.add(v);
	     }
	     return recommended;
	     }
	
	}
	
	


