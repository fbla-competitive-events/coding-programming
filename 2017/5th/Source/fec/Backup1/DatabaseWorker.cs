using System;
using System.Data.SQLite;

namespace fec {

    public static class DatabaseWorker {

        // Used for data sharing
        public static int tempId;

        #region Employee Methods
        /// <summary>
        /// Add a new employee entry to the database.
        /// </summary>
        /// <param name="employeeName"> The name of the employee. </param>
        /// <param name="job"> The job of the employee. </param>
        /// <param name="address"> The address of the employee. </param>
        /// <param name="phone"> The phone of the employee </param>
        /// <param name="workDays"> The days the employee works in the FEC. </param>
        public static void AddEmployee(int employeeId, string employeeName, string job, string address, string phone, string[] schedule) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@job", job);
            command.Parameters.AddWithValue("@address", address);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            AddEmployeeSchedule(employeeId, employeeName, schedule);
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit an employee entry in the database.
        /// </summary>
        /// <param name="id"> The id of the employee entry to edit. </param>
        /// <param name="employeeName"> The new name of the employee. </param>
        /// <param name="job"> The new job of the employee. </param>
        /// <param name="address"> The new address of the employee. </param>
        /// <param name="phone"> The new phone of the employee. </param>
        /// <param name="workDays"> The new work days of the employee. </param>
        public static void EditEmployee(int id, string employeeName, string job, string address, string phone) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@job", job);
            command.Parameters.AddWithValue("@address", address);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            EditEmployeeNameSchedule(id, employeeName);
            Queries.connection.Close();
        }

        /// <summary>
        /// Remove the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee to remove. </param>
        public static void RemoveEmployee(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            RemoveEmployeeSchedule(id);
            Queries.connection.Close();
        }

