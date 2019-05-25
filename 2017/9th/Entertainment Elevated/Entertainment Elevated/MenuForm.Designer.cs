namespace Entertainment_Elevated
{
    partial class MenuForm
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
        public void InitializeComponent()
        {
            this.EmployeeButton = new System.Windows.Forms.Button();
            this.CustomerButton = new System.Windows.Forms.Button();
            this.ScheduleButton = new System.Windows.Forms.Button();
            this.MainFormPanel = new System.Windows.Forms.Panel();
            this.HelpButton = new System.Windows.Forms.Button();
            this.MainFormPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // EmployeeButton
            // 
            this.EmployeeButton.Location = new System.Drawing.Point(3, 3);
            this.EmployeeButton.Name = "EmployeeButton";
            this.EmployeeButton.Size = new System.Drawing.Size(281, 212);
            this.EmployeeButton.TabIndex = 0;
            this.EmployeeButton.Text = "Employees";
            this.EmployeeButton.UseVisualStyleBackColor = true;
            this.EmployeeButton.Click += new System.EventHandler(this.EmployeeButton_Click);
            // 
            // CustomerButton
            // 
            this.CustomerButton.Location = new System.Drawing.Point(3, 221);
            this.CustomerButton.Name = "CustomerButton";
            this.CustomerButton.Size = new System.Drawing.Size(282, 228);
            this.CustomerButton.TabIndex = 2;
            this.CustomerButton.Text = "Customers";
            this.CustomerButton.UseVisualStyleBackColor = true;
            this.CustomerButton.Click += new System.EventHandler(this.CustomerButton_Click);
            // 
            // ScheduleButton
            // 
            this.ScheduleButton.Location = new System.Drawing.Point(291, 3);
            this.ScheduleButton.Name = "ScheduleButton";
            this.ScheduleButton.Size = new System.Drawing.Size(282, 215);
            this.ScheduleButton.TabIndex = 1;
            this.ScheduleButton.Text = "Schedule";
            this.ScheduleButton.UseVisualStyleBackColor = true;
            this.ScheduleButton.Click += new System.EventHandler(this.ScheduleButton_Click);
            // 
            // MainFormPanel
            // 
            this.MainFormPanel.Controls.Add(this.HelpButton);
            this.MainFormPanel.Controls.Add(this.EmployeeButton);
            this.MainFormPanel.Controls.Add(this.CustomerButton);
            this.MainFormPanel.Controls.Add(this.ScheduleButton);
            this.MainFormPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.MainFormPanel.Location = new System.Drawing.Point(0, 0);
            this.MainFormPanel.Name = "MainFormPanel";
            this.MainFormPanel.Size = new System.Drawing.Size(584, 461);
            this.MainFormPanel.TabIndex = 4;
            // 
            // HelpButton
            // 
            this.HelpButton.Location = new System.Drawing.Point(291, 224);
            this.HelpButton.Name = "HelpButton";
            this.HelpButton.Size = new System.Drawing.Size(282, 228);
            this.HelpButton.TabIndex = 3;
            this.HelpButton.Text = "Help";
            this.HelpButton.UseVisualStyleBackColor = true;
            this.HelpButton.Click += new System.EventHandler(this.HelpButton_Click);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(584, 461);
            this.Controls.Add(this.MainFormPanel);
            this.Name = "MainForm";
            this.Text = " ";
            this.MainFormPanel.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button EmployeeButton;
        private System.Windows.Forms.Button CustomerButton;
        private System.Windows.Forms.Button ScheduleButton;
        public System.Windows.Forms.Panel MainFormPanel;
        private System.Windows.Forms.Button HelpButton;
    }
}
