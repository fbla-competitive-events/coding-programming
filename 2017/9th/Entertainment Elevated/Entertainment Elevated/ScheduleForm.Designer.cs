namespace Entertainment_Elevated
{
    partial class ScheduleForm
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ScheduleForm));
            this.ScheduleCalendar = new System.Windows.Forms.MonthCalendar();
            this.ScheduleFormPanel = new System.Windows.Forms.Panel();
            this.HelpButton = new System.Windows.Forms.Button();
            this.PayLabel = new System.Windows.Forms.Label();
            this.HoursLabel = new System.Windows.Forms.Label();
            this.ReportButton = new System.Windows.Forms.Button();
            this.ScheduleDataGridView = new System.Windows.Forms.DataGridView();
            this.MenuButton = new System.Windows.Forms.Button();
            this.PrintDocument = new System.Drawing.Printing.PrintDocument();
            this.PrintPreviewDialog = new System.Windows.Forms.PrintPreviewDialog();
            this.ScheduleFormPanel.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.ScheduleDataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // ScheduleCalendar
            // 
            this.ScheduleCalendar.Location = new System.Drawing.Point(156, 201);
            this.ScheduleCalendar.MaxSelectionCount = 1;
            this.ScheduleCalendar.Name = "ScheduleCalendar";
            this.ScheduleCalendar.TabIndex = 0;
            this.ScheduleCalendar.DateSelected += new System.Windows.Forms.DateRangeEventHandler(this.ScheduleCalendar_DateSelected);
            // 
            // ScheduleFormPanel
            // 
            this.ScheduleFormPanel.Controls.Add(this.HelpButton);
            this.ScheduleFormPanel.Controls.Add(this.PayLabel);
            this.ScheduleFormPanel.Controls.Add(this.HoursLabel);
            this.ScheduleFormPanel.Controls.Add(this.ReportButton);
            this.ScheduleFormPanel.Controls.Add(this.ScheduleDataGridView);
            this.ScheduleFormPanel.Controls.Add(this.MenuButton);
            this.ScheduleFormPanel.Controls.Add(this.ScheduleCalendar);
            this.ScheduleFormPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ScheduleFormPanel.Location = new System.Drawing.Point(0, 0);
            this.ScheduleFormPanel.Name = "ScheduleFormPanel";
            this.ScheduleFormPanel.Size = new System.Drawing.Size(584, 461);
            this.ScheduleFormPanel.TabIndex = 1;
            // 
            // HelpButton
            // 
            this.HelpButton.Location = new System.Drawing.Point(497, 426);
            this.HelpButton.Name = "HelpButton";
            this.HelpButton.Size = new System.Drawing.Size(75, 23);
            this.HelpButton.TabIndex = 6;
            this.HelpButton.Text = "Help";
            this.HelpButton.UseVisualStyleBackColor = true;
            this.HelpButton.Click += new System.EventHandler(this.HelpButton_Click);
            // 
            // PayLabel
            // 
            this.PayLabel.AutoSize = true;
            this.PayLabel.Location = new System.Drawing.Point(399, 175);
            this.PayLabel.Name = "PayLabel";
            this.PayLabel.Size = new System.Drawing.Size(64, 13);
            this.PayLabel.TabIndex = 5;
            this.PayLabel.Text = "Total Pay: 0";
            // 
            // HoursLabel
            // 
            this.HoursLabel.AutoSize = true;
            this.HoursLabel.Location = new System.Drawing.Point(58, 175);
            this.HoursLabel.Name = "HoursLabel";
            this.HoursLabel.Size = new System.Drawing.Size(47, 13);
            this.HoursLabel.TabIndex = 4;
            this.HoursLabel.Text = "Hours: 0";
            // 
            // ReportButton
            // 
            this.ReportButton.Location = new System.Drawing.Point(432, 246);
            this.ReportButton.Name = "ReportButton";
            this.ReportButton.Size = new System.Drawing.Size(120, 35);
            this.ReportButton.TabIndex = 1;
            this.ReportButton.Text = "Print Weekly Report";
            this.ReportButton.UseVisualStyleBackColor = true;
            this.ReportButton.Click += new System.EventHandler(this.ReportButton_Click);
            // 
            // ScheduleDataGridView
            // 
            this.ScheduleDataGridView.AllowUserToAddRows = false;
            this.ScheduleDataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.AllCells;
            this.ScheduleDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.ScheduleDataGridView.Dock = System.Windows.Forms.DockStyle.Top;
            this.ScheduleDataGridView.Location = new System.Drawing.Point(0, 0);
            this.ScheduleDataGridView.Name = "ScheduleDataGridView";
            this.ScheduleDataGridView.RowHeadersVisible = false;
            this.ScheduleDataGridView.Size = new System.Drawing.Size(584, 150);
            this.ScheduleDataGridView.TabIndex = 4;
            this.ScheduleDataGridView.CellEndEdit += new System.Windows.Forms.DataGridViewCellEventHandler(this.ScheduleDataGridView_CellEndEdit);
            // 
            // MenuButton
            // 
            this.MenuButton.Location = new System.Drawing.Point(12, 426);
            this.MenuButton.Name = "MenuButton";
            this.MenuButton.Size = new System.Drawing.Size(75, 23);
            this.MenuButton.TabIndex = 2;
            this.MenuButton.Text = "Menu";
            this.MenuButton.UseVisualStyleBackColor = true;
            this.MenuButton.Click += new System.EventHandler(this.MenuButton_Click);
            // 
            // PrintDocument
            // 
            this.PrintDocument.PrintPage += new System.Drawing.Printing.PrintPageEventHandler(this.PrintDocument_PrintPage);
            // 
            // PrintPreviewDialog
            // 
            this.PrintPreviewDialog.AutoScrollMargin = new System.Drawing.Size(0, 0);
            this.PrintPreviewDialog.AutoScrollMinSize = new System.Drawing.Size(0, 0);
            this.PrintPreviewDialog.ClientSize = new System.Drawing.Size(400, 300);
            this.PrintPreviewDialog.Enabled = true;
            this.PrintPreviewDialog.Icon = ((System.Drawing.Icon)(resources.GetObject("PrintPreviewDialog.Icon")));
            this.PrintPreviewDialog.Name = "PrintPreviewDialog";
            this.PrintPreviewDialog.Visible = false;
            // 
            // ScheduleForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(584, 461);
            this.Controls.Add(this.ScheduleFormPanel);
            this.Name = "ScheduleForm";
            this.Text = "ScheduleForm";
            this.ScheduleFormPanel.ResumeLayout(false);
            this.ScheduleFormPanel.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.ScheduleDataGridView)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.MonthCalendar ScheduleCalendar;
        public System.Windows.Forms.Panel ScheduleFormPanel;
        private System.Windows.Forms.Button MenuButton;
        private System.Windows.Forms.DataGridView ScheduleDataGridView;
        private System.Windows.Forms.Button ReportButton;
        private System.Drawing.Printing.PrintDocument PrintDocument;
        private System.Windows.Forms.PrintPreviewDialog PrintPreviewDialog;
        private System.Windows.Forms.Label HoursLabel;
        private System.Windows.Forms.Label PayLabel;
        private System.Windows.Forms.Button HelpButton;
    }
}