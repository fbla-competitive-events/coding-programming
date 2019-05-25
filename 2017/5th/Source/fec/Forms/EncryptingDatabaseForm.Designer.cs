namespace fec {
    partial class EncryptingDatabaseForm {
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
            this.progressBar = new Syncfusion.Windows.Forms.Tools.ProgressBarAdv();
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.progressBar)).BeginInit();
            this.SuspendLayout();
            // 
            // progressBar
            // 
            this.progressBar.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.progressBar.BackGradientEndColor = System.Drawing.Color.LightBlue;
            this.progressBar.BackGradientStartColor = System.Drawing.Color.LightBlue;
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
            this.progressBar.Location = new System.Drawing.Point(-1, 87);
            this.progressBar.Minimum = 10;
            this.progressBar.MultipleColors = new System.Drawing.Color[] {
        System.Drawing.Color.Empty};
            this.progressBar.Name = "progressBar";
            this.progressBar.ProgressFallbackStyle = Syncfusion.Windows.Forms.Tools.ProgressBarStyles.Metro;
            this.progressBar.ProgressStyle = Syncfusion.Windows.Forms.Tools.ProgressBarStyles.WaitingGradient;
            this.progressBar.SegmentWidth = 12;
            this.progressBar.Size = new System.Drawing.Size(365, 77);
            this.progressBar.TabIndex = 11;
            this.progressBar.Text = "progressBarAdv1";
            this.progressBar.TextVisible = false;
            this.progressBar.ThemesEnabled = true;
            this.progressBar.TubeEndColor = System.Drawing.Color.Black;
            this.progressBar.TubeStartColor = System.Drawing.Color.Red;
            this.progressBar.WaitingGradientEnabled = true;
            this.progressBar.WaitingGradientInterval = 35;
            this.progressBar.WaitingGradientWidth = 169;
            // 
            // label1
            // 
            this.label1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.label1.AutoSize = true;
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.label1.ForeColor = System.Drawing.Color.White;
            this.label1.Location = new System.Drawing.Point(90, 35);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(192, 20);
            this.label1.TabIndex = 12;
            this.label1.Text = "Encrypting Database...";
            // 
            // EncryptingDatabaseForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.DeepSkyBlue;
            this.ClientSize = new System.Drawing.Size(363, 189);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.progressBar);
            this.DoubleBuffered = true;
            this.DropShadow = true;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.MaximumSize = new System.Drawing.Size(363, 189);
            this.MinimumSize = new System.Drawing.Size(363, 189);
            this.Name = "EncryptingDatabaseForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "EncryptingDatabaseForm";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.EncryptionDatabaseForm_Load);
            ((System.ComponentModel.ISupportInitialize)(this.progressBar)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Syncfusion.Windows.Forms.Tools.ProgressBarAdv progressBar;
        private System.Windows.Forms.Label label1;
    }
}