        /// <summary>
        /// Get the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get data for. </param>
        /// <returns> The data of the employee entry. </returns>
        public static string[] GetEmployeeEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[5];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[0].ToString();
                    values[1] = reader[1].ToString();
                    values[2] = reader[2].ToString();
                    values[3] = reader[3].ToString();
                    values[4] = reader[4].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }

        /// <summary>
        /// Get the employee entry (without the id) with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get data for. </param>
        /// <returns> The data of the employee entry (without the id). </returns>
        public static string[] GetEmployeeEntryNoId(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[4];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[1].ToString();
                    values[1] = reader[2].ToString();
                    values[2] = reader[3].ToString();
                    values[3] = reader[4].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }

        /// <summary>
        /// Check if an employee or customer with the specified id exists in the database.
        /// </summary>
        /// <param name="id"> The id of the employee or customer to search for. </param>
        /// <returns> Whether an entry with the specified id exists. </returns>
        public static bool IdExists(int id) {
            SQLiteCommand commandEmployee = new SQLiteCommand();
            SQLiteCommand commandCustomer = new SQLiteCommand();

            commandEmployee.CommandType = System.Data.CommandType.Text;
            commandEmployee.CommandText = Queries.getEmployeeQuery;
            commandEmployee.Connection = Queries.connection;

            commandCustomer.CommandType = System.Data.CommandType.Text;
            commandCustomer.CommandText = Queries.getCustomerQuery;
            commandCustomer.Connection = Queries.connection;

            commandEmployee.Parameters.AddWithValue("@id", id);
            commandCustomer.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            try {
                commandEmployee.ExecuteNonQuery();
                commandCustomer.ExecuteNonQuery();
            } catch (Exception) {
                Queries.connection.Close();
                return true;
            }
            Queries.connection.Close();
            return false;
        }
        #endregion

        #region Employee Schedule Methods
        /// <summary>
        /// Add a new employee schedule entry to the database together with an employee entry.
        /// This method is meant to be called from the AddEmployee method.
        /// </summary>
        /// <param name="employeeId"> The id of the employee entry. </param>
        /// <param name="employeeName"> The name of the employee for whom this schedule is for. </param>
        /// <param name="scheduleValues"> The values of the schedule. </param>
        private static void AddEmployeeSchedule(int employeeId, string employeeName, string[] scheduleValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@monday", scheduleValues[0]);
            command.Parameters.AddWithValue("@tuesday", scheduleValues[1]);
            command.Parameters.AddWithValue("@wednesday", scheduleValues[2]);
            command.Parameters.AddWithValue("@thursday", scheduleValues[3]);
            command.Parameters.AddWithValue("@friday", scheduleValues[4]);
            command.Parameters.AddWithValue("@saturday", scheduleValues[5]);
            command.Parameters.AddWithValue("@sunday", scheduleValues[6]);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Edit an employee schedule entry in the database.
        /// </summary>
        /// <param name="employeeId"> The id of the employee whose schedule is to be changed. </param>
        /// <param name="scheduleValues"> The new schedule values. </param>
        public static void EditEmployeeSchedule(int employeeId, string[] scheduleValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@monday", scheduleValues[0]);
            command.Parameters.AddWithValue("@tuesday", scheduleValues[1]);
            command.Parameters.AddWithValue("@wednesday", scheduleValues[2]);
            command.Parameters.AddWithValue("@thursday", scheduleValues[3]);
            command.Parameters.AddWithValue("@friday", scheduleValues[4]);
            command.Parameters.AddWithValue("@saturday", scheduleValues[5]);
            command.Parameters.AddWithValue("@sunday", scheduleValues[6]);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit the name of the employee a schedule is registered to.
        /// *** This method is meath to be called from the AddEmployee method. ***
        /// </summary>
        /// <param name="id"> The id of the employee schedule to edit. </param>
        /// <param name="employeeName"> The new name assigned to the schedule. </param>
        private static void EditEmployeeNameSchedule(int id, string employeeName) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeScheduleNameQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", employeeName);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Remove the employee schedule of the employee with the specified id.
        /// *** This method is meath to be called from the AddEmployee method. ***
        /// </summary>
        /// <param name="id"> The id of the employe to remove. </param>
        private static void RemoveEmployeeSchedule(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.removeEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Get the schedule data of the employee with the specified id.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get the schedule data. </param>
        /// <returns> The schedule data of the employee. </returns>
        public static string[] GetEmployeeScheduleEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[9];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    for (int i = 0;i < 9;i++) {
                        values[i] = reader[i].ToString();
                    }
                }
            }
            Queries.connection.Close();

            return values;
        }
        #endregion

        #region Customer Methods
        /// <summary>
        /// Add a new employee entry to the database.
        /// </summary>
        /// <param name="employeeName"> The name of the employee. </param>
        /// <param name="job"> The job of the employee. </param>
        /// <param name="address"> The address of the employee. </param>
        /// <param name="phone"> The phone of the employee </param>
        /// <param name="workDays"> The days the employee works in the FEC. </param>
        public static void AddCustomer(int customerId, string customerName, string membership, string phone, string[] attedance) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", customerId);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@membership", membership);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            AddCustomerAttendance(customerId, customerName, attedance);
            Queries.connection.Close();
        }

        public static void EditCustomer(int id, string customerName, string membership, string phone) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@membership", membership);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            EditCustomerAttendanceName(id, customerName);
            Queries.connection.Close();
        }

        /// <summary>
        /// Get the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get data for. </param>
        /// <returns> The data of the employee entry. </returns>
        public static string[] GetCustomerEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[4];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[0].ToString();
                    values[1] = reader[1].ToString();
                    values[2] = reader[2].ToString();
                    values[3] = reader[3].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }

        public static string[] GetCustomerEntryNoId(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[3];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[1].ToString();
                    values[1] = reader[2].ToString();
                    values[2] = reader[3].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }


        /// <summary>
        /// Remove the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee to remove. </param>
        public static void RemoveCustomer(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            RemoveCustomerAttendance(id);
            Queries.connection.Close();
        }

        /// <summary>
        /// Remove the employee schedule of the employee with the specified id.
        /// *** This method is meath to be called from the AddEmployee method. ***
        /// </summary>
        /// <param name="id"> The id of the employe to remove. </param>
        private static void RemoveCustomerAttendance(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleleCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Add a new employee schedule entry to the database together with an employee entry.
        /// This method is meant to be called from the AddEmployee method.
        /// </summary>
        /// <param name="employeeId"> The id of the employee entry. </param>
        /// <param name="employeeName"> The name of the employee for whom this schedule is for. </param>
        /// <param name="scheduleValues"> The values of the schedule. </param>
        private static void AddCustomerAttendance(int customerId, string customerName, string[] attendanceValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", customerId);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@monday", attendanceValues[0]);
            command.Parameters.AddWithValue("@tuesday", attendanceValues[1]);
            command.Parameters.AddWithValue("@wednesday", attendanceValues[2]);
            command.Parameters.AddWithValue("@thursday", attendanceValues[3]);
            command.Parameters.AddWithValue("@friday", attendanceValues[4]);
            command.Parameters.AddWithValue("@saturday", attendanceValues[5]);
            command.Parameters.AddWithValue("@sunday", attendanceValues[6]);

            command.ExecuteNonQuery();
        }

        public static void EditCustomerAttendance(int id, string[] attendanceValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@monday", attendanceValues[0]);
            command.Parameters.AddWithValue("@tuesday", attendanceValues[1]);
            command.Parameters.AddWithValue("@wednesday", attendanceValues[2]);
            command.Parameters.AddWithValue("@thursday", attendanceValues[3]);
            command.Parameters.AddWithValue("@friday", attendanceValues[4]);
            command.Parameters.AddWithValue("@saturday", attendanceValues[5]);
            command.Parameters.AddWithValue("@sunday", attendanceValues[6]);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }

        private static void EditCustomerAttendanceName(int id, string customerName) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerAttendanceNameQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", customerName);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Get the schedule data of the employee with the specified id.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get the schedule data. </param>
        /// <returns> The schedule data of the employee. </returns>
        public static string[] GetCustomerAttendanceEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[9];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    for (int i = 0;i < 9;i++) {
                        values[i] = reader[i].ToString();
                    }
                }
            }
            Queries.connection.Close();

            return values;
        }
        #endregion

        public static void CompactDatabase() {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.vaccumQuery;
            command.Connection = Queries.connection;

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }
    }
}
