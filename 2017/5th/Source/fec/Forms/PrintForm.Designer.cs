namespace fec {
    partial class PrintForm {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            Syncfusion.Windows.Forms.CaptionImage captionImage1 = new Syncfusion.Windows.Forms.CaptionImage();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PrintForm));
            this.tableComboBox = new System.Windows.Forms.ComboBox();
            this.selectTableLabel = new System.Windows.Forms.Label();
            this.cancelButton = new System.Windows.Forms.Button();
            this.printButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // tableComboBox
            // 
            this.tableComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.tableComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.tableComboBox.FormattingEnabled = true;
            this.tableComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "Employees",
            "Employee Schedule",
            "Customers",
            "Customer Attendance"});
            this.tableComboBox.Location = new System.Drawing.Point(61, 65);
            this.tableComboBox.Name = "tableComboBox";
            this.tableComboBox.Size = new System.Drawing.Size(159, 23);
            this.tableComboBox.TabIndex = 0;
            // 
            // selectTableLabel
            // 
            this.selectTableLabel.AutoSize = true;
            this.selectTableLabel.Location = new System.Drawing.Point(61, 46);
            this.selectTableLabel.Name = "selectTableLabel";
            this.selectTableLabel.Size = new System.Drawing.Size(66, 13);
            this.selectTableLabel.TabIndex = 1;
            this.selectTableLabel.Text = "Select table:";
            // 
            // cancelButton
            // 
            this.cancelButton.BackColor = System.Drawing.Color.Red;
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.cancelButton.ForeColor = System.Drawing.Color.White;
            this.cancelButton.Location = new System.Drawing.Point(103, 180);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 10;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = false;
            // 
            // printButton
            // 
            this.printButton.BackColor = System.Drawing.Color.DeepSkyBlue;
            this.printButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.printButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.printButton.ForeColor = System.Drawing.Color.White;
            this.printButton.Location = new System.Drawing.Point(103, 130);
            this.printButton.Name = "printButton";
            this.printButton.Size = new System.Drawing.Size(75, 35);
            this.printButton.TabIndex = 11;
            this.printButton.Text = "Print";
            this.printButton.UseVisualStyleBackColor = false;
            this.printButton.Click += new System.EventHandler(this.PrintButton_Click);
            // 
            // PrintForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.cancelButton;
            this.CaptionBarColor = System.Drawing.Color.DeepSkyBlue;
            this.CaptionBarHeight = 40;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionFont = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(20, -4);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(48, 48);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(276, 215);
            this.Controls.Add(this.printButton);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.selectTableLabel);
            this.Controls.Add(this.tableComboBox);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "PrintForm";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Print Report";
            this.TopMost = true;
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.PrintForm_FormClosing);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox tableComboBox;
        private System.Windows.Forms.Label selectTableLabel;
        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.Button printButton;
    }
}