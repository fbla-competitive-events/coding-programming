# Generated by Django 2.0 on 2017-12-30 01:25

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('libraryapp', '0012_usertable2_hasoverduebooks'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='booktable2',
            options={'verbose_name_plural': 'Catalogue Of Books'},
        ),
        migrations.AlterModelOptions(
            name='usertable2',
            options={'verbose_name_plural': 'List of Current Library Users'},
        ),
        migrations.AlterField(
            model_name='booktable2',
            name='userbookfine',
            field=models.FloatField(blank=True, null=True),
        ),
    ]
