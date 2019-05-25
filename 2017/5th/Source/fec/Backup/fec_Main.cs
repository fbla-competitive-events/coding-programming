using Syncfusion.Grouping;
using Syncfusion.Windows.Forms;
using Syncfusion.Windows.Forms.Grid;
using System;
using System.Data;
using System.IO;
using System.Xml;

namespace fec {

    public partial class fec_Main : MetroForm {

        public static XmlDocument xmlDoc = new XmlDocument();
        public static XmlNode employeeTable;

        DataSet XmlData = new DataSet();

        bool needsFirstSetup = false;

        string temp;

        public fec_Main() {
            InitializeComponent();

            employeeGrid.TableOptions.AllowSelection = GridSelectionFlags.AlphaBlend | GridSelectionFlags.Row | GridSelectionFlags.Multiple;
            employeeGrid.TopLevelGroupOptions.ShowFilterBar = true;
            employeeGrid.ChildGroupOptions.ShowFilterBar = true;
            employeeGrid.NestedTableGroupOptions.ShowFilterBar = true;

            if (File.Exists("fec.xmldb")) {
                xmlDoc.Load("fec.xmldb");
                SetupEmployeeGrid();
                employeeTable = xmlDoc.SelectSingleNode("Employees");
            } else {
                GenerateDB();
                needsFirstSetup = true;
            }
        }

        private void GenerateDB() {
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Indent = true;
            settings.IndentChars = "\t";

            XmlDeclaration xmlDeclaration = xmlDoc.CreateXmlDeclaration("1.0", "UTF-8", null);
            XmlElement root = xmlDoc.DocumentElement;
            xmlDoc.InsertBefore(xmlDeclaration, root);
            xmlDoc.AppendChild(xmlDoc.CreateElement("Employees"));

            employeeTable = xmlDoc.SelectSingleNode("Employees");

            xmlDoc.Save("fec.xmldb");

        }

        private void SetupEmployeeGrid() {
            
            //Populate with data from an XML file.
            XmlData.ReadXml("fec.xmldb");

            //Binds the grid to Data Set.
            employeeGrid.DataSource = XmlData;
            try {
                employeeGrid.DataMember = XmlData.Tables[0].ToString();
            } 
            catch (Exception) { }
            

            for (int i = 0;i < employeeGrid.TableDescriptor.Columns.Count;i++) {
               employeeGrid.TableDescriptor.Columns[i].Width = employeeGrid.Width / employeeGrid.TableDescriptor.Columns.Count - 6;
            }

        }

        private void UpdateEmployeeGrid() {
            XmlData.Clear();
            XmlData.Tables[0].BeginLoadData();
            XmlData.ReadXml("fec.xmldb");
            XmlData.Tables[0].EndLoadData();
        }

        private void addEmployeeButton_Click(object sender, EventArgs e) {
            AddEmployeeForm prompt = new AddEmployeeForm();
            prompt.ShowDialog();

            if (needsFirstSetup && prompt.DialogResult == System.Windows.Forms.DialogResult.OK) {
                SetupEmployeeGrid();
            } else {
                UpdateEmployeeGrid();
            }
         
            employeeGrid.Table.TableDirty = true;
        }

        private void removeEmployeeButton_Click(object sender, EventArgs e) {
            GridRangeInfoList range = employeeGrid.TableModel.Selections.GetSelectedRows(true, true);

            foreach (GridRangeInfo info in range) {
                Element element = employeeGrid.TableModel.GetDisplayElementAt(info.Top);

                DatabaseWorker.RemoveEmployee(element.GetRecord()["Name"].ToString());
            }

            UpdateEmployeeGrid();          
        }

        private void employeeGrid_RecordValueChanged(object sender, RecordValueChangedEventArgs e) {
            DataRowView drv = e.Record.GetData() as DataRowView;

            XmlNode node = DatabaseWorker.FindEmployeeNode(temp);

            node["Name"].InnerText = drv[0].ToString();
            node["Job"].InnerText = drv[1].ToString();
            node["Address"].InnerText = drv[2].ToString();
            node["Phone"].InnerText = drv[3].ToString();

            xmlDoc.Save("fec.xmldb");
            UpdateEmployeeGrid();
        }

        private void employeeGrid_RecordValueChanging(object sender, RecordValueChangingEventArgs e) {
            DataRowView drv = e.Record.GetData() as DataRowView;

            temp = drv[0].ToString();
        }

        private void button1_Click(object sender, EventArgs e) {
            for (int i = 0; i < 40000;i++) {
                DatabaseWorker.AddEmployee(RandomString(5), RandomString(5), RandomString(5), RandomString(5), true);
            }

            fec_Main.xmlDoc.Save("fec.xmldb");
            UpdateEmployeeGrid();
        }

        static Random random = new Random();
        private static string RandomString(int Size) {
            string input = "abcdefghijklmnopqrstuvwxyz0123456789";
            System.Text.StringBuilder builder = new System.Text.StringBuilder();
            char ch;
            for (int i = 0;i < Size;i++) {
                ch = input[random.Next(0, input.Length)];
                builder.Append(ch);
            }
            return builder.ToString();
        }
    }
}
