namespace Entertainment_Elevated
{
    partial class CustomerForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CustomerForm));
            this.CustomerFormPanel = new System.Windows.Forms.Panel();
            this.AddVisitButton = new System.Windows.Forms.Button();
            this.PrintReportButton = new System.Windows.Forms.Button();
            this.AddCustomerButton = new System.Windows.Forms.Button();
            this.CustomerListBox = new System.Windows.Forms.ListBox();
            this.SearchLabel = new System.Windows.Forms.Label();
            this.MenuButton = new System.Windows.Forms.Button();
            this.PrintPreviewDialog = new System.Windows.Forms.PrintPreviewDialog();
            this.PrintDocument = new System.Drawing.Printing.PrintDocument();
            this.CustomerStatisticsButton = new System.Windows.Forms.Button();
            this.SearchTextBox = new System.Windows.Forms.TextBox();
            this.CustomerFormPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // CustomerFormPanel
            // 
            this.CustomerFormPanel.Controls.Add(this.SearchTextBox);
            this.CustomerFormPanel.Controls.Add(this.CustomerStatisticsButton);
            this.CustomerFormPanel.Controls.Add(this.AddVisitButton);
            this.CustomerFormPanel.Controls.Add(this.PrintReportButton);
            this.CustomerFormPanel.Controls.Add(this.AddCustomerButton);
            this.CustomerFormPanel.Controls.Add(this.CustomerListBox);
            this.CustomerFormPanel.Controls.Add(this.SearchLabel);
            this.CustomerFormPanel.Controls.Add(this.MenuButton);
            this.CustomerFormPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.CustomerFormPanel.Location = new System.Drawing.Point(0, 0);
            this.CustomerFormPanel.Name = "CustomerFormPanel";
            this.CustomerFormPanel.Size = new System.Drawing.Size(584, 461);
            this.CustomerFormPanel.TabIndex = 10;
            // 
            // AddVisitButton
            // 
            this.AddVisitButton.Location = new System.Drawing.Point(420, 173);
            this.AddVisitButton.Name = "AddVisitButton";
            this.AddVisitButton.Size = new System.Drawing.Size(152, 64);
            this.AddVisitButton.TabIndex = 2;
            this.AddVisitButton.Text = "Add Visit";
            this.AddVisitButton.UseVisualStyleBackColor = true;
            this.AddVisitButton.Click += new System.EventHandler(this.AddVisitButton_Click);
            // 
            // PrintReportButton
            // 
            this.PrintReportButton.Location = new System.Drawing.Point(420, 379);
            this.PrintReportButton.Name = "PrintReportButton";
            this.PrintReportButton.Size = new System.Drawing.Size(152, 69);
            this.PrintReportButton.TabIndex = 5;
            this.PrintReportButton.Text = "Print Report";
            this.PrintReportButton.UseVisualStyleBackColor = true;
            this.PrintReportButton.Click += new System.EventHandler(this.ReportButton_Click);
            // 
            // AddCustomerButton
            // 
            this.AddCustomerButton.Location = new System.Drawing.Point(420, 243);
            this.AddCustomerButton.Name = "AddCustomerButton";
            this.AddCustomerButton.Size = new System.Drawing.Size(152, 62);
            this.AddCustomerButton.TabIndex = 3;
            this.AddCustomerButton.Text = "Add Customer";
            this.AddCustomerButton.UseVisualStyleBackColor = true;
            this.AddCustomerButton.Click += new System.EventHandler(this.AddCustomerButton_Click);
            // 
            // CustomerListBox
            // 
            this.CustomerListBox.FormattingEnabled = true;
            this.CustomerListBox.Location = new System.Drawing.Point(62, 57);
            this.CustomerListBox.Name = "CustomerListBox";
            this.CustomerListBox.Size = new System.Drawing.Size(182, 303);
            this.CustomerListBox.TabIndex = 1;
            // 
            // SearchLabel
            // 
            this.SearchLabel.AutoSize = true;
            this.SearchLabel.Location = new System.Drawing.Point(12, 24);
            this.SearchLabel.Name = "SearchLabel";
            this.SearchLabel.Size = new System.Drawing.Size(44, 13);
            this.SearchLabel.TabIndex = 7;
            this.SearchLabel.Text = "Search:";
            // 
            // MenuButton
            // 
            this.MenuButton.Location = new System.Drawing.Point(12, 427);
            this.MenuButton.Name = "MenuButton";
            this.MenuButton.Size = new System.Drawing.Size(75, 23);
            this.MenuButton.TabIndex = 6;
            this.MenuButton.Text = "Menu";
            this.MenuButton.UseVisualStyleBackColor = true;
            this.MenuButton.Click += new System.EventHandler(this.MenuButton_Click);
            // 
            // PrintPreviewDialog
            // 
            this.PrintPreviewDialog.AutoScrollMargin = new System.Drawing.Size(0, 0);
            this.PrintPreviewDialog.AutoScrollMinSize = new System.Drawing.Size(0, 0);
            this.PrintPreviewDialog.ClientSize = new System.Drawing.Size(400, 300);
            this.PrintPreviewDialog.Document = this.PrintDocument;
            this.PrintPreviewDialog.Enabled = true;
            this.PrintPreviewDialog.Icon = ((System.Drawing.Icon)(resources.GetObject("PrintPreviewDialog.Icon")));
            this.PrintPreviewDialog.Name = "PrintPreviewDialog";
            this.PrintPreviewDialog.Visible = false;
            // 
            // PrintDocument
            // 
            this.PrintDocument.PrintPage += new System.Drawing.Printing.PrintPageEventHandler(this.PrintDocument_PrintPage);
            // 
            // CustomerStatisticsButton
            // 
            this.CustomerStatisticsButton.Location = new System.Drawing.Point(420, 311);
            this.CustomerStatisticsButton.Name = "CustomerStatisticsButton";
            this.CustomerStatisticsButton.Size = new System.Drawing.Size(152, 62);
            this.CustomerStatisticsButton.TabIndex = 4;
            this.CustomerStatisticsButton.Text = "Customer Statistics";
            this.CustomerStatisticsButton.UseVisualStyleBackColor = true;
            this.CustomerStatisticsButton.Click += new System.EventHandler(this.CustomerStatisticsButton_Click);
            // 
            // SearchTextBox
            // 
            this.SearchTextBox.Location = new System.Drawing.Point(62, 21);
            this.SearchTextBox.Name = "SearchTextBox";
            this.SearchTextBox.Size = new System.Drawing.Size(182, 20);
            this.SearchTextBox.TabIndex = 0;
            // 
            // CustomerForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(584, 461);
            this.Controls.Add(this.CustomerFormPanel);
            this.Name = "CustomerForm";
            this.Text = "CustomerForm";
            this.CustomerFormPanel.ResumeLayout(false);
            this.CustomerFormPanel.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        public System.Windows.Forms.Panel CustomerFormPanel;
        private System.Windows.Forms.Button MenuButton;
        private System.Windows.Forms.Label SearchLabel;
        private System.Windows.Forms.ListBox CustomerListBox;
        private System.Windows.Forms.Button AddCustomerButton;
        private System.Windows.Forms.Button PrintReportButton;
        private System.Windows.Forms.PrintPreviewDialog PrintPreviewDialog;
        private System.Drawing.Printing.PrintDocument PrintDocument;
        private System.Windows.Forms.Button AddVisitButton;
        private System.Windows.Forms.Button CustomerStatisticsButton;
        private System.Windows.Forms.TextBox SearchTextBox;
    }
}