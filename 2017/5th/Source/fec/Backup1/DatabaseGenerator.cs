using System.Data.SQLite;

namespace fec {
    public class DatabaseGenerator {

        public const string DATABASE_NAME = "FEC_DATABASE.db";

        /// <summary>
        /// Create a database and tables.
        /// </summary>
        public static void GenerateDB() {
            SQLiteConnection.CreateFile(DATABASE_NAME);

            string employeeTableCreationQuery = "CREATE TABLE Employees (Id INTEGER PRIMARY KEY, Name TEXT, Job TEXT, Address TEXT, Phone BIGINT)";

            string employeeScheduleTableCreationQuery = "CREATE TABLE [Employee Schedule] (Id INTEGER PRIMARY KEY, Name TEXT, "
                                                        + "Monday TEXT, Tuesday TEXT, Wednesday TEXT, "
                                                        + "Thursday TEXT, Friday TEXT, Saturday TEXT, "
                                                        + "Sunday TEXT)";


            string customerTableCreationQuery = "CREATE TABLE Customers (Id INTEGER PRIMARY KEY, Name TEXT, Membership TEXT, Phone BIGINT)";

            string customerAttendanceTableCreationQuery = "CREATE TABLE [Customer Attendance] (Id INTEGER PRIMARY KEY, Name TEXT, "
                                                        + "Monday TEXT, Tuesday TEXT, Wednesday TEXT, "
                                                        + "Thursday TEXT, Friday TEXT, Saturday TEXT, "
                                                        + "Sunday TEXT)";
        
            SQLiteCommand employeeTableCreationCommand = new SQLiteCommand(employeeTableCreationQuery, Queries.connection);
            SQLiteCommand employeeScheduleTableCreationCommand = new SQLiteCommand(employeeScheduleTableCreationQuery, Queries.connection);
            SQLiteCommand customerTableCreationCommand = new SQLiteCommand(customerTableCreationQuery, Queries.connection);
            SQLiteCommand customerAttendanceTableCreationCommand = new SQLiteCommand(customerAttendanceTableCreationQuery, Queries.connection);

            Queries.connection.Open();
            employeeTableCreationCommand.ExecuteNonQuery();
            employeeScheduleTableCreationCommand.ExecuteNonQuery();
            customerTableCreationCommand.ExecuteNonQuery();
            customerAttendanceTableCreationCommand.ExecuteNonQuery();
            Queries.connection.Close();
        }
    }
}
