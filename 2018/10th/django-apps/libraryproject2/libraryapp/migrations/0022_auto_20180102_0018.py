# Generated by Django 2.0 on 2018-01-02 05:18

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('libraryapp', '0021_auto_20180101_2214'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usertable2',
            name='user_book_check_out_days_limit',
            field=models.IntegerField(choices=[('S', 7), ('F', 14)], default=7),
        ),
        migrations.AlterField(
            model_name='usertable2',
            name='user_book_checkout_limit',
            field=models.IntegerField(choices=[('S', 3), ('F', 5)], default=3),
        ),
    ]