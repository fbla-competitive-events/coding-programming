namespace fec {
    partial class AddCustomerForm {
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(AddCustomerForm));
            this.cancelButton = new System.Windows.Forms.Button();
            this.addButton = new System.Windows.Forms.Button();
            this.employeeDetailsGroupBox = new System.Windows.Forms.GroupBox();
            this.membershipComboBox = new System.Windows.Forms.ComboBox();
            this.nameTextBox = new System.Windows.Forms.TextBox();
            this.phoneTextBox = new System.Windows.Forms.TextBox();
            this.nameLabel = new System.Windows.Forms.Label();
            this.phoneLabel = new System.Windows.Forms.Label();
            this.membershipLabel = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.mondayLabel = new System.Windows.Forms.Label();
            this.sundayComboBox = new System.Windows.Forms.ComboBox();
            this.tuesdayLabel = new System.Windows.Forms.Label();
            this.saturdayComboBox = new System.Windows.Forms.ComboBox();
            this.wednesdayLabel = new System.Windows.Forms.Label();
            this.fridayComboBox = new System.Windows.Forms.ComboBox();
            this.thursdayLabel = new System.Windows.Forms.Label();
            this.thursdayComboBox = new System.Windows.Forms.ComboBox();
            this.fridayLabel = new System.Windows.Forms.Label();
            this.wednesdayComboBox = new System.Windows.Forms.ComboBox();
            this.saturdayLabel = new System.Windows.Forms.Label();
            this.tuesdayComboBox = new System.Windows.Forms.ComboBox();
            this.sundayLabel = new System.Windows.Forms.Label();
            this.mondayComboBox = new System.Windows.Forms.ComboBox();
            this.employeeDetailsGroupBox.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // cancelButton
            // 
            this.cancelButton.BackColor = System.Drawing.Color.Red;
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.cancelButton.ForeColor = System.Drawing.Color.White;
            this.cancelButton.Location = new System.Drawing.Point(259, 294);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 11;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = false;
            // 
            // addButton
            // 
            this.addButton.BackColor = System.Drawing.Color.LimeGreen;
            this.addButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.addButton.ForeColor = System.Drawing.Color.White;
            this.addButton.Location = new System.Drawing.Point(259, 243);
            this.addButton.Name = "addButton";
            this.addButton.Size = new System.Drawing.Size(75, 33);
            this.addButton.TabIndex = 10;
            this.addButton.Text = "Add";
            this.addButton.UseVisualStyleBackColor = false;
            this.addButton.Click += new System.EventHandler(this.addButton_Click);
            // 
            // employeeDetailsGroupBox
            // 
            this.employeeDetailsGroupBox.Controls.Add(this.membershipComboBox);
            this.employeeDetailsGroupBox.Controls.Add(this.nameTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.phoneTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.nameLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.phoneLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.membershipLabel);
            this.employeeDetailsGroupBox.Location = new System.Drawing.Point(21, 30);
            this.employeeDetailsGroupBox.Name = "employeeDetailsGroupBox";
            this.employeeDetailsGroupBox.Size = new System.Drawing.Size(156, 198);
            this.employeeDetailsGroupBox.TabIndex = 25;
            this.employeeDetailsGroupBox.TabStop = false;
            this.employeeDetailsGroupBox.Text = "Customer Details";
            // 
            // membershipComboBox
            // 
            this.membershipComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.membershipComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.membershipComboBox.FormattingEnabled = true;
            this.membershipComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "N/A",
            "SMILE PLUS"});
            this.membershipComboBox.Location = new System.Drawing.Point(9, 141);
            this.membershipComboBox.Name = "membershipComboBox";
            this.membershipComboBox.Size = new System.Drawing.Size(134, 21);
            this.membershipComboBox.TabIndex = 9;
            // 
            // nameTextBox
            // 
            this.nameTextBox.BackColor = System.Drawing.SystemColors.Window;
            this.nameTextBox.Location = new System.Drawing.Point(9, 53);
            this.nameTextBox.MaxLength = 100;
            this.nameTextBox.Name = "nameTextBox";
            this.nameTextBox.Size = new System.Drawing.Size(133, 20);
            this.nameTextBox.TabIndex = 1;
            this.nameTextBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.nameTextBox_KeyPress);
            // 
            // phoneTextBox
            // 
            this.phoneTextBox.Location = new System.Drawing.Point(9, 97);
            this.phoneTextBox.MaxLength = 12;
            this.phoneTextBox.Name = "phoneTextBox";
            this.phoneTextBox.ShortcutsEnabled = false;
            this.phoneTextBox.Size = new System.Drawing.Size(134, 20);
            this.phoneTextBox.TabIndex = 3;
            this.phoneTextBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.phoneTextBox_KeyPress);
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Location = new System.Drawing.Point(6, 37);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(35, 13);
            this.nameLabel.TabIndex = 4;
            this.nameLabel.Text = "Name";
            // 
            // phoneLabel
            // 
            this.phoneLabel.AutoSize = true;
            this.phoneLabel.Location = new System.Drawing.Point(6, 81);
            this.phoneLabel.Name = "phoneLabel";
            this.phoneLabel.Size = new System.Drawing.Size(38, 13);
            this.phoneLabel.TabIndex = 6;
            this.phoneLabel.Text = "Phone";
            // 
            // membershipLabel
            // 
            this.membershipLabel.AutoSize = true;
            this.membershipLabel.Location = new System.Drawing.Point(6, 125);
            this.membershipLabel.Name = "membershipLabel";
            this.membershipLabel.Size = new System.Drawing.Size(64, 13);
            this.membershipLabel.TabIndex = 8;
            this.membershipLabel.Text = "Membership";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.mondayLabel);
            this.groupBox1.Controls.Add(this.sundayComboBox);
            this.groupBox1.Controls.Add(this.tuesdayLabel);
            this.groupBox1.Controls.Add(this.saturdayComboBox);
            this.groupBox1.Controls.Add(this.wednesdayLabel);
            this.groupBox1.Controls.Add(this.fridayComboBox);
            this.groupBox1.Controls.Add(this.thursdayLabel);
            this.groupBox1.Controls.Add(this.thursdayComboBox);
            this.groupBox1.Controls.Add(this.fridayLabel);
            this.groupBox1.Controls.Add(this.wednesdayComboBox);
            this.groupBox1.Controls.Add(this.saturdayLabel);
            this.groupBox1.Controls.Add(this.tuesdayComboBox);
            this.groupBox1.Controls.Add(this.sundayLabel);
            this.groupBox1.Controls.Add(this.mondayComboBox);
            this.groupBox1.Location = new System.Drawing.Point(202, 30);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(401, 198);
            this.groupBox1.TabIndex = 26;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Customer Attendance";
            // 
            // mondayLabel
            // 
            this.mondayLabel.AutoSize = true;
            this.mondayLabel.Location = new System.Drawing.Point(15, 29);
            this.mondayLabel.Name = "mondayLabel";
            this.mondayLabel.Size = new System.Drawing.Size(45, 13);
            this.mondayLabel.TabIndex = 10;
            this.mondayLabel.Text = "Monday";
            // 
            // sundayComboBox
            // 
            this.sundayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.sundayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.sundayComboBox.FormattingEnabled = true;
            this.sundayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.sundayComboBox.Location = new System.Drawing.Point(276, 115);
            this.sundayComboBox.Name = "sundayComboBox";
            this.sundayComboBox.Size = new System.Drawing.Size(115, 20);
            this.sundayComboBox.TabIndex = 23;
            // 
            // tuesdayLabel
            // 
            this.tuesdayLabel.AutoSize = true;
            this.tuesdayLabel.Location = new System.Drawing.Point(15, 62);
            this.tuesdayLabel.Name = "tuesdayLabel";
            this.tuesdayLabel.Size = new System.Drawing.Size(48, 13);
            this.tuesdayLabel.TabIndex = 11;
            this.tuesdayLabel.Text = "Tuesday";
            // 
            // saturdayComboBox
            // 
            this.saturdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.saturdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.saturdayComboBox.FormattingEnabled = true;
            this.saturdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.saturdayComboBox.Location = new System.Drawing.Point(276, 78);
            this.saturdayComboBox.Name = "saturdayComboBox";
            this.saturdayComboBox.Size = new System.Drawing.Size(115, 20);
            this.saturdayComboBox.TabIndex = 22;
            // 
            // wednesdayLabel
            // 
            this.wednesdayLabel.AutoSize = true;
            this.wednesdayLabel.Location = new System.Drawing.Point(15, 97);
            this.wednesdayLabel.Name = "wednesdayLabel";
            this.wednesdayLabel.Size = new System.Drawing.Size(64, 13);
            this.wednesdayLabel.TabIndex = 12;
            this.wednesdayLabel.Text = "Wednesday";
            // 
            // fridayComboBox
            // 
            this.fridayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.fridayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.fridayComboBox.FormattingEnabled = true;
            this.fridayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.fridayComboBox.Location = new System.Drawing.Point(85, 160);
            this.fridayComboBox.Name = "fridayComboBox";
            this.fridayComboBox.Size = new System.Drawing.Size(115, 20);
            this.fridayComboBox.TabIndex = 21;
            // 
            // thursdayLabel
            // 
            this.thursdayLabel.AutoSize = true;
            this.thursdayLabel.Location = new System.Drawing.Point(15, 131);
            this.thursdayLabel.Name = "thursdayLabel";
            this.thursdayLabel.Size = new System.Drawing.Size(51, 13);
            this.thursdayLabel.TabIndex = 13;
            this.thursdayLabel.Text = "Thursday";
            // 
            // thursdayComboBox
            // 
            this.thursdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.thursdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.thursdayComboBox.FormattingEnabled = true;
            this.thursdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.thursdayComboBox.Location = new System.Drawing.Point(85, 128);
            this.thursdayComboBox.Name = "thursdayComboBox";
            this.thursdayComboBox.Size = new System.Drawing.Size(115, 20);
            this.thursdayComboBox.TabIndex = 20;
            // 
            // fridayLabel
            // 
            this.fridayLabel.AutoSize = true;
            this.fridayLabel.Location = new System.Drawing.Point(15, 163);
            this.fridayLabel.Name = "fridayLabel";
            this.fridayLabel.Size = new System.Drawing.Size(35, 13);
            this.fridayLabel.TabIndex = 14;
            this.fridayLabel.Text = "Friday";
            // 
            // wednesdayComboBox
            // 
            this.wednesdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.wednesdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.wednesdayComboBox.FormattingEnabled = true;
            this.wednesdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.wednesdayComboBox.Location = new System.Drawing.Point(85, 94);
            this.wednesdayComboBox.Name = "wednesdayComboBox";
            this.wednesdayComboBox.Size = new System.Drawing.Size(115, 20);
            this.wednesdayComboBox.TabIndex = 19;
            // 
            // saturdayLabel
            // 
            this.saturdayLabel.AutoSize = true;
            this.saturdayLabel.Location = new System.Drawing.Point(218, 81);
            this.saturdayLabel.Name = "saturdayLabel";
            this.saturdayLabel.Size = new System.Drawing.Size(49, 13);
            this.saturdayLabel.TabIndex = 15;
            this.saturdayLabel.Text = "Saturday";
            // 
            // tuesdayComboBox
            // 
            this.tuesdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.tuesdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.tuesdayComboBox.FormattingEnabled = true;
            this.tuesdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.tuesdayComboBox.Location = new System.Drawing.Point(85, 59);
            this.tuesdayComboBox.Name = "tuesdayComboBox";
            this.tuesdayComboBox.Size = new System.Drawing.Size(115, 20);
            this.tuesdayComboBox.TabIndex = 18;
            // 
            // sundayLabel
            // 
            this.sundayLabel.AutoSize = true;
            this.sundayLabel.Location = new System.Drawing.Point(218, 118);
            this.sundayLabel.Name = "sundayLabel";
            this.sundayLabel.Size = new System.Drawing.Size(43, 13);
            this.sundayLabel.TabIndex = 16;
            this.sundayLabel.Text = "Sunday";
            // 
            // mondayComboBox
            // 
            this.mondayComboBox.BackColor = System.Drawing.SystemColors.Window;
            this.mondayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.mondayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.1F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.mondayComboBox.FormattingEnabled = true;
            this.mondayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.mondayComboBox.Location = new System.Drawing.Point(85, 26);
            this.mondayComboBox.Name = "mondayComboBox";
            this.mondayComboBox.Size = new System.Drawing.Size(115, 20);
            this.mondayComboBox.TabIndex = 17;
            // 
            // AddCustomerForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.cancelButton;
            this.CaptionBarColor = System.Drawing.Color.LimeGreen;
            this.CaptionBarHeight = 40;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(34, -4);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(48, 48);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(612, 323);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.employeeDetailsGroupBox);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.addButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Name = "AddCustomerForm";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.ShowMaximizeBox = false;
            this.ShowMinimizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Add Customer";
            this.TopMost = true;
            this.employeeDetailsGroupBox.ResumeLayout(false);
            this.employeeDetailsGroupBox.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.Button addButton;
        private System.Windows.Forms.GroupBox employeeDetailsGroupBox;
        private System.Windows.Forms.ComboBox membershipComboBox;
        private System.Windows.Forms.TextBox nameTextBox;
        private System.Windows.Forms.TextBox phoneTextBox;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.Label phoneLabel;
        private System.Windows.Forms.Label membershipLabel;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label mondayLabel;
        private System.Windows.Forms.ComboBox sundayComboBox;
        private System.Windows.Forms.Label tuesdayLabel;
        private System.Windows.Forms.ComboBox saturdayComboBox;
        private System.Windows.Forms.Label wednesdayLabel;
        private System.Windows.Forms.ComboBox fridayComboBox;
        private System.Windows.Forms.Label thursdayLabel;
        private System.Windows.Forms.ComboBox thursdayComboBox;
        private System.Windows.Forms.Label fridayLabel;
        private System.Windows.Forms.ComboBox wednesdayComboBox;
        private System.Windows.Forms.Label saturdayLabel;
        private System.Windows.Forms.ComboBox tuesdayComboBox;
        private System.Windows.Forms.Label sundayLabel;
        private System.Windows.Forms.ComboBox mondayComboBox;
    }
}