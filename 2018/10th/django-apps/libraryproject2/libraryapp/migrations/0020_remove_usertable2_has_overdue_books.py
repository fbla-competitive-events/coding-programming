# Generated by Django 2.0 on 2018-01-02 02:55

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('libraryapp', '0019_remove_usertable2_user_book_fine'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='usertable2',
            name='has_overdue_books',
        ),
    ]
