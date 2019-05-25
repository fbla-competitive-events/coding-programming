namespace Entertainment_Elevated
{
    partial class DeleteEmployeeForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.EmployeeListBox = new System.Windows.Forms.ListBox();
            this.DeleteEmployeesButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // EmployeeListBox
            // 
            this.EmployeeListBox.FormattingEnabled = true;
            this.EmployeeListBox.Location = new System.Drawing.Point(12, 12);
            this.EmployeeListBox.Name = "EmployeeListBox";
            this.EmployeeListBox.SelectionMode = System.Windows.Forms.SelectionMode.MultiExtended;
            this.EmployeeListBox.Size = new System.Drawing.Size(260, 147);
            this.EmployeeListBox.TabIndex = 1;
            // 
            // DeleteEmployeesButton
            // 
            this.DeleteEmployeesButton.Location = new System.Drawing.Point(12, 177);
            this.DeleteEmployeesButton.Name = "DeleteEmployeesButton";
            this.DeleteEmployeesButton.Size = new System.Drawing.Size(260, 64);
            this.DeleteEmployeesButton.TabIndex = 2;
            this.DeleteEmployeesButton.Text = "Delete Selected Employees";
            this.DeleteEmployeesButton.UseVisualStyleBackColor = true;
            this.DeleteEmployeesButton.Click += new System.EventHandler(this.DeleteEmployeesButton_Click);
            // 
            // DeleteEmployeeForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 251);
            this.Controls.Add(this.DeleteEmployeesButton);
            this.Controls.Add(this.EmployeeListBox);
            this.Name = "DeleteEmployeeForm";
            this.Text = "DeleteEmployeeForm";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.DeleteEmployeeForm_FormClosed);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListBox EmployeeListBox;
        private System.Windows.Forms.Button DeleteEmployeesButton;
    }
}