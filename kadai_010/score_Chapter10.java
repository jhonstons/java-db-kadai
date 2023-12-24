package kadai_010;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Score_Chapter10 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;


        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/Challenge_java",
                "root",
                "blackpigs"
            );

            System.out.println("データベース接続成功" + con.toString());


            // 生徒IDが5の数学と英語の点数を更新するSQL文
            String updateSql = "UPDATE score SET score_math = ?, score_english = ? WHERE id = ?";

            statement = con.prepareStatement(updateSql);

                statement.setInt(1, 95); // 数学の点数
                statement.setInt(2, 80); // 英語の点数
                statement.setInt(3, 5); // 生徒ID

                // SQLクエリを実行（DBMSに送信）
                System.out.println("レコード更新を実行します:");
                int rowsAffected = statement.executeUpdate();

                System.out.println(rowsAffected + "件のレコードが更新されました");

            // 数学・英語の点数が高い順に生徒情報を取得するSQL文
            String selectSql = "SELECT * FROM score ORDER BY score_math DESC, score_english DESC";
            statement = con.prepareStatement(selectSql);

            // SQLクエリを実行して結果を取得
            resultSet = statement.executeQuery();

            int count = 1;
            while(resultSet.next()) {
              System.out.println(count + "件目:" + "生徒ID=" + resultSet.getInt("id") +
                  "／氏名=" + resultSet.getString("name") +
                  "／数学=" + resultSet.getInt("score_math") +
                  "／英語=" + resultSet.getInt("score_english"));
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
