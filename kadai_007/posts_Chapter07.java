package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // ユーザーリスト
        String[][] Posts = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13"},
            { "1002", "2023-02-08", "お疲れ様です！", "12"},
            { "1003", "2023-02-08", "今日も頑張ります！", "18"},
            { "1001", "2023-02-08", "無理は禁物ですよ！", "17"},
            { "1002", "2023-02-08", "明日から連休ですね！", "20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/Challenge_java",
                "root",
                "blackpigs"
            );

            System.out.println("データベース接続成功:" + con.toString());

            // SQLクエリを準備
            String sql = "INSERT INTO posts(user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?)";
            statement = con.prepareStatement(sql);

            int recordsAdded = 0;

            // レコード追加
            for(String[] row : Posts) {
                statement.setString(1, row[0]); // user_id
                statement.setString(2, row[1]); // posted_at
                statement.setString(3, row[2]); // post_content
                statement.setString(4, row[3]); // likes

                // SQLクエリを実行（DBMSに送信）
                System.out.println("レコード追加を実行します");
                int rowsAffected = statement.executeUpdate();
                recordsAdded += rowsAffected;

            }

            System.out.println(recordsAdded + "件のレコードが追加されました");

            // ユーザーIDが1002のレコードを検索
            String searchQuery = "SELECT * FROM posts WHERE user_id = ?";
            PreparedStatement searchStatement = con.prepareStatement(searchQuery);

            // 検索対象ユーザーのID
            String targetUserID = "1002";
            searchStatement.setString(1, targetUserID);

            // SQLクエリを実行
            System.out.println("全てのレコードを検索しました");
            resultSet = searchStatement.executeQuery(); // resultSetに検索結果を代入

            int count = 1;
            while(resultSet.next()) {
                System.out.println(count + "件目：投稿日時=" + resultSet.getString("posted_at") +
                        "／投稿内容=" + resultSet.getString("post_content") +
                        "／いいね数=" + resultSet.getString("likes"));
                count++;
            }

        } catch (SQLException e) {
            System.out.println("データベースへの接続時にエラーが発生しました。");
            e.printStackTrace();
        } finally {
            // 必要に応じて、リソースの解放を行う（ConnectionやPreparedStatementなど）
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
