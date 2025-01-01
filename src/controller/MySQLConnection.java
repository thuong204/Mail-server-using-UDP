package controller;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;

	public class MySQLConnection {
	    public static void main(String[] args) {
	        // Thông tin kết nối
	        String url = "jdbc:mysql://localhost:3306/testdb"; // Đường dẫn đến cơ sở dữ liệu
	        String user = "root"; // Tài khoản MySQL (mặc định của XAMPP là 'root')
	        String password = ""; // Mật khẩu (mặc định là rỗng nếu bạn không đặt mật khẩu)

	        try {
	            // Tải driver MySQL
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            // Tạo kết nối
	            Connection connection = DriverManager.getConnection(url, user, password);

	            if (connection != null) {
	                System.out.println("Kết nối thành công!");
	            }

	        } catch (ClassNotFoundException e) {
	            System.out.println("Không tìm thấy Driver JDBC MySQL!");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            System.out.println("Kết nối thất bại!");
	            e.printStackTrace();
	        }
	    }


}
