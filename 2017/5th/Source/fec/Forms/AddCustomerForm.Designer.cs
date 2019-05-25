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
            this.middleTextBox = new System.Windows.Forms.TextBox();
            this.lastNameTextBox = new System.Windows.Forms.TextBox();
            this.lastNameLabel = new System.Windows.Forms.Label();
            this.middleLabel = new System.Windows.Forms.Label();
            this.phoneTextBox = new System.Windows.Forms.MaskedTextBox();
            this.membershipComboBox = new System.Windows.Forms.ComboBox();
            this.firstNameTextBox = new System.Windows.Forms.TextBox();
            this.firstNameLabel = new System.Windows.Forms.Label();
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
            this.cancelButton.Location = new System.Drawing.Point(256, 374);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 13;
            this.cancelButton.Text = "Cancel";
            this.cancelButton.UseVisualStyleBackColor = false;
            // 
            // addButton
            // 
            this.addButton.BackColor = System.Drawing.Color.LimeGreen;
            this.addButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.addButton.ForeColor = System.Drawing.Color.White;
            this.addButton.Location = new System.Drawing.Point(256, 323);
            this.addButton.Name = "addButton";
            this.addButton.Size = new System.Drawing.Size(75, 33);
            this.addButton.TabIndex = 12;
            this.addButton.Text = "Add";
            this.addButton.UseVisualStyleBackColor = false;
            this.addButton.Click += new System.EventHandler(this.AddButton_Click);
            // 
            // employeeDetailsGroupBox
            // 
            this.employeeDetailsGroupBox.Controls.Add(this.middleTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.lastNameTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.lastNameLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.middleLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.phoneTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.membershipComboBox);
            this.employeeDetailsGroupBox.Controls.Add(this.firstNameTextBox);
            this.employeeDetailsGroupBox.Controls.Add(this.firstNameLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.phoneLabel);
            this.employeeDetailsGroupBox.Controls.Add(this.membershipLabel);
            this.employeeDetailsGroupBox.Location = new System.Drawing.Point(21, 30);
            this.employeeDetailsGroupBox.Name = "employeeDetailsGroupBox";
            this.employeeDetailsGroupBox.Size = new System.Drawing.Size(162, 278);
            this.employeeDetailsGroupBox.TabIndex = 26;
            this.employeeDetailsGroupBox.TabStop = false;
            this.employeeDetailsGroupBox.Text = "Customer Details";
            // 
            // middleTextBox
            // 
            this.middleTextBox.Location = new System.Drawing.Point(7, 91);
            this.middleTextBox.MaxLength = 1;
            this.middleTextBox.Name = "middleTextBox";
            this.middleTextBox.Size = new System.Drawing.Size(133, 20);
            this.middleTextBox.TabIndex = 1;
            this.middleTextBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.MiddleTextBox_KeyPress);
            // 
            // lastNameTextBox
            // 
            this.lastNameTextBox.BackColor = System.Drawing.SystemColors.Window;
            this.lastNameTextBox.Location = new System.Drawing.Point(6, 139);
            this.lastNameTextBox.MaxLength = 100;
            this.lastNameTextBox.Name = "lastNameTextBox";
            this.lastNameTextBox.Size = new System.Drawing.Size(133, 20);
            this.lastNameTextBox.TabIndex = 2;
            this.lastNameTextBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.LastNameTextBox_KeyPress);
            // 
            // lastNameLabel
            // 
            this.lastNameLabel.AutoSize = true;
            this.lastNameLabel.Location = new System.Drawing.Point(3, 123);
            this.lastNameLabel.Name = "lastNameLabel";
            this.lastNameLabel.Size = new System.Drawing.Size(58, 13);
            this.lastNameLabel.TabIndex = 16;
            this.lastNameLabel.Text = "Last Name";
            // 
            // middleLabel
            // 
            this.middleLabel.AutoSize = true;
            this.middleLabel.Location = new System.Drawing.Point(3, 74);
            this.middleLabel.Name = "middleLabel";
            this.middleLabel.Size = new System.Drawing.Size(113, 13);
            this.middleLabel.TabIndex = 15;
            this.middleLabel.Text = "Middle Initial (Optional)";
            // 
            // phoneTextBox
            // 
            this.phoneTextBox.AllowPromptAsInput = false;
            this.phoneTextBox.AsciiOnly = true;
            this.phoneTextBox.Culture = new System.Globalization.CultureInfo("en-US");
            this.phoneTextBox.Location = new System.Drawing.Point(5, 186);
            this.phoneTextBox.Mask = "(000)-000-0000";
            this.phoneTextBox.Name = "phoneTextBox";
            this.phoneTextBox.ResetOnSpace = false;
            this.phoneTextBox.Size = new System.Drawing.Size(132, 20);
            this.phoneTextBox.TabIndex = 3;
            this.phoneTextBox.TextMaskFormat = System.Windows.Forms.MaskFormat.ExcludePromptAndLiterals;
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
            this.membershipComboBox.Location = new System.Drawing.Point(5, 235);
            this.membershipComboBox.Name = "membershipComboBox";
            this.membershipComboBox.Size = new System.Drawing.Size(134, 21);
            this.membershipComboBox.TabIndex = 4;
            // 
            // firstNameTextBox
            // 
            this.firstNameTextBox.BackColor = System.Drawing.SystemColors.Window;
            this.firstNameTextBox.Location = new System.Drawing.Point(6, 42);
            this.firstNameTextBox.MaxLength = 100;
            this.firstNameTextBox.Name = "firstNameTextBox";
            this.firstNameTextBox.Size = new System.Drawing.Size(134, 20);
            this.firstNameTextBox.TabIndex = 0;
            this.firstNameTextBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.FirstNameTextBox_KeyPress);
            // 
            // firstNameLabel
            // 
            this.firstNameLabel.AutoSize = true;
            this.firstNameLabel.Location = new System.Drawing.Point(3, 26);
            this.firstNameLabel.Name = "firstNameLabel";
            this.firstNameLabel.Size = new System.Drawing.Size(57, 13);
            this.firstNameLabel.TabIndex = 14;
            this.firstNameLabel.Text = "First Name";
            // 
            // phoneLabel
            // 
            this.phoneLabel.AutoSize = true;
            this.phoneLabel.Location = new System.Drawing.Point(2, 170);
            this.phoneLabel.Name = "phoneLabel";
            this.phoneLabel.Size = new System.Drawing.Size(38, 13);
            this.phoneLabel.TabIndex = 17;
            this.phoneLabel.Text = "Phone";
            // 
            // membershipLabel
            // 
            this.membershipLabel.AutoSize = true;
            this.membershipLabel.Location = new System.Drawing.Point(2, 219);
            this.membershipLabel.Name = "membershipLabel";
            this.membershipLabel.Size = new System.Drawing.Size(64, 13);
            this.membershipLabel.TabIndex = 18;
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
            this.groupBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.groupBox1.Location = new System.Drawing.Point(202, 30);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(401, 278);
            this.groupBox1.TabIndex = 27;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Customer Attendance";
            // 
            // mondayLabel
            // 
            this.mondayLabel.AutoSize = true;
            this.mondayLabel.Location = new System.Drawing.Point(15, 58);
            this.mondayLabel.Name = "mondayLabel";
            this.mondayLabel.Size = new System.Drawing.Size(48, 13);
            this.mondayLabel.TabIndex = 19;
            this.mondayLabel.Text = "Monday:";
            // 
            // sundayComboBox
            // 
            this.sundayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.sundayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.sundayComboBox.FormattingEnabled = true;
            this.sundayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.sundayComboBox.Location = new System.Drawing.Point(276, 144);
            this.sundayComboBox.Name = "sundayComboBox";
            this.sundayComboBox.Size = new System.Drawing.Size(115, 21);
            this.sundayComboBox.TabIndex = 11;
            // 
            // tuesdayLabel
            // 
            this.tuesdayLabel.AutoSize = true;
            this.tuesdayLabel.Location = new System.Drawing.Point(15, 91);
            this.tuesdayLabel.Name = "tuesdayLabel";
            this.tuesdayLabel.Size = new System.Drawing.Size(51, 13);
            this.tuesdayLabel.TabIndex = 20;
            this.tuesdayLabel.Text = "Tuesday:";
            // 
            // saturdayComboBox
            // 
            this.saturdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.saturdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.saturdayComboBox.FormattingEnabled = true;
            this.saturdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.saturdayComboBox.Location = new System.Drawing.Point(276, 107);
            this.saturdayComboBox.Name = "saturdayComboBox";
            this.saturdayComboBox.Size = new System.Drawing.Size(115, 21);
            this.saturdayComboBox.TabIndex = 10;
            // 
            // wednesdayLabel
            // 
            this.wednesdayLabel.AutoSize = true;
            this.wednesdayLabel.Location = new System.Drawing.Point(15, 126);
            this.wednesdayLabel.Name = "wednesdayLabel";
            this.wednesdayLabel.Size = new System.Drawing.Size(67, 13);
            this.wednesdayLabel.TabIndex = 21;
            this.wednesdayLabel.Text = "Wednesday:";
            // 
            // fridayComboBox
            // 
            this.fridayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.fridayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.fridayComboBox.FormattingEnabled = true;
            this.fridayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.fridayComboBox.Location = new System.Drawing.Point(85, 189);
            this.fridayComboBox.Name = "fridayComboBox";
            this.fridayComboBox.Size = new System.Drawing.Size(115, 21);
            this.fridayComboBox.TabIndex = 9;
            // 
            // thursdayLabel
            // 
            this.thursdayLabel.AutoSize = true;
            this.thursdayLabel.Location = new System.Drawing.Point(15, 160);
            this.thursdayLabel.Name = "thursdayLabel";
            this.thursdayLabel.Size = new System.Drawing.Size(54, 13);
            this.thursdayLabel.TabIndex = 22;
            this.thursdayLabel.Text = "Thursday:";
            // 
            // thursdayComboBox
            // 
            this.thursdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.thursdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.thursdayComboBox.FormattingEnabled = true;
            this.thursdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.thursdayComboBox.Location = new System.Drawing.Point(85, 157);
            this.thursdayComboBox.Name = "thursdayComboBox";
            this.thursdayComboBox.Size = new System.Drawing.Size(115, 21);
            this.thursdayComboBox.TabIndex = 8;
            // 
            // fridayLabel
            // 
            this.fridayLabel.AutoSize = true;
            this.fridayLabel.Location = new System.Drawing.Point(15, 192);
            this.fridayLabel.Name = "fridayLabel";
            this.fridayLabel.Size = new System.Drawing.Size(38, 13);
            this.fridayLabel.TabIndex = 23;
            this.fridayLabel.Text = "Friday:";
            // 
            // wednesdayComboBox
            // 
            this.wednesdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.wednesdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.wednesdayComboBox.FormattingEnabled = true;
            this.wednesdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.wednesdayComboBox.Location = new System.Drawing.Point(85, 123);
            this.wednesdayComboBox.Name = "wednesdayComboBox";
            this.wednesdayComboBox.Size = new System.Drawing.Size(115, 21);
            this.wednesdayComboBox.TabIndex = 7;
            // 
            // saturdayLabel
            // 
            this.saturdayLabel.AutoSize = true;
            this.saturdayLabel.ForeColor = System.Drawing.Color.Firebrick;
            this.saturdayLabel.Location = new System.Drawing.Point(218, 110);
            this.saturdayLabel.Name = "saturdayLabel";
            this.saturdayLabel.Size = new System.Drawing.Size(52, 13);
            this.saturdayLabel.TabIndex = 24;
            this.saturdayLabel.Text = "Saturday:";
            // 
            // tuesdayComboBox
            // 
            this.tuesdayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.tuesdayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.tuesdayComboBox.FormattingEnabled = true;
            this.tuesdayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.tuesdayComboBox.Location = new System.Drawing.Point(85, 88);
            this.tuesdayComboBox.Name = "tuesdayComboBox";
            this.tuesdayComboBox.Size = new System.Drawing.Size(115, 21);
            this.tuesdayComboBox.TabIndex = 6;
            // 
            // sundayLabel
            // 
            this.sundayLabel.AutoSize = true;
            this.sundayLabel.ForeColor = System.Drawing.Color.Firebrick;
            this.sundayLabel.Location = new System.Drawing.Point(218, 147);
            this.sundayLabel.Name = "sundayLabel";
            this.sundayLabel.Size = new System.Drawing.Size(46, 13);
            this.sundayLabel.TabIndex = 25;
            this.sundayLabel.Text = "Sunday:";
            // 
            // mondayComboBox
            // 
            this.mondayComboBox.BackColor = System.Drawing.SystemColors.Window;
            this.mondayComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.mondayComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.mondayComboBox.FormattingEnabled = true;
            this.mondayComboBox.Items.AddRange(new object[] {
            "-- Select --",
            "AM",
            "PM",
            "AM/PM",
            "N/A"});
            this.mondayComboBox.Location = new System.Drawing.Point(85, 55);
            this.mondayComboBox.Name = "mondayComboBox";
            this.mondayComboBox.Size = new System.Drawing.Size(115, 21);
            this.mondayComboBox.TabIndex = 5;
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
            this.CaptionFont = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(34, -4);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(48, 48);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(612, 404);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.employeeDetailsGroupBox);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.addButton);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.Name = "AddCustomerForm";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.ShowMaximizeBox = false;
            this.ShowMinimizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Add Customer";
            this.TopMost = true;
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.AddCustomerForm_FormClosing);
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
        private System.Windows.Forms.TextBox firstNameTextBox;
        private System.Windows.Forms.Label firstNameLabel;
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
        private System.Windows.Forms.MaskedTextBox phoneTextBox;
        private System.Windows.Forms.TextBox lastNameTextBox;
        private System.Windows.Forms.Label lastNameLabel;
        private System.Windows.Forms.Label middleLabel;
        private System.Windows.Forms.TextBox middleTextBox;
    }
}