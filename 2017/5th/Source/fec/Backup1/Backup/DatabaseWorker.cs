using System.Xml;

namespace fec {

    public static class DatabaseWorker {

        public static void AddEmployee (string employeeName, string job, string address, string phone, bool fast = false) {
            XmlElement employeeElement = fec_Main.xmlDoc.CreateElement("Employee");
            fec_Main.employeeTable.AppendChild(employeeElement);

            XmlElement nameElement = fec_Main.xmlDoc.CreateElement("Name");
            nameElement.InnerText = employeeName;
            employeeElement.AppendChild(nameElement);

            XmlElement jobElement = fec_Main.xmlDoc.CreateElement("Job");
            jobElement.InnerText = job;
            employeeElement.AppendChild(jobElement);

            XmlElement addressElement = fec_Main.xmlDoc.CreateElement("Address");
            addressElement.InnerText = address;
            employeeElement.AppendChild(addressElement);

            XmlElement phoneElement = fec_Main.xmlDoc.CreateElement("Phone");
            phoneElement.InnerText = phone;
            employeeElement.AppendChild(phoneElement);

            if (!fast) {
                fec_Main.xmlDoc.Save("fec.xmldb");
            }
        }

        public static void RemoveEmployee (string employeeName) {
            XmlNode employeeToRemove = FindEmployeeNode(employeeName);

            employeeToRemove.ParentNode.RemoveChild(employeeToRemove);

            fec_Main.xmlDoc.Save("fec.xmldb");
        }

        public static XmlNode FindEmployeeNode(string employeeName) {
            XmlNodeList nodeList = fec_Main.xmlDoc.SelectNodes("Employees/Employee");

            foreach (XmlNode node in nodeList) {
                if (node["Name"].InnerText == employeeName) {
                    return node;
                }
            }
            return null;
        }
    }
}
