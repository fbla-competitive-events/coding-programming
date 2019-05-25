namespace fec {
    partial class DatabaseSecurityForm {
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(DatabaseSecurityForm));
            Syncfusion.Windows.Forms.CaptionImage captionImage1 = new Syncfusion.Windows.Forms.CaptionImage();
            this.enableDisableEncButton = new System.Windows.Forms.Button();
            this.enableDisableEncLabel = new System.Windows.Forms.Label();
            this.changePasswordButton = new System.Windows.Forms.Button();
            this.changePasswordLabel = new System.Windows.Forms.Label();
            this.encryptionStatusLabel = new System.Windows.Forms.Label();
            this.encryptionLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // enableDisableEncButton
            // 
            this.enableDisableEncButton.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.enableDisableEncButton.BackColor = System.Drawing.Color.White;
            this.enableDisableEncButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.enableDisableEncButton.Image = ((System.Drawing.Image)(resources.GetObject("enableDisableEncButton.Image")));
            this.enableDisableEncButton.Location = new System.Drawing.Point(148, 68);
            this.enableDisableEncButton.Name = "enableDisableEncButton";
            this.enableDisableEncButton.Size = new System.Drawing.Size(125, 119);
            this.enableDisableEncButton.TabIndex = 5;
            this.enableDisableEncButton.TabStop = false;
            this.enableDisableEncButton.UseVisualStyleBackColor = false;
            this.enableDisableEncButton.Click += new System.EventHandler(this.EnableDisableEncButton_Click);
            // 
            // enableDisableEncLabel
            // 
            this.enableDisableEncLabel.AutoSize = true;
            this.enableDisableEncLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.enableDisableEncLabel.Location = new System.Drawing.Point(148, 47);
            this.enableDisableEncLabel.Name = "enableDisableEncLabel";
            this.enableDisableEncLabel.Size = new System.Drawing.Size(127, 18);
            this.enableDisableEncLabel.TabIndex = 12;
            this.enableDisableEncLabel.Text = "Enable Encryption";
            // 
            // changePasswordButton
            // 
            this.changePasswordButton.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.changePasswordButton.BackColor = System.Drawing.Color.White;
            this.changePasswordButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.changePasswordButton.Image = ((System.Drawing.Image)(resources.GetObject("changePasswordButton.Image")));
            this.changePasswordButton.Location = new System.Drawing.Point(334, 95);
            this.changePasswordButton.Name = "changePasswordButton";
            this.changePasswordButton.Size = new System.Drawing.Size(68, 66);
            this.changePasswordButton.TabIndex = 13;
            this.changePasswordButton.TabStop = false;
            this.changePasswordButton.UseVisualStyleBackColor = false;
            this.changePasswordButton.Click += new System.EventHandler(this.ChangePasswordButton_Click);
            // 
            // changePasswordLabel
            // 
            this.changePasswordLabel.AutoSize = true;
            this.changePasswordLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.changePasswordLabel.Location = new System.Drawing.Point(324, 78);
            this.changePasswordLabel.Name = "changePasswordLabel";
            this.changePasswordLabel.Size = new System.Drawing.Size(93, 13);
            this.changePasswordLabel.TabIndex = 14;
            this.changePasswordLabel.Text = "Change Password";
            // 
            // encryptionStatusLabel
            // 
            this.encryptionStatusLabel.AutoSize = true;
            this.encryptionStatusLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.encryptionStatusLabel.Location = new System.Drawing.Point(1, 93);
            this.encryptionStatusLabel.Name = "encryptionStatusLabel";
            this.encryptionStatusLabel.Size = new System.Drawing.Size(128, 18);
            this.encryptionStatusLabel.TabIndex = 15;
            this.encryptionStatusLabel.Text = "Encryption Status:";
            // 
            // encryptionLabel
            // 
            this.encryptionLabel.AutoSize = true;
            this.encryptionLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.encryptionLabel.ForeColor = System.Drawing.Color.RoyalBlue;
            this.encryptionLabel.Location = new System.Drawing.Point(22, 118);
            this.encryptionLabel.Name = "encryptionLabel";
            this.encryptionLabel.Size = new System.Drawing.Size(77, 18);
            this.encryptionLabel.TabIndex = 16;
            this.encryptionLabel.Text = "ENABLED";
            // 
            // DatabaseSecurityForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CaptionBarColor = System.Drawing.Color.RoyalBlue;
            this.CaptionBarHeight = 42;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionFont = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(18, -3);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(48, 48);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(430, 265);
            this.Controls.Add(this.encryptionLabel);
            this.Controls.Add(this.encryptionStatusLabel);
            this.Controls.Add(this.changePasswordLabel);
            this.Controls.Add(this.changePasswordButton);
            this.Controls.Add(this.enableDisableEncLabel);
            this.Controls.Add(this.enableDisableEncButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.Name = "DatabaseSecurityForm";
            this.ShowIcon = false;
            this.ShowMaximizeBox = false;
            this.ShowMinimizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Database Security";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.DatabaseSecurityForm_FormClosing);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button enableDisableEncButton;
        private System.Windows.Forms.Label enableDisableEncLabel;
        private System.Windows.Forms.Button changePasswordButton;
        private System.Windows.Forms.Label changePasswordLabel;
        private System.Windows.Forms.Label encryptionStatusLabel;
        private System.Windows.Forms.Label encryptionLabel;
    }
}