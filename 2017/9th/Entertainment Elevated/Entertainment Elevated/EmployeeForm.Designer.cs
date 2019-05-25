namespace Entertainment_Elevated
{
    partial class EmployeeForm
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
            this.EmployeeDataGridView = new System.Windows.Forms.DataGridView();
            this.EmployeeFormPanel = new System.Windows.Forms.Panel();
            this.DeleteEmployeeButton = new System.Windows.Forms.Button();
            this.AddEmployeeButton = new System.Windows.Forms.Button();
            this.MenuButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.EmployeeDataGridView)).BeginInit();
            this.EmployeeFormPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // EmployeeDataGridView
            // 
            this.EmployeeDataGridView.AllowUserToOrderColumns = true;
            this.EmployeeDataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.EmployeeDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.EmployeeDataGridView.Dock = System.Windows.Forms.DockStyle.Top;
            this.EmployeeDataGridView.Location = new System.Drawing.Point(0, 0);
            this.EmployeeDataGridView.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.EmployeeDataGridView.Name = "EmployeeDataGridView";
            this.EmployeeDataGridView.RowHeadersVisible = false;
            this.EmployeeDataGridView.RowHeadersWidthSizeMode = System.Windows.Forms.DataGridViewRowHeadersWidthSizeMode.DisableResizing;
            this.EmployeeDataGridView.Size = new System.Drawing.Size(876, 231);
            this.EmployeeDataGridView.TabIndex = 0;
            // 
            // EmployeeFormPanel
            // 
            this.EmployeeFormPanel.Controls.Add(this.DeleteEmployeeButton);
            this.EmployeeFormPanel.Controls.Add(this.AddEmployeeButton);
            this.EmployeeFormPanel.Controls.Add(this.MenuButton);
            this.EmployeeFormPanel.Controls.Add(this.EmployeeDataGridView);
            this.EmployeeFormPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.EmployeeFormPanel.Location = new System.Drawing.Point(0, 0);
            this.EmployeeFormPanel.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.EmployeeFormPanel.Name = "EmployeeFormPanel";
            this.EmployeeFormPanel.Size = new System.Drawing.Size(876, 709);
            this.EmployeeFormPanel.TabIndex = 1;
            // 
            // DeleteEmployeeButton
            // 
            this.DeleteEmployeeButton.Location = new System.Drawing.Point(219, 426);
            this.DeleteEmployeeButton.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.DeleteEmployeeButton.Name = "DeleteEmployeeButton";
            this.DeleteEmployeeButton.Size = new System.Drawing.Size(408, 112);
            this.DeleteEmployeeButton.TabIndex = 2;
            this.DeleteEmployeeButton.Text = "Delete Employee";
            this.DeleteEmployeeButton.UseVisualStyleBackColor = true;
            this.DeleteEmployeeButton.Click += new System.EventHandler(this.DeleteEmployeeButton_Click);
            // 
            // AddEmployeeButton
            // 
            this.AddEmployeeButton.Location = new System.Drawing.Point(219, 288);
            this.AddEmployeeButton.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.AddEmployeeButton.Name = "AddEmployeeButton";
            this.AddEmployeeButton.Size = new System.Drawing.Size(408, 112);
            this.AddEmployeeButton.TabIndex = 1;
            this.AddEmployeeButton.Text = "Add Employee";
            this.AddEmployeeButton.UseVisualStyleBackColor = true;
            this.AddEmployeeButton.Click += new System.EventHandler(this.AddEmployeeButton_Click);
            // 
            // MenuButton
            // 
            this.MenuButton.Location = new System.Drawing.Point(18, 657);
            this.MenuButton.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.MenuButton.Name = "MenuButton";
            this.MenuButton.Size = new System.Drawing.Size(112, 35);
            this.MenuButton.TabIndex = 3;
            this.MenuButton.Text = "Menu";
            this.MenuButton.UseVisualStyleBackColor = true;
            this.MenuButton.Click += new System.EventHandler(this.MenuButton_Click);
            // 
            // EmployeeForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(876, 709);
            this.Controls.Add(this.EmployeeFormPanel);
            this.Margin = new System.Windows.Forms.Padding(4, 5, 4, 5);
            this.Name = "EmployeeForm";
            this.Text = "EmployeeForm";
            ((System.ComponentModel.ISupportInitialize)(this.EmployeeDataGridView)).EndInit();
            this.EmployeeFormPanel.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion
        public System.Windows.Forms.Panel EmployeeFormPanel;
        public System.Windows.Forms.DataGridView EmployeeDataGridView;
        private System.Windows.Forms.Button MenuButton;
        private System.Windows.Forms.Button AddEmployeeButton;
        private System.Windows.Forms.Button DeleteEmployeeButton;
    }
}