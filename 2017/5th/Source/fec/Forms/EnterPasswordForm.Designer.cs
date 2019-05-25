namespace fec {
    partial class EnterPasswordForm {
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(EnterPasswordForm));
            this.showPasswordCheckBox = new System.Windows.Forms.CheckBox();
            this.passwordLabel = new System.Windows.Forms.Label();
            this.passwordTextBox = new System.Windows.Forms.TextBox();
            this.submitPasswordButton = new System.Windows.Forms.Button();
            this.progressBar = new Syncfusion.Windows.Forms.Tools.ProgressBarAdv();
            this.decryptionWorker = new System.ComponentModel.BackgroundWorker();
            ((System.ComponentModel.ISupportInitialize)(this.progressBar)).BeginInit();
            this.SuspendLayout();
            // 
            // showPasswordCheckBox
            // 
            this.showPasswordCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.showPasswordCheckBox.AutoSize = true;
            this.showPasswordCheckBox.Location = new System.Drawing.Point(91, 90);
            this.showPasswordCheckBox.Name = "showPasswordCheckBox";
            this.showPasswordCheckBox.Size = new System.Drawing.Size(53, 17);
            this.showPasswordCheckBox.TabIndex = 1;
            this.showPasswordCheckBox.TabStop = false;
            this.showPasswordCheckBox.Text = "Show";
            this.showPasswordCheckBox.UseVisualStyleBackColor = true;
            this.showPasswordCheckBox.CheckedChanged += new System.EventHandler(this.ShowPasswordCheckBox_CheckedChanged);
            // 
            // passwordLabel
            // 
            this.passwordLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.passwordLabel.AutoSize = true;
            this.passwordLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.passwordLabel.Location = new System.Drawing.Point(88, 44);
            this.passwordLabel.Name = "passwordLabel";
            this.passwordLabel.Size = new System.Drawing.Size(68, 16);
            this.passwordLabel.TabIndex = 3;
            this.passwordLabel.Text = "Password";
            // 
            // passwordTextBox
            // 
            this.passwordTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.passwordTextBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.passwordTextBox.Location = new System.Drawing.Point(91, 63);
            this.passwordTextBox.Name = "passwordTextBox";
            this.passwordTextBox.Size = new System.Drawing.Size(191, 22);
            this.passwordTextBox.TabIndex = 0;
            this.passwordTextBox.UseSystemPasswordChar = true;
            // 
            // submitPasswordButton
            // 
            this.submitPasswordButton.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.submitPasswordButton.BackColor = System.Drawing.Color.RoyalBlue;
            this.submitPasswordButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.submitPasswordButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.submitPasswordButton.ForeColor = System.Drawing.Color.White;
            this.submitPasswordButton.Location = new System.Drawing.Point(138, 134);
            this.submitPasswordButton.Name = "submitPasswordButton";
            this.submitPasswordButton.Size = new System.Drawing.Size(93, 43);
            this.submitPasswordButton.TabIndex = 2;
            this.submitPasswordButton.Text = "Submit";
            this.submitPasswordButton.UseVisualStyleBackColor = false;
            this.submitPasswordButton.Click += new System.EventHandler(this.SubmitPasswordButton_Click);
            // 
            // progressBar
            // 
            this.progressBar.BackGradientEndColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(211)))), ((int)(((byte)(212)))));
            this.progressBar.BackGradientStartColor = System.Drawing.Color.FromArgb(((int)(((byte)(209)))), ((int)(((byte)(211)))), ((int)(((byte)(212)))));
            this.progressBar.BackgroundStyle = Syncfusion.Windows.Forms.Tools.ProgressBarBackgroundStyles.Gradient;
            this.progressBar.BackMultipleColors = new System.Drawing.Color[] {
        System.Drawing.Color.Empty};
            this.progressBar.BackSegments = false;
            this.progressBar.BackTubeEndColor = System.Drawing.Color.White;
            this.progressBar.BackTubeStartColor = System.Drawing.Color.LightGray;
            this.progressBar.Border3DStyle = System.Windows.Forms.Border3DStyle.Flat;
            this.progressBar.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(147)))), ((int)(((byte)(149)))), ((int)(((byte)(152)))));
            this.progressBar.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.progressBar.CustomText = "";
            this.progressBar.CustomWaitingRender = false;
            this.progressBar.FontColor = System.Drawing.Color.White;
            this.progressBar.ForeColor = System.Drawing.Color.RoyalBlue;
            this.progressBar.ForegroundImage = null;
            this.progressBar.GradientEndColor = System.Drawing.Color.RoyalBlue;
            this.progressBar.GradientStartColor = System.Drawing.Color.RoyalBlue;
            this.progressBar.Location = new System.Drawing.Point(138, 134);
            this.progressBar.Minimum = 10;
            this.progressBar.MultipleColors = new System.Drawing.Color[] {
        System.Drawing.Color.Empty};
            this.progressBar.Name = "progressBar";
            this.progressBar.ProgressFallbackStyle = Syncfusion.Windows.Forms.Tools.ProgressBarStyles.Metro;
            this.progressBar.ProgressStyle = Syncfusion.Windows.Forms.Tools.ProgressBarStyles.WaitingGradient;
            this.progressBar.SegmentWidth = 12;
            this.progressBar.Size = new System.Drawing.Size(93, 43);
            this.progressBar.TabIndex = 10;
            this.progressBar.Text = "progressBarAdv1";
            this.progressBar.TextVisible = false;
            this.progressBar.ThemesEnabled = true;
            this.progressBar.TubeEndColor = System.Drawing.Color.Black;
            this.progressBar.TubeStartColor = System.Drawing.Color.Red;
            this.progressBar.Visible = false;
            this.progressBar.WaitingGradientEnabled = true;
            this.progressBar.WaitingGradientInterval = 60;
            this.progressBar.WaitingGradientWidth = 60;
            // 
            // decryptionWorker
            // 
            this.decryptionWorker.WorkerSupportsCancellation = true;
            this.decryptionWorker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.DecryptionWorker_DoWork);
            this.decryptionWorker.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.DecryptionWorker_WorkCompleted);
            // 
            // EnterPasswordForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CaptionBarColor = System.Drawing.Color.RoyalBlue;
            this.CaptionBarHeight = 46;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionFont = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(30, -1);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(50, 50);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(369, 198);
            this.Controls.Add(this.progressBar);
            this.Controls.Add(this.submitPasswordButton);
            this.Controls.Add(this.showPasswordCheckBox);
            this.Controls.Add(this.passwordLabel);
            this.Controls.Add(this.passwordTextBox);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "EnterPasswordForm";
            this.ShowIcon = false;
            this.ShowMaximizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Enter Password";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.EnterPasswordForm_FormClosing);
            ((System.ComponentModel.ISupportInitialize)(this.progressBar)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.CheckBox showPasswordCheckBox;
        private System.Windows.Forms.Label passwordLabel;
        private System.Windows.Forms.TextBox passwordTextBox;
        private System.Windows.Forms.Button submitPasswordButton;
        private Syncfusion.Windows.Forms.Tools.ProgressBarAdv progressBar;
        private System.ComponentModel.BackgroundWorker decryptionWorker;
    }
}